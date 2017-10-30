package pl.kfeed.gallerywithmusicplayer.data.local.filter;

import android.graphics.Bitmap;

public class WeirdCircles extends BaseFilter {

    private int radius;

    private WeirdCircles(Bitmap image){
        super(image);
        radius = 15;
    }

    public static WeirdCircles onBitmap(Bitmap image){
        return new WeirdCircles(image);
    }

    public WeirdCircles setRadius(int radius){
        this.radius = radius;
        return this;
    }

    public Bitmap makeWeirdCircles(){
        for(int x = 0; x < x_res; x++){
            for(int y = 0;y< y_res; y++){
                double distance = calculateDistanceBetweenPoints(x, y, x/radius*radius + radius/2, y/radius*radius + radius/2);
                int circleId = (int) distance / 5;

                if (circleId % 2 != 0) {
                    image.setPixel(x, y, int2RGB(0, 0, 0));
                }
            }
        }
        return image;
    }
}
