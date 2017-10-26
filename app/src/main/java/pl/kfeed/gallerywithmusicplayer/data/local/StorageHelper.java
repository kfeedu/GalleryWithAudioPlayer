package pl.kfeed.gallerywithmusicplayer.data.local;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.kfeed.gallerywithmusicplayer.R;

/**
 * Created by Kfeed on 22.10.2017.
 */

@Singleton
public class StorageHelper {

    private static final String TAG = StorageHelper.class.getSimpleName();

    private static final Uri IMAGE_INTERNAL_STORAGE_URI = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    private static final Uri IMAGE_EXTERNAL_STORAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final Uri THUMB_INTERNAL_STORAGE_URI = MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI;
    private static final Uri THUMB_EXTERNAL_STORAGE_URI = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;

    Context mContext;

    @Inject
    public StorageHelper(Context context) {
        mContext = context;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    private String saveImageToInternalStorage(Bitmap bitmapImage, String fileName) {
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE);
        File myPath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path) {

        Bitmap bitmap = null;
        try {
            File file = new File(path);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    public String[] fetchImagePathsFromExternalStorage() {
        return fetchImagesPathFromStorage(IMAGE_EXTERNAL_STORAGE_URI);
    }

    public String[] fetchImagePathsFromInternalStorage() {
        return fetchImagesPathFromStorage(IMAGE_INTERNAL_STORAGE_URI);
    }

    private String[] fetchImagesPathFromStorage(Uri storageType) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = MediaStore.Images.Media.query(mContext.getContentResolver(), storageType, columns, null, orderBy);
//                mContext.getContentResolver().query(
//                storageType, columns, null,
//                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            Log.i("PATH", arrPath[i]);
        }
        // The cursor should be freed up after use with close()
        cursor.close();

        return arrPath;
    }

    public Cursor getCursorToImagesAndThumbnails() {
        return joinImageAndThumbCursors(getImageCursor(), getThumbnailCursor());
    }

    private Cursor getThumbnailCursor() {
        return mContext.getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                null, null, null,
                "(" + MediaStore.Images.Thumbnails.IMAGE_ID + "*(-1))");
    }

    private Cursor getImageCursor() {
        return mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                "(" + MediaStore.Images.Media._ID + "*(-1))");
    }

    public Cursor joinImageAndThumbCursors(Cursor imageCursor, Cursor thumbCursor) {
        // join these and return
        // the join is on images._ID = thumbnails.IMAGE_ID
        for(int i = 0; i< thumbCursor.getColumnCount(); i++){
            Log.d(TAG, "Id=" + i + " ColumnName=" + thumbCursor.getColumnName(i));
        }

        CursorJoiner joiner = new CursorJoiner(
                thumbCursor, new String[]{MediaStore.Images.Thumbnails.IMAGE_ID},  // left = thumbnails
                imageCursor, new String[]{MediaStore.Images.Media._ID}   // right = images
        );

        String[] projection = {"thumb_path", "ID", "title", "desc", "datetaken", "filename", "image_path"};

        MatrixCursor retCursor = new MatrixCursor(projection);

        try {
            for (CursorJoiner.Result joinerResult : joiner) {

                switch (joinerResult) {
                    case LEFT:
                        // handle case where a row in cursorA is unique
                        // images is unique (missing thumbnail)

                        // we want to show ALL images, even (new) ones without thumbnail!
                        // data = null will cause a temporary thumbnail to be generated in PhotoAdapter.bindView()

                        retCursor.addRow(new Object[]{
                                null, // data
                                imageCursor.getLong(0), // image id
                                imageCursor.getString(5), // title
                                imageCursor.getString(8),  // desc
                                imageCursor.getLong(6),  // date
                                imageCursor.getString(3),  // filename
                                imageCursor.getString(1)  //image path
                        });

                        // compensate for CursorJoiner expecting cursors ordered ascending...
                        imageCursor.moveToNext();
                        thumbCursor.moveToPrevious();
                        break;

                    case RIGHT:
                        // handle case where a row in cursorB is unique
                        // thumbs is unique (missing image)

                        // compensate for CursorJoiner expecting cursors ordered ascending...
                        thumbCursor.moveToNext();
                        imageCursor.moveToPrevious();
                        break;

                    case BOTH:

                        // handle case where a row with the same key is in both cursors
                        retCursor.addRow(new Object[]{
                                thumbCursor.getString(1), // data
                                imageCursor.getLong(0), // image id
                                imageCursor.getString(5), // title
                                imageCursor.getString(8),  // desc
                                imageCursor.getLong(6),  // date
                                imageCursor.getString(3),  // filename
                                imageCursor.getString(1)  //image path
                        });

                        break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "JOIN FAILED: " + e);
        }

        for(int i = 0; i <retCursor.getCount(); i++){
            retCursor.moveToPosition(i);
            Log.d(TAG, "ThumbPath=" + retCursor.getString(0) + " imageId=" + retCursor.getLong(1) + " imgPath=" + retCursor.getString(6));

        }

        thumbCursor.close();
        imageCursor.close();

        return retCursor;
    }

//    public void generateThumbnails() {
//        Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        mediaScan.setData(IMAGE_EXTERNAL_STORAGE_URI);
//        mContext.sendBroadcast(mediaScan);
//    }

    public String[] getThumbPaths() {
        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
        String orderBy = MediaStore.Images.Thumbnails._ID;
        Cursor cursor = mContext.getContentResolver().query(THUMB_EXTERNAL_STORAGE_URI, projection, null, null, orderBy);
        String[] paths = new String[cursor.getCount()];
        Log.d(TAG,"" + cursor.getColumnCount());
        Log.d(TAG,"" + cursor.getCount());

        int columnDataIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
        int columnIdIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
        int columnImageId = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);

//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToPosition(i);
//            Log.d(TAG, "ThumbPath: id=" + cursor.getLong(columnIdIndex) + " imageId=" + cursor.getLong(columnImageId) + " path=" + cursor.getString(columnDataIndex));
//            paths[i] = cursor.getString(columnDataIndex);
//        }
        cursor.close();

        return paths;
    }

    public String[] getImagePaths() {
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = mContext.getContentResolver().query(IMAGE_EXTERNAL_STORAGE_URI, projection, null, null, orderBy);
        String[] paths = new String[cursor.getCount()];
        Log.d(TAG,"" + cursor.getColumnCount());
        Log.d(TAG,"" + cursor.getCount());

        int columnDataIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
        int columnIdIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToPosition(i);
//            Log.d(TAG, "ImagePath: id=" + cursor.getLong(columnIdIndex) + " path=" + cursor.getString(columnDataIndex));
//            paths[i] = cursor.getString(columnDataIndex);
//        }
        cursor.close();

        return paths;
    }


}
