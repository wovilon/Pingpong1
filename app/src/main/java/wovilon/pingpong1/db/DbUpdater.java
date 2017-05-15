package wovilon.pingpong1.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wovilon.pingpong1.model.Level;

public class DbUpdater {
    private SQLiteDatabase db;
    private Cursor c;
    private Context context;

    public DbUpdater(Context context){
        this.context=context;
        DbHelper dbHelper=new DbHelper(context);
        db=dbHelper.getWritableDatabase();
        c=db.rawQuery("SELECT * FROM UserLevels", null);
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

    public ArrayList<Point> getLevelBricksFromDb(int i){
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

        return bricks;
    }

    public void setHighScore(String levelType,int id,int highScore){
        c.moveToPosition(id);

        ContentValues data=new ContentValues();
        data.put("highScore",highScore);
        db.insert(levelType, null, data);
        data.clear();
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



}
