package wovilon.pingpong1.gameObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import wovilon.pingpong1.R;


public class ScoreImage {
    private int score=87;
    private Canvas canvas;
    private Context context;
    private Bitmap bitmap;
    private Paint mPaint;
    private Resources resources;

    public ScoreImage(Canvas canvas, Context context, Paint mPaint){
        this.canvas=canvas;
        this.context=context;
        this.mPaint=mPaint;
        this.score=score;

        resources=context.getResources();
        //bitmap = BitmapFactory.decodeResource(resources, R.drawable.ball);
    }

    public void draw(int score){
        //canvas.drawBitmap(bitmap, 10, 10, mPaint);
        mPaint.setColor(Color.YELLOW);
        mPaint.setTypeface(Typeface.create("ArialBlack",Typeface.BOLD));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(48);
        canvas.drawText(score+"", resources.getDisplayMetrics().widthPixels/2,
                40+resources.getDimension(R.dimen.dy), mPaint);
    }
}