package pl.kfeed.gallerywithmusicplayer.data;

import android.database.Cursor;
import android.graphics.Bitmap;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.kfeed.gallerywithmusicplayer.data.local.DbHelper;
import pl.kfeed.gallerywithmusicplayer.data.local.StorageHelper;
import pl.kfeed.gallerywithmusicplayer.data.local.filter.FilterHelper;
import pl.kfeed.gallerywithmusicplayer.data.model.Song;

@Singleton
public class DataManager {

    private final StorageHelper mStorageHelper;
    private final FilterHelper mFilterHelper;
    private final DbHelper mDbHelper;

    @Inject
    DataManager(StorageHelper storageHelper, FilterHelper filterHelper, DbHelper dbHelper) {
        mFilterHelper = filterHelper;
        mStorageHelper = storageHelper;
        mDbHelper = dbHelper;
    }

    public Cursor getPhotoAndThumbCursor() {
        return mStorageHelper.getCursorToImagesAndThumbnails();
    }

    public Bitmap getPhotoFromPath(String filePath) {
        return mStorageHelper.loadImageFromStorage(filePath);
    }

    public Bitmap getDecodedSampledBitmap(String filePath, int viewWidth, int viewHeight) {
        return mFilterHelper.decodeSampledBitmapFromResource(filePath, viewWidth, viewHeight);
    }

    public Single<Bitmap> getGrowingCirclesPreview(String filePath, int viewWidth, int viewHeight) {
        return mFilterHelper.generateGrowingCircles(getDecodedSampledBitmap(filePath, viewWidth, viewHeight));
    }

    public Single<Bitmap> getWeirdCirclesPreview(String filePath, int viewWidth, int viewHeight) {
        return mFilterHelper.generateWeirdCircles(getDecodedSampledBitmap(filePath, viewWidth, viewHeight));
    }

    public Single<Bitmap> getRotatedCheckerPreview(String filePath, int viewWidth, int viewHeight) {
        return mFilterHelper.generateRotatedChecker(getDecodedSampledBitmap(filePath, viewWidth, viewHeight));
    }

    public boolean saveImageToAppDir(Bitmap image, String fileName) {
        return mStorageHelper.saveImageToInternalStorage(image, fileName + ".jpg");
    }

    public Cursor getSongCursor(){
        return mStorageHelper.getSongCursor();
    }

    public Song getSong(String songId){
        return mDbHelper.getSong(songId);
    }

    public void updateSong(String songId, int pauseTime){
        mDbHelper.updateSong(songId, pauseTime);
    }

    public void removeSong(String songId){
        mDbHelper.removeSongFromDb(songId);
    }
}
