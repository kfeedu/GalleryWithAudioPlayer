package pl.kfeed.gallerywithmusicplayer.ui.gallery;

import android.database.Cursor;
import android.graphics.Bitmap;

import java.util.Calendar;
import java.util.List;

import pl.kfeed.gallerywithmusicplayer.ui.base.BasePresenter;
import pl.kfeed.gallerywithmusicplayer.ui.base.BaseView;

public interface GalleryContract {

    interface Presenter extends BasePresenter<View> {

        Cursor getThumbnailsAndImageCursor();

        Bitmap getFullImageBitmap(int position);

        String getPathToImageOnPosition(int position);

        Calendar getDateFromImageOnPosition(int position);
    }

    interface View extends BaseView<Presenter> {

        void showError(String err);

        void showToast(String message);
    }
}
