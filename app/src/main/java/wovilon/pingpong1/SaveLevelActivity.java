package wovilon.pingpong1;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import wovilon.pingpong1.db.DbUpdater;
import wovilon.pingpong1.model.Level;

public class SaveLevelActivity extends AppCompatActivity {
    //public final static String name="MyLevel"; //by default
    Button buttonSave;
    Button buttonPlay;
    EditText editText;
    Level level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_level);

        buttonSave=(Button)findViewById(R.id.buttonSave);
        buttonPlay=(Button)findViewById(R.id.buttonPlay);
        editText=(EditText)findViewById(R.id.saveLevelEeditText);

        Typeface font=Typeface.createFromAsset(getAssets(), "fonts/alienleaguebold.ttf");
        buttonSave.setTypeface(font);
        buttonSave.setAllCaps(true);
        buttonSave.setTextSize(32);//TODO move to style

        buttonPlay.setTypeface(font);
        buttonPlay.setAllCaps(true);
        buttonPlay.setTextSize(32);//TODO move to style



    }

    public void onButtonSaveClick(View view) {
        String levelName=editText.getText().toString();
        Intent levelNameIntent=new Intent();
        levelNameIntent.putExtra("LEVELNAME", levelName);
        setResult(RESULT_OK, levelNameIntent);
        finish();
    }

    public void onBtPlayClick(View view) {
        /*Intent intent=new Intent(LevelEditorActivity.this, GameActivity.class);
        intent.putExtra("LevelType", level.getType());
        intent.putExtra("LevelNumber", dbUpdater.getCount());
        startActivity(intent);*/

    }
}
