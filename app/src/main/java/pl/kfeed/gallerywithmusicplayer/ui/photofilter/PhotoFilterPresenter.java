package pl.kfeed.gallerywithmusicplayer.ui.photofilter;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.kfeed.gallerywithmusicplayer.R;
import pl.kfeed.gallerywithmusicplayer.data.DataManager;

public class PhotoFilterPresenter implements PhotoFilterContract.Presenter {

    private static final String TAG = PhotoFilterActivity.class.getSimpleName();

    private DataManager mDataManager;
    private PhotoFilterActivity mView;
    private Disposable mDisposable;
    private SingleObserver<Bitmap> previewObserver = new SingleObserver<Bitmap>() {

        @Override
        public void onSubscribe(Disposable disposable) {
            mDisposable = disposable;
        }

        @Override
        public void onSuccess(Bitmap bitmap) {
            mView.setPreviewView(bitmap);
            mView.dismissProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            mView.showError("Photo can't be edited due to bad file format.");
            e.printStackTrace();
        }
    };

    @Inject
    public PhotoFilterPresenter(DataManager dataManager, PhotoFilterActivity view) {
        mDataManager = dataManager;
        mView = view;
    }

    @Override
    public void setGrowingCirclesPreview(String imgPath, int viewWidth, int viewHeight) {
        mDataManager.getGrowingCirclesPreview(imgPath, viewWidth, viewHeight)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(previewObserver);
    }

    @Override
    public void setRotatedCheckerPreview(String imgPath, int viewWidth, int viewHeight) {
        mDataManager.getRotatedCheckerPreview(imgPath, viewWidth, viewHeight)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(previewObserver);
    }

    @Override
    public void setWeirdCirclesPreview(String imgPath, int viewWidth, int viewHeight) {
        mDataManager.getWeirdCirclesPreview(imgPath, viewWidth, viewHeight)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(previewObserver);
    }

    @Override
    public void savePhoto(Bitmap image) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        if (mDataManager.saveImageToAppDir(image, sdf.format(new Date()))) {
            mView.showToast(mView.getString(R.string.image_save_success));
        } else {
            mView.showError(mView.getString(R.string.image_save_fail));
        }
    }

    @Override
    public void stopPhotoProcessing() {
        if (mDisposable != null)
            mDisposable.dispose();
    }

    @Override
    public void attachView(PhotoFilterContract.View view) {

    }

    @Override
    public void detachView() {

    }
}