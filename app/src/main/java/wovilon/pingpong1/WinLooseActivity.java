package wovilon.pingpong1;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import wovilon.pingpong1.db.DbUpdater;
import wovilon.pingpong1.model.Level;

import static android.R.color.holo_orange_dark;

public class WinLooseActivity extends AppCompatActivity {
    Level level=new Level();
    TextView highSscoreTextView;
    TextView scoreTextView;
    TextView winLoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_loose);
        boolean win=getIntent().getExtras().getBoolean("winloose");
        int score=getIntent().getExtras().getInt("score");
        level.setType(getIntent().getExtras().getString("LevelType"));
        level.setLevelNumber(getIntent().getExtras().getInt("LevelNumber"));
        highSscoreTextView=(TextView)findViewById(R.id.HighScore);
        scoreTextView=(TextView)findViewById(R.id.Score);
        winLoose=(TextView)findViewById(R.id.WinLoose);

        //set font
        Typeface font=Typeface.createFromAsset(getAssets(), "fonts/alienleaguebold.ttf");
        Button button=(Button)findViewById(R.id.button1);
        button.setTypeface(font);
        button.setAllCaps(true);
        button.setTextSize(32);//TODO move to style

        button=(Button)findViewById(R.id.button2);
        button.setTypeface(font);
        button.setAllCaps(true);
        button.setTextSize(32);

        TextView textView=highSscoreTextView;
        textView.setTypeface(font);
        textView.setAllCaps(true);
        textView.setTextSize(20);

        textView=scoreTextView;
        textView.setTypeface(font);
        textView.setAllCaps(true);
        textView.setTextSize(20);

        textView=winLoose;
        textView.setTypeface(font);
        textView.setAllCaps(true);
        textView.setTextSize(48);


        if (win) {
            winLoose.setText(R.string.YouWin);
            winLoose.setTextColor(getResources().getColor(R.color.text_color));

        }
        else {
            winLoose.setText(R.string.YouLoose);
            winLoose.setTextColor(getResources().getColor(R.color.text_color));
        }

        //set score textView text
        scoreTextView.setText(getString(R.string.Score)+": "+score);

        //high score showing and add to db, if needed
        DbUpdater dbUpdater = new DbUpdater(this, level.getType());

        int highScore = dbUpdater.getHighScore(level.getType(), level.getLevelNumber());
        if (highScore < score) {
            dbUpdater.setHighScore(level.getType(), level.getLevelNumber(), score);

        }

        highSscoreTextView.setText(getString(R.string.High_score) + ": " + highScore);
    }

    public void onPlayAgain(View view) {
        Intent intent=new Intent(WinLooseActivity.this, GameActivity.class);
        intent.putExtra("LevelType", level.getType());
        intent.putExtra("LevelNumber", level.getLevelNumber());
        startActivity(intent);
        finish();

    }

    public void onMainMenu(View view) {
        Intent intent=new Intent(WinLooseActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
