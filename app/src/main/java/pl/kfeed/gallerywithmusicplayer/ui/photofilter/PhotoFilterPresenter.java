package pl.kfeed.gallerywithmusicplayer.ui.photofilter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import javax.inject.Inject;

import pl.kfeed.gallerywithmusicplayer.data.DataManager;

public class PhotoFilterPresenter implements PhotoFilterContract.Presenter {

    DataManager mDataManager;
    PhotoFilterActivity mView;

    @Inject
    public PhotoFilterPresenter(DataManager dataManager,PhotoFilterActivity view){
        mDataManager = dataManager;
        mView = view;
    }

    @Override
    public void setGrowingCirclesPreview(String imgPath, int viewWidth, int viewHeight) {
        mView.setPreviewView(mDataManager.getGrowingCirclesPreview(imgPath, viewWidth, viewHeight));
    }

    @Override
    public void setRotatedCheckerPreview(String imgPath, int viewWidth, int viewHeight) {
        mView.setPreviewView(mDataManager.getRotatedCheckerPreview(imgPath, viewWidth, viewHeight));
    }

    @Override
    public void setWeirdCirclesPreview(String imgPath, int viewWidth, int viewHeight) {
        mView.setPreviewView(mDataManager.getWeirdCirclesPreview(imgPath, viewWidth, viewHeight));
    }

    @Override
    public void attachView(PhotoFilterContract.View view) {

    }

    @Override
    public void detachView() {

    }
}