package pl.kfeed.gallerywithmusicplayer.ui.gallery;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.dagger.ActivityScoped;
import pl.kfeed.gallerywithmusicplayer.data.DataManager;

/**
 * Created by Kfeed on 19.10.2017.
 */

public final class GalleryPresenter implements GalleryContract.Presenter {

    private static final String TAG = GalleryPresenter.class.getSimpleName();

    GalleryContract.View mView;

    private Cursor mImageThumbCursor;

    private final DataManager mDataManager;

    @Nullable
    private GalleryContract.View mGalleryView;

    @Inject
    GalleryPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void takeView(GalleryContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public Cursor getThumbnailsAndImageCursor() {
        mImageThumbCursor = mDataManager.getPhotoAndThumbCursor();
        return mImageThumbCursor;
    }

    @Override
    public Bitmap getFullImageBitmap(int position) {
        mImageThumbCursor.moveToPosition(position);
        Log.d(TAG, "Full image bitmap is about to be send further");
        return mDataManager.getPhotoFromPath(mImageThumbCursor.getString(Constants.CURSOR_IMAGE_PATH));
    }

    @Override
    public String getPathToImageOnPosition(int position) {
        mImageThumbCursor.moveToPosition(position);
        String photoPath = mImageThumbCursor.getString(Constants.CURSOR_IMAGE_PATH);
        Log.d(TAG, "Sending image path from position=" + position + " imgPath=" + photoPath );
        return photoPath;
    }

    @Override
    public String getDateFromImageOnPosition(int position) {
        mImageThumbCursor.moveToPosition(position);
        String date = mImageThumbCursor.getString(Constants.CURSOR_IMAGE_DATE);
        Log.d(TAG, "Date of photo on position " + position + " date=" + date);
        return date;
    }
}
