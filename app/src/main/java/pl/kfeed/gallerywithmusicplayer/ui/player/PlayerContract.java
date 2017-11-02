package pl.kfeed.gallerywithmusicplayer.ui.player;

import android.database.Cursor;

import pl.kfeed.gallerywithmusicplayer.ui.base.BasePresenter;
import pl.kfeed.gallerywithmusicplayer.ui.base.BaseView;

/**
 * Created by Kfeed on 20.10.2017.
 */

public interface PlayerContract {

    interface Presenter extends BasePresenter<View> {
        Cursor getSongCursor();

        void refreshData();
    }

    interface View extends BaseView<Presenter> {
        void updateAdapter(Cursor songCursor);

        void showToast(String msg);
    }
}
