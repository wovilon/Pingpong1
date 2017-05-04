package wovilon.pingpong1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import static wovilon.pingpong1.MusicPlayer.sound;
import static wovilon.pingpong1.R.id.effectsSound;
import static wovilon.pingpong1.R.id.generalSound;
import static wovilon.pingpong1.R.id.musicSound;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    int generalSoundValue=50, musicSoundValue=50, effectsSoundValue=50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        SharedPreferences settings = getSharedPreferences("Settings", 0);
        generalSoundValue = settings.getInt("GenSnd", 0);
        musicSoundValue = settings.getInt("MusSnd", 0);
        effectsSoundValue = settings.getInt("EffSnd", 0);

        final SeekBar generalSnd=(SeekBar)findViewById(R.id.generalSound);
        final SeekBar musicSnd=(SeekBar)findViewById(musicSound);
        final SeekBar effectsSnd=(SeekBar)findViewById(effectsSound);
        generalSnd.setOnSeekBarChangeListener(this);
        musicSnd.setOnSeekBarChangeListener(this);
        effectsSnd.setOnSeekBarChangeListener(this);
        generalSnd.setProgress(generalSoundValue);
        musicSnd.setProgress(musicSoundValue);
        effectsSnd.setProgress(effectsSoundValue);


    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        SharedPreferences settings = getSharedPreferences("Settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        switch(seekBar.getId()){
            case(generalSound):{
                generalSoundValue=seekBar.getProgress();
                editor.putInt("GenSnd", generalSoundValue);
                MusicPlayer.musVolume=(float)generalSoundValue/100 *musicSoundValue/100;break;
            }
            case(musicSound):{
                musicSoundValue=seekBar.getProgress();
                editor.putInt("MusSnd", musicSoundValue);
                MusicPlayer.musVolume=(float)generalSoundValue/100 *musicSoundValue/100; break;
            }
            case(effectsSound):{
                effectsSoundValue=seekBar.getProgress();
                editor.putInt("EffSnd", effectsSoundValue);
                MusicPlayer.effVolume=(float)generalSoundValue/100 *effectsSoundValue/100;break;
            }
         }
         editor.apply();
        sound.setVolume((float)generalSoundValue/100 * musicSoundValue/100,
                (float)generalSoundValue/100 * musicSoundValue/100);



    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void MoveSound(Context context){

    }
}
