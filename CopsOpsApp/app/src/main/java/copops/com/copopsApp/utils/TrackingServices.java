package copops.com.copopsApp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quickblox.messages.services.QBPushManager;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;

import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import copops.com.copopsApp.activity.DashboardActivity;
import copops.com.copopsApp.chatmodule.utils.chat.ChatHelper;
import copops.com.copopsApp.chatmodule.utils.qb.QbDialogHolder;
import copops.com.copopsApp.chatmodule.utils.qb.callback.QBPushSubscribeListenerImpl;
import copops.com.copopsApp.fragment.Frag_Public_Profile_Shown;
import copops.com.copopsApp.fragment.HomeFragment;
import copops.com.copopsApp.fragment.LoginFragment;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.LoginPojoSetData;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.shortcut.GPSTracker;
import copops.com.copopsApp.shortcut.ShortcutViewService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingServices extends Service {


    AppSession mAppSession;
    double latitude;
    double longitude;
    GPSTracker gps;



    //public static final int notify = 30000;  //interval between two services(Here Service run every 5 seconds)
    public static final int notify = 5000;  //interval between two services(Here Service run every 5 seconds)
    int count = 0;  //number of times service is display
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {


        mAppSession = mAppSession.getInstance(getApplicationContext());
        gps = new GPSTracker(getApplicationContext());
       // mLocationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
     //   Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }

    //class TimeDisplay for handling task
    public class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {


                        latitude=gps.getLatitude();
                        longitude=gps.getLongitude();


                        LoginPojoSetData mLoginPojoSetData = new LoginPojoSetData();

                        Log.e("always","tracking");
                        Log.e("latitude"," "+latitude);
                        Log.e("longitude"," "+longitude);

                        mLoginPojoSetData.setUser_id(mAppSession.getData("id"));
                        mLoginPojoSetData.setLatitude(String.valueOf(latitude));
                        mLoginPojoSetData.setLongitude(String.valueOf(longitude));

                        Log.e("taackingdata", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mLoginPojoSetData)));
                        RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mLoginPojoSetData)));

                        getLocation(mFile);

                    }
            });

        }

    }




    private boolean checkPermission() {
        boolean check = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!check) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        }
        return true;
//    }
    }

    private void getLocation(RequestBody Data) {

        try {

            copops.com.copopsApp.services.Service operator = ApiUtils.getAPIService();
            Call<CommanStatusPojo> getallLatLong = operator.getlocations(Data);
            getallLatLong.enqueue(new Callback<CommanStatusPojo>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response)

                {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo commanStatusPojo = response.body();

                            if (commanStatusPojo.getStatus().equals("false")) {


                            } else {


                                Log.e("isfreeze",""+commanStatusPojo.getIsfreeze());
                                Log.e("@@isfreeze",""+commanStatusPojo.getMessage());

 if(commanStatusPojo.getIsfreeze().equalsIgnoreCase("0")){
     mAppSession.saveData("Login", "0");
     userLogout();

     mAppSession.saveData("freez","0");
     stopService(new Intent(getBaseContext(), TrackingServices.class));
   //  stopService(new Intent(getBaseContext(), TrackingServices.class));

   // Utils.fragmentCall(new HomeFragment(), getFragmentManager());
     Intent intent = new Intent(getBaseContext(),DashboardActivity.class);
     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     getApplication().startActivity(intent);
 }else{
    // mAppSession.saveData("freez","1");

    //
 }
                            }


                        } else {

                        }

                    } catch (Exception e) {

                        e.getMessage();

                    }
                }

                @Override
                public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
                    Log.d("TAG", "Error " + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void userLogout() {
        ChatHelper.getInstance().destroy();
        logout();
        SharedPrefsHelper.getInstance().removeQbUser();

        //  finish();
        //  LoginActivity.start(DialogsActivity.this);
        //   Intent mIntent = new Intent(DialogsActivity.this,DashboardActivity.class);
        //   startActivity(mIntent);
        QbDialogHolder.getInstance().clear();
        //  ProgressDialogFragment.hide(getSupportFragmentManager());
        //  finish();
    }
    private void logout() {
        if (QBPushManager.getInstance().isSubscribedToPushes()) {
            QBPushManager.getInstance().addListener(new QBPushSubscribeListenerImpl() {
                @Override
                public void onSubscriptionDeleted(boolean success) {
                    logoutREST();
                    QBPushManager.getInstance().removeListener(this);
                }
            });
            SubscribeService.unSubscribeFromPushes(getApplicationContext());
        } else {
            logoutREST();
        }
    }

    private void logoutREST() {
        QBUsers.signOut().performAsync(null);
    }
}