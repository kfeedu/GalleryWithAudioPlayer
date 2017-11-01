package pl.kfeed.gallerywithmusicplayer.data.local.filter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

/**
 * Created by Kfeed on 27.10.2017.
 */

@Singleton
public class FilterHelper {

    private static final String TAG = FilterHelper.class.getSimpleName();

    @Inject
    public FilterHelper() {
    }

    private static final int GROWING_CIRCLES_PARAMETER = 10;
    private static final int WEIRD_CIRCLES_RADIUS = 50;
    private static final int ROTATED_CHECKER_SIZE = 50;

    public Single<Bitmap> generateGrowingCircles(Bitmap bitmap) {
        return Single.fromCallable(() -> GrowingCircles.onBitmap(bitmap)
                .setParameter(GROWING_CIRCLES_PARAMETER)
                .makeGrowingCircles());
    }

    public Single<Bitmap> generateWeirdCircles(Bitmap bitmap) {
        return Single.fromCallable(() -> WeirdCircles.onBitmap(bitmap)
                .setRadius(WEIRD_CIRCLES_RADIUS)
                .makeWeirdCircles());
    }

    public Single<Bitmap> generateRotatedChecker(Bitmap bitmap) {
        return Single.fromCallable(() -> RotatedChecker.onBitmap(bitmap)
                .setSquareSize(ROTATED_CHECKER_SIZE)
                .makeRotatedChecker());
    }

    public Bitmap decodeSampledBitmapFromResource(String photoPath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        options.inMutable = true;
        return BitmapFactory.decodeFile(photoPath, options);
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.d(TAG, "Sampling size.. reqWidth=" + reqWidth
                + " reqHeight=" + reqHeight + " sampleSize=" + inSampleSize);
        return inSampleSize;
    }
}
