package wovilon.pingpong1;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer extends MediaPlayer {
    static MediaPlayer sound;
    static float musVolume, effVolume;

    public static void oncreate(Context context, String track){
        switch (track) {
            case "menuFon": sound = MediaPlayer.create(context, R.raw.menusound); break;
            case "levelFon": sound = MediaPlayer.create(context, R.raw.gamesound); break;
            case "ballTouch": sound = MediaPlayer.create(context, R.raw.click6); break;
        }}
    public static void volume(float left, float right) {sound.setVolume(left,right);
    }
    public static void onstart(){sound.start();}
    public static void onstop(){sound.stop();}
    public static void onpause(){sound.pause();}

}
