package wovilon.pingpong1.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import wovilon.pingpong1.R;


public class LevelEditorView extends View {
    Canvas canvas;
    private Paint mPaint=new Paint();
    private Rect mRect = new Rect();
    private Bitmap mBitmap;
    public int move=650;
    int maxPointNumber=50;
    int pointsNumber=0;
    int[] pointsX=new int[maxPointNumber];
    int[] pointsY=new int[maxPointNumber];
    public Bitmap brickBitmap, button, buttonClicked;
    ArrayList<Point> brickList;
    Resources resources=getResources(); //get screen size and create boolean gameField
    DisplayMetrics displayMetrics=resources.getDisplayMetrics();
    public Point savePlayButtonXY, loadButtonXY;

    public LevelEditorView(Context context){
        super(context);

        mBitmap=BitmapFactory.decodeResource(resources,R.drawable.field4);
        mBitmap=Bitmap.createScaledBitmap(mBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels, true);
        brickBitmap=BitmapFactory.decodeResource(resources,R.drawable.brick);
        button=BitmapFactory.decodeResource(resources,R.drawable.editor_button);
        buttonClicked=BitmapFactory.decodeResource(resources,R.drawable.editor_button_clicked);

        loadButtonXY=new Point(displayMetrics.widthPixels/2-button.getWidth()/2,
                displayMetrics.heightPixels-200);
        savePlayButtonXY=new Point(displayMetrics.widthPixels/2-button.getWidth()/2,
                displayMetrics.heightPixels-300);

        brickList=new ArrayList<>();

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        this.canvas=canvas;

        canvas.drawBitmap(mBitmap,0,0, mPaint);
        canvas.drawBitmap(button,savePlayButtonXY.x,savePlayButtonXY.y,mPaint);
        canvas.drawBitmap(button,loadButtonXY.x,loadButtonXY.y,mPaint);
        mPaint.setTypeface(Typeface.create("Arial",Typeface.BOLD));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(32);
        canvas.drawText(resources.getString(R.string.SavePlay),
                savePlayButtonXY.x+button.getWidth()/2,
                savePlayButtonXY.y+button.getHeight()/2+resources.getDimension(R.dimen.dy),mPaint);
        canvas.drawText(resources.getString(R.string.Load),
                loadButtonXY.x+button.getWidth()/2,
                loadButtonXY.y+button.getHeight()/2+resources.getDimension(R.dimen.dy),mPaint);


        int i=0;
        while(i<brickList.size()){
            canvas.drawBitmap(brickBitmap,brickList.get(i).x,brickList.get(i).y, mPaint);
            i++;
        }
        canvas.restore();


    }

        public void drawBricks(ArrayList<Point> brickList){

            this.brickList=brickList;
            invalidate();

    }

    public void drawButtonClicked(){
        canvas.drawBitmap(buttonClicked,savePlayButtonXY.x,savePlayButtonXY.y,mPaint);
        canvas.drawBitmap(buttonClicked,loadButtonXY.x,loadButtonXY.y,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {



        return super.onTouchEvent(event);
    }
}
