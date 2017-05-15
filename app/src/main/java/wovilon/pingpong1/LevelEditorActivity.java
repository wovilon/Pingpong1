package wovilon.pingpong1;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.util.ArrayList;

import wovilon.pingpong1.db.DbUpdater;
import wovilon.pingpong1.model.Level;
import wovilon.pingpong1.model.LevelEditorView;

public class LevelEditorActivity extends AppCompatActivity {
LevelEditorView levelEditorView;
    ArrayList<Point> bricksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        levelEditorView =new LevelEditorView(this);
        setContentView(levelEditorView);

        bricksList=new ArrayList<>();


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if (event.getAction()==MotionEvent.ACTION_DOWN){
            levelEditorView.drawButtonClicked();

        }else */if (event.getAction()==MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Resources resources = getResources(); //get screen size and create boolean gameField
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();

            //if button pressed (calculated from coordinates)
            if (x > levelEditorView.savePlayButtonXY.x
                    & x < levelEditorView.savePlayButtonXY.x + levelEditorView.button.getWidth()
                    & y > levelEditorView.savePlayButtonXY.y + resources.getDimension(R.dimen.dy)
                    & y < levelEditorView.savePlayButtonXY.y + levelEditorView.button.getHeight()
                    + resources.getDimension(R.dimen.dy) * 2) {

                Level level=new Level();
                level.setBricks(bricksList);
                DbUpdater dbUpdater=new DbUpdater(this);
                dbUpdater.addLevel(level);
                MusicPlayer musicPlayer=new MusicPlayer();
                musicPlayer.onstop();
                Intent intent=new Intent(LevelEditorActivity.this, GameActivity.class);
                intent.putExtra("LevelType", "UserLevels");
                intent.putExtra("LevelNumber", dbUpdater.getCount()-1);
                startActivity(intent);

            } else if (x > levelEditorView.loadButtonXY.x
                    & x < levelEditorView.loadButtonXY.x + levelEditorView.button.getWidth()
                    & y > levelEditorView.loadButtonXY.y + resources.getDimension(R.dimen.dy)
                    & y < levelEditorView.loadButtonXY.y + levelEditorView.button.getHeight()
                    + resources.getDimension(R.dimen.dy) * 2) {


            } else if (x < levelEditorView.savePlayButtonXY.y) { //else create or delete brick
                //calculating start point of bricks x0
                int brickWidth = BitmapFactory.decodeResource(resources, R.drawable.brick).getWidth();
                int brickHeight = BitmapFactory.decodeResource(resources, R.drawable.brick).getHeight();
                int x0 = displayMetrics.widthPixels / 2 - (displayMetrics.widthPixels / 2 / brickWidth) * brickWidth;

                x = ((x - x0) / brickWidth) * brickWidth + x0 + 5; //5 is empiric calibrating value
                y = (y / brickHeight) * brickHeight - 15;       //10 is empiric calibrating value

                //decide, to add or remove brick, if exists
                boolean brickExists = false;
                for (int i = 0; i < bricksList.size(); i++) {
                    if (bricksList.get(i).x == x & bricksList.get(i).y == y) {
                        brickExists = true;
                        bricksList.remove(i);
                    }
                }
                if (!brickExists) bricksList.add(new Point(x, y));

                levelEditorView.drawBricks(bricksList);
            }
        }
        return super.onTouchEvent(event);
    }
}
