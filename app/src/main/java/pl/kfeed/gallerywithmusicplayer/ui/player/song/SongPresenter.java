package pl.kfeed.gallerywithmusicplayer.ui.player.song;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.Toast;

import javax.inject.Inject;

import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.data.DataManager;
import pl.kfeed.gallerywithmusicplayer.data.model.Song;
import pl.kfeed.gallerywithmusicplayer.service.MusicPlayerService;

public class SongPresenter implements SongContract.Presenter {

    private SongContract.View mView;
    private DataManager mDataManager;

    private MusicPlayerService mMusicService;
    private boolean mIsBound;
    private Context mContext;
    private Cursor mSongCursor;
    private int actualPosition;
    private boolean dataHasChangedWhilePlaying = false;

    @Inject
    public SongPresenter(Context context, DataManager dataManager, SongActivity view) {
        mDataManager = dataManager;
        mView = view;
        mContext = context;
    }

    @Override
    public void stopSong() {
        mMusicService.stopPlaying();
        mView.setPlayButton();
        mDataManager.removeSong(getSongId());
    }

    @Override
    public void playPauseSong() {
        if (mMusicService.isPlaying() && !dataHasChangedWhilePlaying) {
            pauseSong();
            mView.setPlayButton();
        } else {
            if (mMusicService.getCurrentPosition() != 0 && !dataHasChangedWhilePlaying)
                unpauseSong();
            else
                playSong();
            mView.setPauseButton();
        }
    }

    private void pauseSong() {
        mMusicService.pausePlaying();
        mDataManager.updateSong(getSongId(), mMusicService.getCurrentPosition());
        mView.showToast(mContext.getString(R.string.pause_position_saved));
    }

    private void playSong() {
        mSongCursor.moveToPosition(actualPosition);
        String filePath = "file://" + getSongData();
        Song currentSong;
        if ((currentSong = mDataManager.getSong(getSongId())) != null) {
            mMusicService.startPlayingFromPosition(filePath, currentSong.pauseTime);
            mView.showToast(mContext.getString(R.string.track_resumed_from_saved));
        } else {
            mMusicService.startPlaying(filePath);
        }
        mView.setPauseButton();
        dataHasChangedWhilePlaying = false;
    }

    private void unpauseSong() {
        mMusicService.unPausePlaying();
        mDataManager.removeSong(getSongId());
    }

    @Override
    public void nextSong() {
        if (mSongCursor.getCount() > actualPosition + 1) {
            actualPosition++;
            setViewsWithSongData();
            playSong();
        } else {
            mView.showToast(mContext.getString(R.string.last_song));
        }
    }

    @Override
    public void prevSong() {
        if (actualPosition > 0) {
            actualPosition--;
            setViewsWithSongData();
            playSong();
        } else {
            mView.showToast(mContext.getString(R.string.first_song));
        }
    }

    private void setViewsWithSongData() {
        mSongCursor.moveToPosition(actualPosition);
        String newArtist = mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        String newTitle = mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        mView.setArtist(newArtist);
        mView.setTitle(newTitle);
    }

    @Override
    public void prepareService(int position) {
        if (mSongCursor == null)
            mSongCursor = mDataManager.getSongCursor();
        if (!mIsBound)
            doBindService();
        if (position != actualPosition) {
            actualPosition = position;
            dataHasChangedWhilePlaying = true;
        }
        setViewsWithSongData();
    }

    @Override
    public void registerForCallbacks(Activity activity) {
        if (mIsBound) mMusicService.registerCallbackClient(activity);
    }


    @Override
    public void stopServiceIfNotPlaying() {
        if (!mMusicService.isPlaying()) {
            doUnbindService();
        }
    }

    private String getSongId() {
        return mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media._ID));
    }

    private String getSongData() {
        return mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
    }

    @Override
    public void attachView(SongContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mMusicService = ((MusicPlayerService.LocalBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            mMusicService = null;
        }
    };

    private void doBindService() {
        mContext.bindService(new Intent(mContext,
                MusicPlayerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }
}
