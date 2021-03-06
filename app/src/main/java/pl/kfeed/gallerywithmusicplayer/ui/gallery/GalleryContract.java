package pl.kfeed.gallerywithmusicplayer.ui.gallery;

import android.database.Cursor;

import pl.kfeed.gallerywithmusicplayer.ui.base.BasePresenter;
import pl.kfeed.gallerywithmusicplayer.ui.base.BaseView;

public interface GalleryContract {

    interface Presenter extends BasePresenter<View> {

        Cursor getThumbnailsAndImageCursor();

        void refreshData();

    }

    interface View extends BaseView<Presenter> {

        void updateAdapter(Cursor imageAndThumbCursor);

        void showToast(String message);
    }
}
