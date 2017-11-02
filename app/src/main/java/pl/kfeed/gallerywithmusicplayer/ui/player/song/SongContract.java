package pl.kfeed.gallerywithmusicplayer.ui.player.song;

import android.app.Activity;

import pl.kfeed.gallerywithmusicplayer.ui.base.BasePresenter;
import pl.kfeed.gallerywithmusicplayer.ui.base.BaseView;

/**
 * Created by Kfeed on 01.11.2017.
 */

public interface SongContract {

    interface Presenter extends BasePresenter<View> {

        void prepareService(int position);

        void stopServiceIfNotPlaying();

        void stopSong();

        void playPauseSong();

        void nextSong();

        void prevSong();

        void registerForCallbacks(Activity activity);


    }

    interface View extends BaseView<Presenter> {

        void showToast(String msg);

        void setTitle(String newTitle);

        void setArtist(String newArtist);

        void setPauseButton();

        void setPlayButton();
    }
}
