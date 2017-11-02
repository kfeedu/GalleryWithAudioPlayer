package pl.kfeed.gallerywithmusicplayer;

import android.provider.MediaStore;

public class Constants {

    //Column ID's in Thumb/Image Cursor
    public static final int CURSOR_THUMB_PATH = 0;
    public static final int CURSOR_IMAGE_ID = 1;
    public static final int CURSOR_IMAGE_TITLE = 2;
    public static final int CURSOR_IMAGE_DESC = 3;
    public static final int CURSOR_IMAGE_DATE = 4;
    public static final int CURSOR_IMAGE_NAME = 5;
    public static final int CURSOR_IMAGE_PATH = 6;

    //Intent keys
    public static final String PHOTO_FILTER_INTENT_PHOTO_PATH = "photoToEdit";
    public static final String SONG_ACTIVITY_INTENT_POSITION = "songPosition";

    //App directory
    public static final String PHOTO_SAVING_DIRECTORY = "GalleryProcessedImages";

}
