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
import android.util.DisplayMetrics;
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
    private Bitmap brickBitmap;
    ArrayList<Point> brickList;

    public LevelEditorView(Context context){
        super(context);

        Resources resources=context.getResources(); //get screen size and create boolean gameField
        DisplayMetrics displayMetrics=resources.getDisplayMetrics();
        Resources res=this.getResources();
        mBitmap=BitmapFactory.decodeResource(resources,R.drawable.field4);
        mBitmap=Bitmap.createScaledBitmap(mBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels, true);
        brickBitmap=BitmapFactory.decodeResource(resources,R.drawable.brick);
        brickList=new ArrayList<>();

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);


        canvas.drawBitmap(mBitmap,0,0, mPaint);
        canvas.drawBitmap(brickBitmap,100,100, mPaint);

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


}
