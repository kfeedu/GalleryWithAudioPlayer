package pl.kfeed.gallerywithmusicplayer.data;

import android.database.Cursor;
import android.graphics.Bitmap;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.kfeed.gallerywithmusicplayer.data.local.StorageHelper;

@Singleton
public class DataManager {

    private final StorageHelper mStorageHelper;

    @Inject
    DataManager(StorageHelper storageHelper){
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
}
