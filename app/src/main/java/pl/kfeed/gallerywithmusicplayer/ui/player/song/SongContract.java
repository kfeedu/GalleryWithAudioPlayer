package pl.kfeed.gallerywithmusicplayer.ui.player.song;

import android.app.Activity;

import pl.kfeed.gallerywithmusicplayer.ui.base.BasePresenter;
import pl.kfeed.gallerywithmusicplayer.ui.base.BaseView;

public interface SongContract {

    interface Presenter extends BasePresenter<View> {

        void prepareService(int position, Activity activity);

        void stopServiceIfNotPlaying();

        void stopSong();

        void playPauseSong();

        void nextSong();

        void prevSong();

        void seekTo(int position);

    }

    interface View extends BaseView<Presenter> {

        void showToast(String msg);

        void setTitle(String newTitle);

        void setArtist(String newArtist);

        void setPauseButton();

        void setPlayButton();

        void setMaxDuration(int maxDuration);

        void updateProgress(int progress);
    }
}
