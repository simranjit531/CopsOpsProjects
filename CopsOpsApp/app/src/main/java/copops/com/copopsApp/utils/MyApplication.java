package copops.com.copopsApp.utils;

/**
 * Created by Lenovo on 19-12-2018.
 */

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



}
