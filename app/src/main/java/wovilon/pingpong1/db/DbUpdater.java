package wovilon.pingpong1.db;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wovilon.pingpong1.R;
import wovilon.pingpong1.model.Level;

public class DbUpdater {
    private SQLiteDatabase db;
    private Cursor c;
    private Context context;
    private String table;

    public DbUpdater(Context context, String table){
        this.context=context;
        this.table=table;
        DbHelper dbHelper=new DbHelper(context);
        db=dbHelper.getWritableDatabase();
        c=db.rawQuery("SELECT * FROM " + table, null);
    }

    public void addLevel(Level level){
        int id=0;
        try{
            id=c.getCount();
        }catch (NullPointerException ne){
            Log.d("MyLOG","NullPointer Exception in DbUpdater");}

        //form JSON
        JSONObject levelJSON=new JSONObject();
        JSONArray bricksX=new JSONArray();
        JSONArray bricksY=new JSONArray();
        for (int i=0; i<level.getBricks().size();i++){
            bricksX.put(level.getBricks().get(i).x);
            bricksY.put(level.getBricks().get(i).y);
        }
        try {
            levelJSON.put("bricksX", bricksX);
            levelJSON.put("bricksY", bricksY);
        }catch (JSONException je){}


        ContentValues data=new ContentValues();
        data.put("id",id);
        data.put("name", "MyLevel_"+id);
        data.put("bricksJSON", levelJSON.toString());
        data.put("highScore",0);
        db.insert("UserLevels", null, data);
        data.clear();
    }

    public Level getLevelFromDb(int i){
        Level level=new Level();
        ArrayList<Point> bricks=new ArrayList<>();
        c.moveToPosition(i);
        try{
            JSONObject bricksJSON=new JSONObject(c.getString(c.getColumnIndex("bricksJSON")));
            JSONArray JSONBricksX= bricksJSON.getJSONArray("bricksX");
            JSONArray JSONBricksY= bricksJSON.getJSONArray("bricksY");

            for (int k=0; k<JSONBricksX.length(); k++){
                bricks.add(new Point(JSONBricksX.getInt(k), JSONBricksY.getInt(k)));
            }
        }catch (JSONException je){}

        level.setBricks(bricks);
        level.setType(table);
        level.setLevelNumber(i);
        level.setName(c.getString(c.getColumnIndex("name")));

        return level;
    }

    public void setHighScore(String levelType,int id,int highScore){
        c.moveToPosition(id);

        ContentValues data=new ContentValues();
        data.put("highScore",highScore);
        db.update(levelType, data, "id="+id, null);
        data.clear();


        Log.d("MyLOG", getHighScore(levelType, id)+"");
        int g=8;
    }

    public int getHighScore(String levelType,int id){
        int highScore;
        c.moveToPosition(id);
        highScore=c.getInt(c.getColumnIndex("highScore"));

        return highScore;
    }


    public int getCount(){
        return c.getCount();
    }




        // to add main levels to the game
    public void addMainLevels(){
        Cursor c=db.rawQuery("SELECT * FROM " + "MainLevels", null);
        Level level;
        level=new Level();
        level.setBricks(Level_1());

        int id=0;
        try{
            id=c.getCount();
        }catch (NullPointerException ne){
            Log.d("MyLOG","NullPointer Exception in DbUpdater");}

        //form JSON
        JSONObject levelJSON=new JSONObject();
        JSONArray bricksX=new JSONArray();
        JSONArray bricksY=new JSONArray();
        for (int i=0; i<level.getBricks().size();i++){
            bricksX.put(level.getBricks().get(i).x);
            bricksY.put(level.getBricks().get(i).y);
        }
        try {
            levelJSON.put("bricksX", bricksX);
            levelJSON.put("bricksY", bricksY);
        }catch (JSONException je){}


        ContentValues data=new ContentValues();
        data.put("id",id);
        data.put("name", "MyLevel_"+id);
        data.put("bricksJSON", levelJSON.toString());
        data.put("highScore",0);
        db.insert("MainLevels", null, data);
        data.clear();
    }

    private int[][] Level_1(){
        Resources resources = context.getResources();
        Bitmap brick=BitmapFactory.decodeResource(resources, R.drawable.brick);
        int Y=260, w= brick.getWidth(), h=brick.getHeight();
        int X=resources.getDisplayMetrics().widthPixels/2-5*w;//calculate start X, so that bricks are symetric on screen
        int [][] brickPoints = new int[22][2];

        brickPoints[0][0]=X; brickPoints[0][1]=Y;
        brickPoints[1][0]=X+ w; brickPoints[1][1]=Y;
        brickPoints[2][0]=X+w*2; brickPoints[2][1]=Y;
        brickPoints[3][0]=X+w*3; brickPoints[3][1]=Y;
        brickPoints[4][0]=X+w*4; brickPoints[4][1]=Y;
        brickPoints[5][0]=X+w*5; brickPoints[5][1]=Y;
        brickPoints[6][0]=X+w*6; brickPoints[6][1]=Y;
        brickPoints[7][0]=X+w*7; brickPoints[7][1]=Y;
        brickPoints[8][0]=X+w*8; brickPoints[8][1]=Y;
        brickPoints[9][0]=X+w*9; brickPoints[9][1]=Y;

        brickPoints[10][0]=X+w*2; brickPoints[10][1]=Y+ h;
        brickPoints[11][0]=X+w*3; brickPoints[11][1]=Y+ h;
        brickPoints[12][0]=X+w*4; brickPoints[12][1]=Y+ h;
        brickPoints[13][0]=X+w*5; brickPoints[13][1]=Y+ h;
        brickPoints[14][0]=X+w*6; brickPoints[14][1]=Y+ h;
        brickPoints[15][0]=X+w*7; brickPoints[15][1]=Y+ h;

        brickPoints[16][0]=X+w*3; brickPoints[16][1]=Y+h*2;
        brickPoints[17][0]=X+w*4; brickPoints[17][1]=Y+h*2;
        brickPoints[18][0]=X+w*5; brickPoints[18][1]=Y+h*2;
        brickPoints[19][0]=X+w*6; brickPoints[19][1]=Y+h*2;

        brickPoints[20][0]=X+w*4; brickPoints[20][1]=Y+h*3;
        brickPoints[21][0]=X+w*5; brickPoints[21][1]=Y+h*3;

        return brickPoints;
    }

}
