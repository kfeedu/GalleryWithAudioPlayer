package pl.kfeed.gallerywithmusicplayer.data.local.filter;

import android.graphics.Bitmap;
import android.graphics.Paint;

/**
 * Created by Kfeed on 27.10.2017.
 */

public class GrowingCircles extends BaseFilter {

    private int parameter;

    private GrowingCircles(Bitmap image){
        super(image);
    }

    public static GrowingCircles onBitmap(Bitmap bitmap) {
        return new GrowingCircles(bitmap);
    }


    public GrowingCircles setParameter(int parameter) {
        this.parameter = parameter;
        return this;
    }

    public Bitmap makeGrowingCircles() {
        for (int x = 0; x < x_res; x++) {
            for (int y = 0; y < y_res; y++) {
                double distance = calculateDistanceFromCenter(x, y);
                int circleId = 0;
                int sum = 0;
                while (distance > sum) {
                    sum += 2 + sum / parameter;
                    circleId++;
                }
                if (circleId % 2 == 0) {
                    image.setPixel(x, y, BLACK_COLOR);
                }
            }
        }
        return image;
    }
}
