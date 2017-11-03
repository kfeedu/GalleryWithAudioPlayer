package pl.kfeed.gallerywithmusicplayer.ui.photofilter;

import android.graphics.Bitmap;

import pl.kfeed.gallerywithmusicplayer.ui.base.BasePresenter;
import pl.kfeed.gallerywithmusicplayer.ui.base.BaseView;

public interface PhotoFilterContract {

    interface Presenter extends BasePresenter<PhotoFilterContract.View> {

        void setWeirdCirclesPreview(String imgPath, int viewWidth, int viewHeight);

        void setGrowingCirclesPreview(String imgPath, int viewWidth, int viewHeight);

        void setRotatedCheckerPreview(String imgPath, int viewWidth, int viewHeight);

        void stopPhotoProcessing();

        void savePhoto(Bitmap bitmap);

    }

    interface View extends BaseView<PhotoFilterPresenter> {

        void setPreviewView(Bitmap bitmap);

        void showError(String err);

        void showToast(String message);

        void showProgressBar();

        void dismissProgressBar();
    }
}
