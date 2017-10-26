package pl.kfeed.gallerywithmusicplayer.ui.gallery;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.data.DataManager;

public final class GalleryPresenter implements GalleryContract.Presenter {

    private static final String TAG = GalleryPresenter.class.getSimpleName();

    private GalleryContract.View mView;
    private Cursor mImageThumbCursor;
    private final DataManager mDataManager;

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
        Log.d(TAG, "Sending image path from position=" + position + " imgPath=" + photoPath);
        return photoPath;
    }

    @Override
    public Calendar getDateFromImageOnPosition(int position) {
        mImageThumbCursor.moveToPosition(position);
        Long date = mImageThumbCursor.getLong(Constants.CURSOR_IMAGE_DATE);
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date * 1000);
        Log.d(TAG, "Date of photo on position " + position + " date=" + date);
        return calendar;
    }
}
