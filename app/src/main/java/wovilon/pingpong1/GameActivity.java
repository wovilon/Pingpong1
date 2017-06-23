package wovilon.pingpong1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import wovilon.pingpong1.db.DbUpdater;
import wovilon.pingpong1.gameObjects.ScoreImage;
import wovilon.pingpong1.model.Level;

import static android.content.Context.SENSOR_SERVICE;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class GameActivity extends Activity implements SensorEventListener {
    GameView gameView;
    Sensor mySensor;
    SensorManager SM;
    public double phoneRotation;
    Level level;

    void rotTranslate(float dat){this.phoneRotation=dat; //if (this.phoneRotation>2) System.exit(0);
        }

    @Override
    public void onSensorChanged(SensorEvent event) {
        rotTranslate(event.values[0]);
            }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        level=new Level();
        Intent intent=getIntent();
        level.setType(intent.getExtras().getString("LevelType"));
        level.setLevelNumber(intent.getExtras().getInt("LevelNumber"));

        gameView=new GameView(this,this.phoneRotation, level);
        setContentView(gameView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SM=(SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor=SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            MusicPlayer.onstop();
            gameView.getGameManager().setRunning(false);
            finish();
        }
        return true;
    }



    class GameView extends SurfaceView implements SurfaceHolder.Callback{
        Context context;
        double phoneRotation;
        private SurfaceHolder mSurfaceHolder;
        private GameManager gameManager;
        Level level;

        public GameView (Context context,double phoneRotation, Level level){
            super(context); this.context=context; this.phoneRotation=phoneRotation;
            mSurfaceHolder=getHolder();
            mSurfaceHolder.addCallback(this);
            this.level=level;

        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            gameManager = new GameManager(getHolder(),context,this.phoneRotation, level);
            gameManager.setRunning(true);
            gameManager.start();

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        }

        public GameManager getGameManager(){
            return gameManager;
        }
    }
}

class GameManager extends Thread {

    public boolean running = false;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private double phoneRotation;
    private int displayHeight, displayWidth;
    private boolean [][] gameField;
    private int defeatedBricks=0;
    private Level level;
    private int score=0;


    GameManager(SurfaceHolder surfaceHolder, Context context, double phoneRotation, Level currentLevel) {
        this.surfaceHolder = surfaceHolder;
        this.context=context;
        this.phoneRotation=phoneRotation;
        this.level=currentLevel;
        this.score=0;

        DbUpdater dbUpdater=new DbUpdater(context, level.getType());
        level.setBricks(dbUpdater.getLevelFromDb(level.getLevelNumber()).getBricks());

        Resources resources=context.getResources(); //get screen size and create boolean gameField
        DisplayMetrics displayMetrics=resources.getDisplayMetrics();
        this.displayWidth=displayMetrics.widthPixels;
        this.displayHeight=displayMetrics.heightPixels;
        this.gameField=new boolean [this.displayWidth] [this.displayHeight];
        for (int i=0;i<this.displayWidth;i++){
            for (int j=0;j<this.displayHeight;j++){gameField[i] [j]=false;} }

        //old code for field borders
        /*for (int i=0;i<border;i++){
            for (int j=0;j<this.displayHeight;j++){gameField[i] [j]=true;} }
        for (int i=this.displayWidth-border;i<this.displayWidth;i++){
            for (int j=0;j<this.displayHeight;j++){gameField[i] [j]=true;} }
        for (int i=0;i<this.displayWidth;i++){
            for (int j=0;j<border;j++){gameField[i] [j]=true;} }*/

        //old code for bottom field border
        /*for (int i=0;i<this.displayWidth;i++){
            for (int j=this.displayHeight-border-40;j<this.displayHeight;j++){gameField[i] [j]=true;} }*/

    }

    void setRunning(boolean running) {
        this.running = running;
            }

    @Override
    public void run() {
        Canvas canvas = surfaceHolder.lockCanvas(null);
        BackGround backGround=new BackGround(canvas);
        Ball ball=new Ball(canvas,context);
        Pad pad=new Pad(canvas,context,this.displayWidth,this.displayHeight,this.phoneRotation);
        Brick [] bricks=new Brick[this.createBricks().length];

        int iteration=0;
        Paint mPaint=new Paint();
        ScoreImage scoreImage=new ScoreImage(canvas,context,mPaint);
        //boolean touched=false, touched1=false;
        double oldAlfa;
        for (int i=0;i<this.createBricks().length;i++){
            bricks[i]=new Brick(canvas,context,gameField);  }
        Intent intent=new Intent(context, WinLooseActivity.class);
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        MusicPlayer.oncreate(context,"levelFon");
        float vol=(float)settings.getInt("GenSnd",50) * settings.getInt("MusSnd",100);  vol/=100*100;
        MusicPlayer.volume(vol,vol);
        MusicPlayer.onstart();

        surfaceHolder.unlockCanvasAndPost(canvas);
        while (running) {
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas == null)
                    continue;

                if (backGround.bitmap.getWidth()-displayWidth>iteration/2) backGround.draw(-iteration/2,0);
                else backGround.draw(-(backGround.bitmap.getWidth()-displayWidth),0);

                for (int i=0;i<this.createBricks().length;i++){
                    if (bricks[i].state)
                    bricks[i].draw(this.createBricks()[i][0],createBricks()[i][1]);
                }

                ball.update();
                ball.draw();

                pad.update();
                pad.draw();

                oldAlfa=ball.alfa;

                ball.alfa=collisionFinder(ball,bricks);

                pad.undraw();
                scoreImage.draw(score);

                if(ball.y>pad.y+pad.bitmap.getHeight()){
                    intent.putExtra("winloose", false);
                    intent.putExtra("score", score);
                    intent.putExtra("LevelType", level.getType());
                    intent.putExtra("LevelNumber", level.getLevelNumber());
                    context.startActivity(intent);
                    MusicPlayer.onstop();
                    running=false; //to stop the thread
                }

                iteration++;
                sleep(10);

            }catch(Exception e) {}
            finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

    }
         private double collisionFinder(Ball ball, Brick[] bricks){
         double alfa=ball.alfa;
         int mmax=0; //numder of interferred pixels
         double xsr=0,ysr=0; //middle point of interferred pixels
         double normal=ball.alfa-Math.toRadians(90); //angle of normal to surface
         int intx=(int)ball.x; int inty=(int)ball.y; //ball coordinates
         Point intpix=new Point(-1,-1); //one of all interferred pixels to find object of collision
             String collisionObject="pad"; //pad by default


                //collision with walls, then put normal directly
             if (ball.x<0) {normal=Math.toRadians(0); collisionObject="wall";}
             else if (ball.x>displayWidth-ball.bitmap.getWidth()) {normal=Math.toRadians(180); collisionObject="wall";}
             else if (ball.y<0) {normal=Math.toRadians(90); collisionObject="wall";}
             else {
                 //else find interference pixels
                 for (int n = 0; n < ball.pix.length; n++) {
                     if (this.gameField[(int) ball.x + ball.pix[n][0]][(int) ball.y + ball.pix[n][1]]) {
                         mmax++;
                         intpix.x = (int) ball.x + ball.pix[n][0];
                         intpix.y = (int) ball.y + ball.pix[n][1];
                     }
                 }
             }

            //if interferred pixels number >0, than calculating normal
         if (mmax>0) {
             SharedPreferences settings = context.getSharedPreferences("Settings", 0);
             MediaPlayer touchSound=MediaPlayer.create(context,R.raw.click6);
             float vol=(float)settings.getInt("GenSnd",50) * settings.getInt("EffSnd",100);  vol/=100*100;
             touchSound.setVolume(vol,vol);
             touchSound.start();

             for (int n=0;n<ball.pix.length;n++) {
                 if (gameField[intx+ball.pix[n][0]][inty+ball.pix[n][1]]) {
                     xsr+=intx+ball.pix[n][0];
                     ysr+=inty+ball.pix[n][1]; }
             }
             xsr=xsr/mmax+1; ysr=ysr/mmax+1;
             //normal=Math.atan((ball.y+ball.bitmap.getHeight()/2-ysr) / (ball.x+ball.bitmap.getWidth()/2-xsr));
             normal=Math.atan2((ball.y+ball.bitmap.getHeight()/2-ysr),(ball.x+ball.bitmap.getWidth()/2-xsr));

             if (normal<0) normal=Math.toRadians(360)+normal;
             normal=normal%Math.toRadians(360);
         }
            //calculating new alfa
             double alfaBuffer = (2 * normal - (alfa - Math.toRadians(180)))%Math.toRadians(360);
             if (alfaBuffer<0) alfaBuffer=Math.toRadians(360)+alfaBuffer;

             if (Math.abs(normal - alfaBuffer)<Math.toRadians(90)|
                     Math.abs(normal - alfaBuffer)>Math.toRadians(270) ) {
                 alfa = alfaBuffer;
             }

         if (intpix.x!=(-1)){
             for (int i=0;i<bricks.length;i++){
                 if (intpix.x>=bricks[i].x & intpix.x<=bricks[i].x+bricks[i].bitmap.getWidth()
                   & intpix.y>=bricks[i].y & intpix.y<=bricks[i].y+bricks[i].bitmap.getHeight()) {
                     bricks[i].state=false;
                     bricks[i].delete();
                     this.defeatedBricks+=1;
                     collisionObject="brick";
                 }
             }
             if (this.defeatedBricks>=bricks.length) {
                 running = false; //to stop the thread
                 Intent intent=new Intent(context, WinLooseActivity.class);
                 intent.putExtra("winloose",true);
                 intent.putExtra("score", score);
                 intent.putExtra("LevelType", level.getType());
                 intent.putExtra("LevelNumber", level.getLevelNumber());
                 context.startActivity(intent);
                 MusicPlayer.onstop();
                 }
         }

             if (collisionObject.equals("brick")) score += 10;
             else if (collisionObject.equals("pad")) score -= 0;
             else if (collisionObject.equals("wall")) score -= 1;
             if (score < 0) score = 0;

         return alfa;
    }

    private int[][] createBricks(){

        int[][] brickPoints; //TODO no necessity to convert list to array, try to use array everywhere


            try {
                brickPoints = new int[level.getBricks().size()][2];
                for (int i = 0; i < level.getBricks().size(); i++) {
                    brickPoints[i][0] = level.getBricks().get(i).x;
                    brickPoints[i][1] = level.getBricks().get(i).y;
                }

            } catch (NegativeArraySizeException nae) {brickPoints = new int[0][2];
            }


        return brickPoints;
    }


    private class Ball {
        Bitmap bitmap;
        Canvas canv;
        Paint mPaint;
        double x=300,y=900;
        double alfa=Math.toRadians(-90);
        double velocity=context.getSharedPreferences("Settings", 0).getInt("BallVelocity", 20)+5;
        int [] [] pix;

        Ball(Canvas canvas, Context context) {
            canv=canvas;
            Resources resources=context.getResources();
            bitmap= BitmapFactory.decodeResource(resources,R.drawable.ball_1);
            x=resources.getDisplayMetrics().widthPixels/2-bitmap.getWidth()/2;
            y=resources.getDisplayMetrics().heightPixels*0.8;

            int n=0,nmax=0; //get ball pixels
            for (int i=0;i<bitmap.getWidth();i++) {
                for (int j=0;j<bitmap.getHeight();j++) {
                    if (bitmap.getPixel(i,j)!=0) nmax++;
                }
            }
            pix=new int[nmax] [2];
            for (int i=0;i<bitmap.getWidth();i++) {
                for (int j=0;j<bitmap.getHeight();j++) {
                    if (bitmap.getPixel(i,j)!=0) {pix[n][0]=i; pix[n][1]=j; n++;}
                }
            }
        }
        void draw(){
            canv.drawBitmap(bitmap,(int)Math.round(x),(int)Math.round(y),mPaint);
        }
        void update(){
            this.x+=velocity*cos(alfa);
            this.y+=velocity*sin(alfa);
        }
    }

    private class Brick{
        Bitmap bitmap;
        Canvas canv;
        Context context;
        Paint mPaint;
        int x,y;
        boolean state=true; //true - object is visible, false - non visible
        boolean[][] gameField;
      Brick(Canvas canvas, Context context, boolean[][] gameField){
            canv=canvas; this.context=context; this.gameField=gameField;
            Resources resources=context.getResources();
            bitmap= BitmapFactory.decodeResource(resources,R.drawable.rock_brick);
    }

        void draw(int x, int y){
            canv.drawBitmap(bitmap,x,y,mPaint);
            this.x=x; this.y=y;
            for (int i=0;i<bitmap.getWidth();i++) {
                for (int j=0;j<bitmap.getHeight();j++)
                    gameField[x+i][y+j] = true;
            }}
        void delete(){
            for (int i=0;i<bitmap.getWidth();i++) {
                for (int j=0;j<bitmap.getHeight();j++)
                    gameField[x+i][y+j] = false;
            }
        }

          }

    private class Pad {
        Bitmap bitmap;
        Canvas canv;
        Context context;
        Paint mPaint;
        double x=300,y;
        double velocity=5;
        double phoneRotation;
        int [] [] pix;
        Sens sens;
        private int timeIteration=0;
        String turbulenceMode;

        Pad(Canvas canvas, Context context, int displayWidth, int displayHeight, double phoneRotation) {
            canv=canvas; this.context=context; this.phoneRotation=phoneRotation;
            Resources resources=context.getResources();
            bitmap= BitmapFactory.decodeResource(resources,R.drawable.pad_cosmonavt);
            this.sens=new Sens(context);
            velocity=(double)context.getSharedPreferences("Settings", 0)
                    .getInt("BallVelocity", 20)/5+5;//depends on ball velocity
            turbulenceMode=context.getSharedPreferences("Settings", 0)
                    .getString("TurbulenceMode", "mode_classic");


            int n=0,nmax=0; //get ball pixels
            for (int i=0;i<bitmap.getWidth();i++) {
                for (int j=0;j<bitmap.getHeight();j++) {
                    if (bitmap.getPixel(i,j)!=0) nmax++;
                }
            }
            pix=new int[nmax] [2];
            for (int i=0;i<bitmap.getWidth();i++) {
                for (int j=0;j<bitmap.getHeight();j++) {
                    if (bitmap.getPixel(i,j)!=0) {pix[n][0]=i; pix[n][1]=j; n++;}
                }
            }
            this.y=displayHeight-this.bitmap.getHeight()-50;
            this.x=displayWidth/2-this.bitmap.getWidth()/2;
            }
        void draw(){
            canv.drawBitmap(bitmap,(int)Math.round(x),(int)Math.round(y),mPaint);
            for (int i=0;i<pix.length;i++)
                    gameField[(int)this.x+pix[i][0]][(int)this.y+pix[i][1]] = true;
        }
        void undraw(){
            for (int i=0;i<bitmap.getWidth();i++) {
                for (int j=0;j<bitmap.getHeight();j++)
                    gameField[(int)this.x+i][(int)this.y+j] = false;
            }}
        void update(){
            //add turbulence
            double turbX;
            switch (turbulenceMode) {
                case "mode_classic": turbX = 0; break;
                case "mode_drunk": turbX = 2 * Math.sin((double) timeIteration / 3)
                                            +5 * Math.sin((double) timeIteration / 20); break;
                case "mode_ship": turbX = 5 * Math.sin((double) timeIteration / 20); break;
                default:turbX = 0; break;
            }
            timeIteration++;

            //pad motion between borders
            if (x>10&x<displayWidth-bitmap.getWidth()-10) x-=sens.ph*velocity+turbX;
            else {if(x<=10&sens.ph<=0)x-=sens.ph*5+turbX;
                if (x>=displayWidth-bitmap.getWidth()-10&sens.ph>=0)x-=sens.ph*velocity+turbX;}
            //if pad is outside borders
            if (x<10) x=10;
            else if (x>displayWidth-bitmap.getWidth()-10) x=displayWidth-bitmap.getWidth()-10;

        }
    }

    private class BackGround{
        Bitmap bitmap,inputBitmap;
        Canvas canv;
        Paint mPaint;

        BackGround(Canvas canvas){
            canv=canvas;
            Resources resources=context.getResources();
            inputBitmap= BitmapFactory.decodeResource(resources,R.drawable.background_cosmos);
            double scale=displayHeight*1.0/inputBitmap.getHeight();
            bitmap = Bitmap.createScaledBitmap(inputBitmap,
                    (int)(inputBitmap.getWidth()*scale),
                    (int)(inputBitmap.getHeight()*scale), true);

            //bitmap = Bitmap.createScaledBitmap(inputBitmap, displayWidth, displayHeight, true);
        }

        void draw(int x, int y){
            canv.drawBitmap(bitmap,x,y,mPaint);
            }

    }


}
 class Sens implements SensorEventListener{
     float ph;

     Sens(Context context){
         Context context1 = context;
         SensorManager SM = (SensorManager) context1.getSystemService(SENSOR_SERVICE);
         Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
     private void ex(float d){this.ph=d; }
     @Override
     public void onSensorChanged(SensorEvent event) {
         //if (event.values[0]>2)System.exit(0);
         //this.ph=event.values[0];
         ex(event.values[0]);
     }

     @Override
     public void onAccuracyChanged(Sensor sensor, int i) {

     }
 }



