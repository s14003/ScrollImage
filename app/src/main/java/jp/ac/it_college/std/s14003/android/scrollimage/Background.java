package jp.ac.it_college.std.s14003.android.scrollimage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


public class Background {
    private Paint paint  = new Paint();

    public final Bitmap mbitmap;
    public final Rect rect;

    public Background(Bitmap bitmap, int width, int height) {
        this.mbitmap = bitmap;

        int left = (width - bitmap.getWidth()) / 2;
        int top = (height - bitmap.getHeight()) / 2;
        int right = left + bitmap.getWidth();
        int bottom = top + bitmap.getHeight();
        rect = new Rect(left, top, right, bottom);
    }
    public void draw (Canvas canvas) {
        canvas.drawBitmap(mbitmap, rect.left, rect.top, paint);
    }
}
