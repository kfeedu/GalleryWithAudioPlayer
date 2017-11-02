package pl.kfeed.gallerywithmusicplayer.data.local;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
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

import pl.kfeed.gallerywithmusicplayer.Constants;
import pl.kfeed.gallerywithmusicplayer.R;

@Singleton
public class StorageHelper {

    private static final String TAG = StorageHelper.class.getSimpleName();

    private Context mContext;

    @Inject
    public StorageHelper(Context context) {
        mContext = context;
    }

    public boolean saveImageToInternalStorage(Bitmap bitmapImage, String fileName) {
        File directory = getAlbumStorageDir(Constants.PHOTO_SAVING_DIRECTORY);
        File myPath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        galleryAddPic(directory.getAbsolutePath());
        Log.d(TAG, "Image saved to: " + directory.getAbsolutePath());
        return true;
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

    private void galleryAddPic(String photoPath) {
        File file = new File(photoPath);
        MediaScannerConnection.scanFile(mContext,
                new String[]{file.toString()}, null,
                (path, uri) -> {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                });
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

    public Cursor getSongCursor(){
        return mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Audio.Media.DATE_ADDED);
    }

    public Cursor joinImageAndThumbCursors(Cursor imageCursor, Cursor thumbCursor) {
        // join these and return
        // the join is on images._ID = thumbnails.IMAGE_ID
        for (int i = 0; i < thumbCursor.getColumnCount(); i++) {
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
        for (int i = 0; i < retCursor.getCount(); i++) {
            retCursor.moveToPosition(i);
            Log.d(TAG, "ThumbPath=" + retCursor.getString(0) + " imageId=" + retCursor.getLong(1) + " imgPath=" + retCursor.getString(6));
        }
        thumbCursor.close();
        imageCursor.close();
        return retCursor;
    }


}
