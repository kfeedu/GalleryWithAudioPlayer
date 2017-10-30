package pl.kfeed.gallerywithmusicplayer.data.local.filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Kfeed on 27.10.2017.
 */

public class BaseFilter {

    protected Bitmap image;
    protected int x_res;
    protected int y_res;

    public BaseFilter(Bitmap image) {
        this.image = image;
        x_res = image.getWidth();
        y_res = image.getHeight();
    }

    protected static int int2RGB(int red, int green, int blue) {
        // Make sure that color intensities are in 0..255 range
        red = red & 0x000000FF;
        green = green & 0x000000FF;
        blue = blue & 0x000000FF;

        // Assemble packed RGB using bit shift operations
        return (red << 16) + (green << 8) + blue;
    }

    protected double calculateDistanceBetweenPoints(int x, int y, int x2, int y2) {
        return Math.sqrt(Math.pow((x - x2), 2) + Math.pow((y - y2), 2));
    }

    protected double getGreyscaleLevel(double distance, double blurRadius) {
        return 127 * (Math.sin((Math.PI * distance / blurRadius)) + 1);
    }

    protected double calculateDistanceFromCenter(int x, int y) {
        return Math.sqrt(Math.pow((x - x_res / 2), 2) + Math.pow((y - y_res / 2), 2));
    }
}
