package com.example.compasapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compasapp.Fragments.FragmentOptions;
import com.example.compasapp.interfaces.SensorNaviCompassInterface;
import com.example.compasapp.model.Options;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorNaviCompassInterface {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private Boolean mFlag;

    private FragmentOptions mFragmentOptions;
    private Options mOptions;

    //Views
    private ImageView mImageCompass;
    private TextView mTvHeading;
    private Button mBtnSetLocation;

    // Sensor
    private SensorManager mSensorManager;


    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagneticField;

    private float[] mFloatGravity = new float[3];
    private float[] mFloatGeoMagnetic = new float[3];

    private float[] mFloatOrientation = new float[3];
    private float[] mFloatRotationMatrix = new float[9];


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkAndRequestPermissions();

        mFlag = false;

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mFragmentOptions = new FragmentOptions();
        mOptions = new Options();

        mBtnSetLocation = findViewById(R.id.setLocationBtn);
        mImageCompass = (ImageView) findViewById(R.id.imageViewCompass);
        mTvHeading = (TextView) findViewById(R.id.tvHeading);

        mTvHeading.setText("You have not set any destination.");

        mBtnSetLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction
                            .replace(R.id.fragmentContainer, mFragmentOptions)
                            .addToBackStack(null)
                            .commit();
                    mBtnSetLocation.setVisibility(View.GONE);
                }
            });
        createSensor();
    }

    // Check all required permissions
    private  boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBtnSetLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int hasPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            mFlag = true;
        } else {
            if(!mFlag){
                mBtnSetLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Please allow all permissions to continue!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public void createSensor() {
        SensorEventListener sensorEventListenerAccelrometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                mFloatGravity = event.values;

                SensorManager.getRotationMatrix(mFloatRotationMatrix, null, mFloatGravity, mFloatGeoMagnetic);
                SensorManager.getOrientation(mFloatRotationMatrix, mFloatOrientation);

                mImageCompass.setRotation((float) (-mFloatOrientation[0]*180/3.14159));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                mFloatGeoMagnetic = event.values;

                SensorManager.getRotationMatrix(mFloatRotationMatrix, null, mFloatGravity, mFloatGeoMagnetic);
                SensorManager.getOrientation(mFloatRotationMatrix, mFloatOrientation);

                mImageCompass.setRotation((float) (-mFloatOrientation[0]*180/3.14159));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        mSensorManager.registerListener(sensorEventListenerAccelrometer, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(sensorEventListenerMagneticField, mSensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }
}