package wovilon.pingpong1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import wovilon.pingpong1.db.DbUpdater;
import wovilon.pingpong1.model.Level;

public class WinLooseActivity extends AppCompatActivity {
    Level level=new Level();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_loose);
        boolean win=getIntent().getExtras().getBoolean("winloose");
        int score=getIntent().getExtras().getInt("score");
        level.setType(getIntent().getExtras().getString("LevelType"));
        level.setLevelNumber(getIntent().getExtras().getInt("LevelNumber"));


        TextView winLoose=(TextView)findViewById(R.id.WinLoose);
        if (win) {
            winLoose.setText(R.string.YouWin);
            winLoose.setTextColor(getResources().getColor(R.color.GreenCol));

        }
        else {
            winLoose.setText(R.string.YouLoose);
            winLoose.setTextColor(getResources().getColor(R.color.RedCol));
        }

        //set score textView text
        TextView highSscoreTextView=(TextView)findViewById(R.id.HighScore);
        TextView scoreTextView=(TextView)findViewById(R.id.Score);
        scoreTextView.setText(getString(R.string.Score)+": "+score);

        //high score showing and add to db, if needed
        DbUpdater dbUpdater = new DbUpdater(this, level.getType());
        dbUpdater.setHighScore(level.getType(), level.getLevelNumber(), score);
        int highScore = dbUpdater.getHighScore(level.getType(), level.getLevelNumber());
        /*if (highScore < score) {
            dbUpdater.setHighScore(level.getType(), level.getLevelNumber(), score);

        }*/

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
    }
}
