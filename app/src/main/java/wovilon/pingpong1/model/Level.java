package wovilon.pingpong1.model;

import android.graphics.Point;

import java.util.ArrayList;


public class Level {
    private ArrayList<Point> bricks;
    private String name;
    private String type;
    private int levelNumber;

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getHighScore() {
        return highScore;
    }

    private int highScore;

    public void setType(String type) {
        this.type = type;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getType() {

        return type;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public Level(){
        bricks=new ArrayList<>();
    }

    public void setBricks(ArrayList<Point> bricks) {
        this.bricks = bricks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Point> getBricks() {
            return bricks;
    }

    public String getName() {
        return name;
    }
}
