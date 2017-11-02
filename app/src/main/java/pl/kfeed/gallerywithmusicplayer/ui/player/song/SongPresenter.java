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
import android.util.Log;

import javax.inject.Inject;

import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.data.DataManager;
import pl.kfeed.gallerywithmusicplayer.data.model.Song;
import pl.kfeed.gallerywithmusicplayer.service.MusicPlayerService;

public class SongPresenter implements SongContract.Presenter {

    private static final String TAG = SongPresenter.class.getSimpleName();

    private Context mContext;
    private SongContract.View mView;
    private DataManager mDataManager;
    private MusicPlayerService mMusicService;
    private Cursor mSongCursor;

    private boolean mIsBound;
    private int mActualShowingSongPosition;

    @Inject
    public SongPresenter(Context context, DataManager dataManager, SongActivity view) {
        mDataManager = dataManager;
        mView = view;
        mContext = context;
    }

    @Override
    public void stopSong() {
        mMusicService.stopSong();
        mView.setPlayButton();
        mDataManager.removeSong(getSongId(mMusicService.getActualPlayingSongPosition()));
    }

    @Override
    public void playPauseSong() {
        boolean isViewCorrelatedToCurrentSong = mActualShowingSongPosition == mMusicService.getActualPlayingSongPosition();
        if (isViewCorrelatedToCurrentSong) {
            if(mMusicService.isPlaying()){
                pauseSong();
            }else if(mMusicService.isPaused()){
                resumeSong();
            }else{
                playSong();
            }
        }else{
            playSong();
        }
    }

    private void pauseSong() {
        mMusicService.pauseSong();
        mView.setPlayButton();
        mDataManager.updateSong(getSongId(mMusicService.getActualPlayingSongPosition()),
                mMusicService.getCurrentPlayingTime());
        mView.showToast(mContext.getString(R.string.pause_position_saved));
    }

    private void playSong() {
        mMusicService.startPlayingSong(getSongData(mActualShowingSongPosition), mActualShowingSongPosition);
        Song currentShowingSong = mDataManager.getSong(getSongId(mActualShowingSongPosition));
        if(currentShowingSong != null){
            mMusicService.seekTo(currentShowingSong.pauseTime);
        }
        mView.setPauseButton();
        mView.showToast(mContext.getString(R.string.track_resumed_from_saved));
    }

    private void resumeSong() {
        mMusicService.resumeSong();
        mDataManager.removeSong(getSongId(mMusicService.getActualPlayingSongPosition()));
        mView.setPauseButton();
    }

    @Override
    public void nextSong() {
        if (mSongCursor.getCount() > mActualShowingSongPosition + 1) {
            mActualShowingSongPosition++;
            setupViewToNewSong();
            playSong();
        } else {
            mView.showToast(mContext.getString(R.string.last_song));
        }
    }

    @Override
    public void prevSong() {
        if (mActualShowingSongPosition > 0) {
            mActualShowingSongPosition--;
            setupViewToNewSong();
            playSong();
        } else {
            mView.showToast(mContext.getString(R.string.first_song));
        }
    }

    private void setupViewToNewSong() {
        mSongCursor.moveToPosition(mActualShowingSongPosition);
        String newArtist = mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        String newTitle = mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        int duration = getDuration(mActualShowingSongPosition);
        mView.setArtist(newArtist);
        mView.setTitle(newTitle);
        mView.setMaxDuration(duration / 1000);
    }


    private void setupSeekBarHandler(Activity activity){
        final Handler seekBarHandler = new Handler();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMusicService != null) {
                    boolean isViewCorrelatedToCurrentSong = mActualShowingSongPosition == mMusicService.getActualPlayingSongPosition();
                    if(isViewCorrelatedToCurrentSong){
                        mView.updateProgress(mMusicService.getCurrentPlayingTime() / 1000);
                    }else{
                        mView.updateProgress(0);
                    }
                }
                seekBarHandler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void prepareService(int newSongPosition, Activity activity) {
        if (mSongCursor == null)
            mSongCursor = mDataManager.getSongCursor();
        if (!mIsBound)
            doBindService();

        mActualShowingSongPosition = newSongPosition;
        setupViewToNewSong();
        setupSeekBarHandler(activity);

        final Handler handler = new Handler();
        handler.postDelayed(() -> registerForCallbacks(activity), 200);
    }

    private void registerForCallbacks(Activity activity) {
        if (mIsBound) mMusicService.registerCallbackClient(activity);
    }

    @Override
    public void seekTo(int position) {
        mMusicService.seekTo(position);
    }

    @Override
    public void stopServiceIfNotPlaying() {
        if (!mMusicService.isPlaying()) {
            doUnbindService();
        }
    }

    private String getSongId(int position) {
        mSongCursor.moveToPosition(position);
        return mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media._ID));
    }

    private String getSongData(int position) {
        mSongCursor.moveToPosition(position);
        return "file://" +  mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
    }

    private int getDuration(int position) {
        mSongCursor.moveToPosition(position);
        return Integer.valueOf(mSongCursor.getString(mSongCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
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
