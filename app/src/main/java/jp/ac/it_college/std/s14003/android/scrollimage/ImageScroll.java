package jp.ac.it_college.std.s14003.android.scrollimage;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ImageScroll extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    private final String TAG = "ImageScroll";
    private Bitmap mImage;
    private Bitmap inu;
    private SurfaceHolder mHolder;
    private Thread mLooper;
    private int mHeight; //画面の高さ
    private int mPositionTop = 0; //表示位置(Top:Y座標)
    private long mTime = 0; //1つ前の描画時刻
    private long mLapTime = 0; //画面上部から下部に到着するまでの時間
    private Background background;


    public ImageScroll(Context context) {
        super(context);
        getHolder().addCallback(this);
        mImage = BitmapFactory.decodeResource(getResources(),R.raw.background);
        inu = BitmapFactory.decodeResource(getResources(), R.raw.back);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Canvasの取得(マルチスレッド環境対応のためのLook)
        Canvas canvas = holder.lockCanvas();
        Paint paint  = new Paint();
        mHolder = holder;
        mLooper = new Thread(this);

        //描画処理(Look中なのでなるべく早く)
        canvas.drawBitmap(mImage, 0, 0, paint);

        canvas.drawBitmap(inu, 0, 0, paint);
        //Lookしたキャンバスを開放,他の描画処理スレッドがあればそちらに
        holder.unlockCanvasAndPost(canvas);
    }


    //SurfaceView変更時に呼び出される
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mLooper != null) {
            Log.d(TAG,"mLooper not null");
            mHeight = height;
            mTime = System.currentTimeMillis();
            mLooper.start();
        }
    }
    //SurfaceView破棄時に呼び出される

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mLooper = null;
    }

    public void run() {
        while (mLooper != null) {
            //描画処理
            doDraw();

            //位置情報更新
            //処理落ちによりスローモーションを避けるための現在時刻を取得
            long delta = System.currentTimeMillis() - mTime;
            mTime = System.currentTimeMillis();

            //次の描画位置
            //1秒間に100px動くとして
            int nextPosition = (int) ((delta / 1000.0) * 100);

            //描画範囲の設定
            if (mPositionTop + nextPosition < mHeight) {
                mPositionTop += nextPosition;
            } else {
                //画面の縦移動が終わるまでの時間計測(一定であることが期待値)
                Log.d(TAG, "mLapTime:" + (mTime - mLapTime));
                mLapTime = mTime;

                //位置の初期化
                mPositionTop = 0;
            }
        }
    }

    //描画関数
    private void doDraw() {
        //Canvasの取得(マルチスレッド環境対応のためのLook)
        Canvas canvas = mHolder.lockCanvas();
        Paint paint = new Paint();
        canvas.drawColor(Color.GRAY);

        int mPositionLeft = 0;
        if (background == null) {
            Log.d(TAG, "background is null");
            canvas.drawBitmap(mImage, mPositionLeft, mPositionTop, paint);
            canvas.drawBitmap(inu, mPositionLeft, mPositionTop -1080, paint);
            mHolder.unlockCanvasAndPost(canvas);
        }

    }
}
