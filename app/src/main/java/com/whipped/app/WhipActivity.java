package com.whipped.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

public class WhipActivity extends AppCompatActivity {

    public static final String STRING_EXTRA_SOUND = "sound-extra";
    public static final String INT_EXTRA_IMAGE = "image-extra";
    final MediaPlayer mp = new MediaPlayer();

    //for detecting the change in the device's movement
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 12) {
                playWhip();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private Toolbar toolbar;
    private String sound;
    private ImageView whipImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whip);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        whipImage = (ImageView) findViewById(R.id.whip_image);

        Intent i = getIntent();
        sound = i.getStringExtra(STRING_EXTRA_SOUND);
        int image = i.getIntExtra(INT_EXTRA_IMAGE, R.drawable.whip_1);
        Glide.with(this).load(image).into(whipImage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playWhip();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupShakeListener();
        //animateImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private void animateImage() {
        if (whipImage.getVisibility() == View.VISIBLE) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Animator animator = ViewAnimationUtils.createCircularReveal(whipImage,
                    whipImage.getWidth() / 2,
                    whipImage.getHeight() / 2,
                    0,
                    (float) Math.hypot(whipImage.getWidth(), whipImage.getHeight()));
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    whipImage.setVisibility(View.VISIBLE);
                }
            });
            animator.setDuration(250);
            animator.start();
        } else {
            whipImage.animate().alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    whipImage.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void setupShakeListener() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private void playWhip() {
        if (mp.isPlaying()) {
            mp.stop();
        }

        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd(sound);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
