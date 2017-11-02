package pl.kfeed.gallerywithmusicplayer.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.android.DaggerService;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.ui.player.song.SongActivity;

@Singleton
public class MusicPlayerService extends DaggerService {

    private static final String TAG = MusicPlayerService.class.getSimpleName();

    private Callbacks mCallbackActivity;

    private NotificationManager mNotificationManager;
    private int NOTIFICATION = R.string.service_started;
    private final IBinder mBinder = new LocalBinder();

    private MediaPlayer mMediaPlayer;
    private int currentPosition;
    private int currentSongId;

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
        mMediaPlayer = new MediaPlayer();
    }


    public void registerCallbackClient(Activity activity) {
        mCallbackActivity = (Callbacks) activity;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mNotificationManager.cancel(NOTIFICATION);
        Toast.makeText(this, R.string.service_stopped, Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void showNotification() {
        CharSequence text = getText(R.string.service_is_running);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SongActivity.class), 0);
        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.rick)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.service_name))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .build();
        mNotificationManager.notify(NOTIFICATION, notification);
    }

    public void startPlaying(String filePath) {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        currentPosition = 0;
    }

    public void pausePlaying() {
        mMediaPlayer.pause();
        currentPosition = mMediaPlayer.getCurrentPosition();
    }

    public void unPausePlaying() {
        mMediaPlayer.seekTo(currentPosition);
        mMediaPlayer.start();
    }

    public void startPlayingFromPosition(String filePath, int position) {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.seekTo(position);
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seekTo(int position){
        mMediaPlayer.pause();
        mMediaPlayer.seekTo(position);
        mMediaPlayer.start();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public int getRealPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getCurrentSongId() {
        return currentSongId;
    }

    public void setCurrentSongId(int currentSongId) {
        this.currentSongId = currentSongId;
    }

    private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlaying();
            mCallbackActivity.songEnded();
        }
    };
    private MediaPlayer.OnErrorListener onError = (mp, what, extra) -> false;

    //Callbacks interface for communication with SongActivity
    public interface Callbacks {
        void songEnded();
    }
}
