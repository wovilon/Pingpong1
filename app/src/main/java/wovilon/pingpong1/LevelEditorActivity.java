package wovilon.pingpong1;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import java.util.ArrayList;

import wovilon.pingpong1.model.LevelEditorView;

public class LevelEditorActivity extends AppCompatActivity {
LevelEditorView levelEditorView;
    ArrayList<Point> bricksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        levelEditorView =new LevelEditorView(this);
        setContentView(levelEditorView);

        bricksList=new ArrayList<Point>();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction()==MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            bricksList.add(new Point((int)x,(int)y));
            levelEditorView.drawBricks(bricksList);
        }

        return super.onTouchEvent(event);
    }
}
