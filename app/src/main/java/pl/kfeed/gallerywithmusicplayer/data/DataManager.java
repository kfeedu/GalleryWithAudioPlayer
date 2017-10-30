package pl.kfeed.gallerywithmusicplayer.data;

import android.database.Cursor;
import android.graphics.Bitmap;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.kfeed.gallerywithmusicplayer.data.local.StorageHelper;
import pl.kfeed.gallerywithmusicplayer.data.local.filter.FilterHelper;

@Singleton
public class DataManager {

    private final StorageHelper mStorageHelper;
    private final FilterHelper mFilterHelper;

    @Inject
    DataManager(StorageHelper storageHelper, FilterHelper filterHelper){
        mFilterHelper = filterHelper;
        mStorageHelper = storageHelper;
    }

    public String[] getPhotoPathsFromInternalStorage(){
        return mStorageHelper.fetchImagePathsFromInternalStorage();
    }

    public String[] getPhotoPathsFromExternalStorage(){
        return mStorageHelper.fetchImagePathsFromExternalStorage();
    }

    public Cursor getPhotoAndThumbCursor(){
        return mStorageHelper.getCursorToImagesAndThumbnails();
    }

    public Bitmap getPhotoFromPath(String filePath){
        return mStorageHelper.loadImageFromStorage(filePath);
    }

    public Bitmap getDecodedSampledBitmap(String filePath, int viewWidth, int viewHeight){
        return mFilterHelper.decodeSampledBitmapFromResource(filePath, viewWidth, viewHeight);
    }

    public Bitmap getGrowingCirclesPreview(String filePath,int  viewWidth,int viewHeight){
        return mFilterHelper.generateGrowingCircles(getDecodedSampledBitmap(filePath, viewWidth, viewHeight ));
    }

    public Bitmap getWeirdCirclesPreview(String filePath, int viewWidth, int viewHeight) {
        return mFilterHelper.generateWeirdCircles(getDecodedSampledBitmap(filePath, viewWidth, viewHeight));
    }

    public Bitmap getRotatedCheckerPreview(String filePath, int viewWidth, int viewHeight) {
        return mFilterHelper.generateRotatedChecker(getDecodedSampledBitmap(filePath, viewWidth, viewHeight));
    }
}
