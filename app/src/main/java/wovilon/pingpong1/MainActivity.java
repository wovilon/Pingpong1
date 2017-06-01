package wovilon.pingpong1;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import wovilon.pingpong1.db.DbUpdater;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    Sensor mySensor;
    SensorManager SM;
    double phoneRot;
    MusicPlayer musicPlayer;


    void ex(float d){this.phoneRot=d; }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //textView1.setText(""+event.values[0]);


        ex(event.values[0]); //if (this.phoneRot>2) System.exit(0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("Settings", 0);
        musicPlayer=new MusicPlayer();
        musicPlayer.oncreate(this, "menuFon");
        float vol=(float)settings.getInt("GenSnd",50) * settings.getInt("MusSnd",100);  vol/=100*100;
        musicPlayer.volume(vol,vol);
        musicPlayer.onstart();

        SM=(SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor=SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);

        new DbUpdater(this, "MainLevels").addMainLevels(); //TODO now I add level everytime, but once needed
        //new DbUpdater(this, "MainLevels").setHighScore("MainLevels",0,1);


    }


    public void onBtNewGame(View view) {
        musicPlayer.onstop();
        Intent intent=new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("LevelType", "MainLevels");
        intent.putExtra("LevelNumber", 0);
        startActivity(intent);

    }

    public void onHowClick(View view) {
        Intent intent=new Intent(MainActivity.this, LevelEditorActivity.class);
        startActivity(intent);
    }

    public void onSettingsClick(View view) {
        Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onContactsClick(View view) {
        Intent intent=new Intent(MainActivity.this, ContactsActivity.class);
        startActivity(intent);
    }

    public void onExitClick(View view) { System.exit(0); }

}
