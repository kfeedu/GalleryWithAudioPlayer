package pl.kfeed.gallerywithmusicplayer.ui.gallery;

import android.database.Cursor;

import javax.inject.Inject;

import pl.kfeed.gallerywithmusicplayer.data.DataManager;

public final class GalleryPresenter implements GalleryContract.Presenter {

    private static final String TAG = GalleryPresenter.class.getSimpleName();

    private GalleryContract.View mView;
    private DataManager mDataManager;

    @Inject
    GalleryPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(GalleryContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public Cursor getThumbnailsAndImageCursor() {
        return mDataManager.getPhotoAndThumbCursor();
    }

    @Override
    public void refreshData() {
        mView.updateAdapter(getThumbnailsAndImageCursor());
    }
}
