package pl.kfeed.gallerywithmusicplayer.data.local.filter;

import android.graphics.Bitmap;

public class RotatedChecker extends BaseFilter {

    private int squareSize;

    private RotatedChecker(Bitmap image) {
        super(image);
    }

    public static RotatedChecker onBitmap(Bitmap image) {
        return new RotatedChecker(image);
    }


    public RotatedChecker setSquareSize(int squareSize) {
        this.squareSize = squareSize;
        return this;
    }

    public Bitmap makeRotatedChecker() {

        for (int x = 0; x < x_res; x++) {
            for (int y = 0; y < y_res; y++) {
                double rad = Math.toRadians(45);
                int x2 = (int) (x * Math.cos(rad) - (y + (squareSize * Math.sqrt(2) / 2)) * Math.sin(rad));
                int y2 = (int) (x * Math.sin(rad) + (y + (squareSize * Math.sqrt(2) / 2)) * Math.cos(rad));

                if ((y2 + x_res + y_res) % (squareSize * 2) < squareSize) {
                    if ((x2 + x_res + y_res) % (squareSize * 2) < squareSize)
                        image.setPixel(x, y, BLACK_COLOR);
                } else {
                    if (!((x2 + x_res + y_res) % (squareSize * 2) < squareSize))
                        image.setPixel(x, y, BLACK_COLOR);
                }
            }
        }
        return image;
    }
}