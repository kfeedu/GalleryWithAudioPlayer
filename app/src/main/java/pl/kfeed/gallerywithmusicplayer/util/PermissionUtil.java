package pl.kfeed.gallerywithmusicplayer.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionUtil {

    private static final int REQUEST_WRITE_PERMISSION = 786;

    public static boolean requestStoragePermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            return false;
        }else{
            return true;
        }
    }

    public static boolean wasAllGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
