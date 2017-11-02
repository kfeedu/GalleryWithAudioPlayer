package pl.kfeed.gallerywithmusicplayer.ui.player.song;

import pl.kfeed.gallerywithmusicplayer.ui.base.BasePresenter;
import pl.kfeed.gallerywithmusicplayer.ui.base.BaseView;

/**
 * Created by Kfeed on 01.11.2017.
 */

public interface SongContract {
    interface Presenter extends BasePresenter<View> {

    }

    interface View extends BaseView<Presenter> {

    }
}
