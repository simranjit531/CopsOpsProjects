package copops.com.copopsApp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;
import copops.com.copopsApp.R;


/**
 * Created by Ranjan Gupta
 */

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mContext=SplashScreen.this;
        refreshUI();
    }
//UI Show After Splash Screen
    private void refreshUI() {
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {

                                          if (getIntent().getExtras()!=null)
                                          {

                                          }else
                                          {
                                              startActivity(new Intent(mContext, DashboardActivity.class));
                                              finish();
                                          }


                                      }
                                  },
                SPLASH_TIME_OUT);
    }

}
