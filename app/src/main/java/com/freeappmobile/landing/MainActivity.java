package com.freeappmobile.landing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.freeappmobile.R;
import com.freeappmobile.home.HomeActivity;
import com.freeappmobile.preferences.FeeAppPreferences;
import com.freeappmobile.utils.BaseActivity;
import com.freeappmobile.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {


    private Context mActivity;
    private long splashDelay = 3000;
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    private void init() {
        mActivity = MainActivity.this;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(mActivity, HomeActivity.class);
                finish();
                startActivity(i);
            }
        };


        timer = new Timer();
        timer.schedule(task, splashDelay);

        FeeAppPreferences.setUDID(mActivity, Utils.getDeviceAndroidID(mActivity));

    }
}
