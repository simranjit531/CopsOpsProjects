package com.copsopsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.copsopsapp.R;

/**
 * Created by Lenovo on 07-11-2018.
 */

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        refreshUI();


    }


    private void refreshUI() {
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                          startActivity(intent);
                                          finish();
                                      }
                                  },
                SPLASH_TIME_OUT);
    }
}
