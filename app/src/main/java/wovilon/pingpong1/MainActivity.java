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
    protected void onCreate(Bundle savedInstanceState) {
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


       // Toast toast=Toast.makeText(this,text,Toast.LENGTH_SHORT);
       // toast.show();
    }


    public void onBtNewGame(View view) {
        musicPlayer.onstop();
        Intent intent=new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("LevelType", "MainLevels");
        intent.putExtra("LevelNumber", 1);
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
    }

    public void onExitClick(View view) {System.exit(0); }




}
