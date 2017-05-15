package wovilon.pingpong1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Levels_DB";
    private static final int DATABASE_VERSION = 1;

    DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_UserLevels =
                "CREATE TABLE UserLevels (id ID, name NAME, bricksJSON BRICKSJSON, highScore HIGHSCORE)";
        db.execSQL(CREATE_TABLE_UserLevels);
        /*String CREATE_TABLE_MainLevels =
                "CREATE TABLE MainLevels (id ID, name NAME, bricksJSON BRICKSJSON, highScore HIGHSCORE)";
        db.execSQL(CREATE_TABLE_MainLevels);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + "UserLevels");
        //db.execSQL("DROP TABLE IF EXISTS " + "MainLevels");
        onCreate(db);
    }
}
