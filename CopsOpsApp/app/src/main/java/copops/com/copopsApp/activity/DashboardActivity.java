package copops.com.copopsApp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import java.util.Locale;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import org.json.JSONException;
import org.json.JSONObject;
import copops.com.copopsApp.R;
import copops.com.copopsApp.fragment.AssignedInterventionFragment;
import copops.com.copopsApp.fragment.AssignmentTableFragment;
import copops.com.copopsApp.fragment.AuthenticateCodeFragment;
import copops.com.copopsApp.fragment.ChatConversionFragment;
import copops.com.copopsApp.fragment.ChatRecentFragment;
import copops.com.copopsApp.fragment.CitizenFragment;
import copops.com.copopsApp.fragment.Frag_Call_Number;
import copops.com.copopsApp.fragment.HandrailFragment;
import copops.com.copopsApp.fragment.HomeFragment;
import copops.com.copopsApp.fragment.IncedentGenerateFragment;
import copops.com.copopsApp.fragment.IncedentReportFragment;
import copops.com.copopsApp.fragment.IncidentFragment;
import copops.com.copopsApp.fragment.OperatorFragment;
import copops.com.copopsApp.fragment.SpleshFragment;
import copops.com.copopsApp.shortcut.AssignmentTableFragmentShortcut;
import copops.com.copopsApp.shortcut.PositionOfInteervebtionsFragmentShortcut;
import copops.com.copopsApp.shortcut.ShortcutViewService;
import copops.com.copopsApp.shortcut.ShortcutViewService_Citizen;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.TrackingServices;
import copops.com.copopsApp.utils.Utils;
/**
 * Created by Ranjan Gupta
 */
public class DashboardActivity extends AppCompatActivity {

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private Handler handler;
    private Runnable checkOverlaySetting;
    private Context mContext;
    AppSession mAppSession;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard);
            mContext=DashboardActivity.this;

            mAppSession.saveData("intervantion_noti","0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                System.exit(2);
            }
        });
        String devicelanguage = Locale.getDefault().getDisplayLanguage();
        mAppSession = mAppSession.getInstance(this);
        if (mAppSession.getData("first").equalsIgnoreCase("")) {
            mAppSession.saveData("first", "1");
            goToNotificationSettings(null, this);
        }
        if (devicelanguage.equalsIgnoreCase("english")) {
            mAppSession.saveData("devicelanguage", "En");
        } else {
            mAppSession.saveData("devicelanguage", "Fr");
        }
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        stopService(new Intent(getBaseContext(), TrackingServices.class));
        handler = new Handler();
        checkOverlaySetting = new Runnable() {
            @Override
            @TargetApi(23)
            public void run() {
                if (Settings.canDrawOverlays(DashboardActivity.this)) {
                    handler.removeCallbacks(checkOverlaySetting);
                    Intent i = new Intent(DashboardActivity.this, DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    return;
                }
                handler.postDelayed(this, 1000);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.CALL_PHONE,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            }, 11);
                }
            }
        }
        fragmentContener();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("copsopsstart", "loginvalid==" + mAppSession.getData("Login") + "==userType==" + mAppSession.getData("userType"));
        String loginvalid = mAppSession.getData("Login");
        String userType = mAppSession.getData("userType");
        if (loginvalid.equals("1") && userType.equals("Cops")) {
            stopService(new Intent(DashboardActivity.this, ShortcutViewService.class));
        } else if (loginvalid.equals("1") && userType.equals("citizen")) {
            stopService(new Intent(DashboardActivity.this, ShortcutViewService_Citizen.class));
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mAppSession.saveData("screenShow","abc");
        Log.e("helll", "ranjan");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String loginvalid = mAppSession.getData("Login");
        String userType = mAppSession.getData("userType");
        if (loginvalid.equals("1") && userType.equals("Cops")) {
            stopService(new Intent(DashboardActivity.this, ShortcutViewService.class));
        } else if (loginvalid.equals("1") && userType.equals("citizen")) {
            stopService(new Intent(DashboardActivity.this, ShortcutViewService_Citizen.class));
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Intent intent_o = getIntent();
        Log.e("hhh",""+intent_o);
        String msg = getIntent().getStringExtra("remMsg");
        if(mAppSession.getData("intervantion_noti").equalsIgnoreCase("1")){
            Utils.fragmentCall(new AssignedInterventionFragment(msg), getSupportFragmentManager());

            mAppSession.saveData("intervantion_noti", "");
        }else {
            if (mAppSession.getData("movechart").equalsIgnoreCase("1")) {
                if (mAppSession.getData("id").equalsIgnoreCase((mAppSession.getData("reciverid")))) {
                    Utils.fragmentCall(new ChatConversionFragment(mAppSession.getData("senderId"), mAppSession.getData("user"), mAppSession.getData("reciverid")), getSupportFragmentManager());
                    mAppSession.saveData("movechart", "");
                } else {
                    Utils.fragmentCall(new ChatConversionFragment(mAppSession.getData("reciverid"), mAppSession.getData("user"), mAppSession.getData("senderId")), getSupportFragmentManager());
                    mAppSession.saveData("movechart", "");
                }
            }
        }
    }
/*
for use load frragment
 */
    private void fragmentContener() {
        Log.e("shortcutscreentype==", mAppSession.getData("shortcutscreentype"));

        if (mAppSession.getData("shortcutscreentype").equals("reportanincident")) {

            Utils.fragmentCall(new PositionOfInteervebtionsFragmentShortcut(), getSupportFragmentManager());
        } else if (mAppSession.getData("shortcutscreentype").equals("assignedintervation")) {
            Utils.fragmentCall(new AssignmentTableFragmentShortcut(), getSupportFragmentManager());
        } else if (mAppSession.getData("shortcutscreentype").equals("citizenhandrail")) {
            Utils.fragmentCall(new HandrailFragment(), getSupportFragmentManager());
            mAppSession.saveData("handrail", "handrail");
        } else if (mAppSession.getData("shortcutscreentype").equals("citizenreportanincident")) {
            Utils.fragmentCall(new IncidentFragment(mAppSession.getData("id")), getSupportFragmentManager());
            mAppSession.saveData("operatorScreenBack", "1");
            mAppSession.saveData("handrail", "dasd");
        } else if (mAppSession.getData("shortcutscreentype").equals("chat")) {
            Utils.fragmentCall(new ChatRecentFragment(), getSupportFragmentManager());
        }
        else
            {
                String intent = getIntent().getStringExtra("notification");
                String msg = getIntent().getStringExtra("remMsg");
                Log.e("jsondata",""+msg);
                if (intent == null || msg==null ) {
                    Utils.fragmentCall(new SpleshFragment(), getSupportFragmentManager());
                } else if(intent.equalsIgnoreCase("chat")) {
                    try {
                        JSONObject  jsonObject = new JSONObject(msg);
                        if(mAppSession.getData("id").equalsIgnoreCase((jsonObject.getString("receiver_id")))){
                            Utils.fragmentCall(new ChatConversionFragment(jsonObject.getString("sender_id"),jsonObject.getString("user"),jsonObject.getString("receiver_id")), getSupportFragmentManager());
                        }else{
                            Utils.fragmentCall(new ChatConversionFragment(jsonObject.getString("receiver_id"),jsonObject.getString("user"),jsonObject.getString("sender_id")), getSupportFragmentManager());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                    else{
                    if (mAppSession.getData("copsuser").equalsIgnoreCase("citizen")) {
                        Utils.fragmentCall(new CitizenFragment(), getSupportFragmentManager());
                    } else {
                        Utils.fragmentCall(new AssignedInterventionFragment(msg), getSupportFragmentManager());
                    }
                }
        }
    }
    @Override
    public void onBackPressed() {


        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (f instanceof CitizenFragment) {//the fragment on which you want to handle your back press
            Utils.opendialogcustomdialogcitizen(DashboardActivity.this, DashboardActivity.this.getString(R.string.exit_message), mAppSession.getData("Login"), mAppSession.getData("userType"));
        } else if (f instanceof HomeFragment) {//the fragment on which you want to handle your back press
            Utils.opendialogcustomdialog(DashboardActivity.this, DashboardActivity.this.getString(R.string.exit_message));
        } else if (f instanceof OperatorFragment) {//the fragment on which you want to handle your back press
            Utils.opendialogcustomdialogoperator(DashboardActivity.this, DashboardActivity.this.getString(R.string.exit_message), mAppSession.getData("Login"), mAppSession.getData("userType"));

        } else if (f instanceof IncedentReportFragment) {//the fragment on which you want to handle your back press

            if (mAppSession.getData("").equalsIgnoreCase("1")) {

            }

        } else if (f instanceof AuthenticateCodeFragment) {//the fragment on which you want to handle your back press

        } else if (f instanceof AssignmentTableFragment) {//the fragment on which you want to handle your back press
            Utils.fragmentCall(new OperatorFragment(), getSupportFragmentManager());
        } else if (f instanceof Frag_Call_Number) {//the fragment on which you want to handle your back press

        } else if (f instanceof AssignedInterventionFragment) {//the fragment on which you want to handle your back press
            getFragmentManager().popBackStackImmediate();
                Utils.fragmentCall(new OperatorFragment(), getSupportFragmentManager());
        } else if (f instanceof IncedentGenerateFragment) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 4) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }
        } else if (f instanceof AssignmentTableFragmentShortcut) {//the fragment on which you want to handle your back press
            Utils.fragmentCall(new OperatorFragment(), getSupportFragmentManager());
        }else if (f instanceof ChatConversionFragment) {//the fragment on which you want to handle your back press
            Utils.fragmentCall(new ChatRecentFragment(), getSupportFragmentManager());
        }else if (f instanceof ChatRecentFragment) {//the fragment on which you want to handle your back press
            getSupportFragmentManager().popBackStackImmediate();
            Utils.fragmentCall(new OperatorFragment(), getSupportFragmentManager());
        }   else if (f instanceof PositionOfInteervebtionsFragmentShortcut) {//the fragment on which you want to handle your back press
            Utils.fragmentCall(new OperatorFragment(), getSupportFragmentManager());
        } else if (f instanceof IncidentFragment) {//the fragment on which you want to handle your back press
            String loginvalid = mAppSession.getData("Login");
            String userType = mAppSession.getData("userType");
            if (loginvalid.equals("1") && userType.equals("Cops")) {
                getSupportFragmentManager().popBackStackImmediate();
                Utils.fragmentCall(new OperatorFragment(), getSupportFragmentManager());
            } else if (loginvalid.equals("1") && userType.equals("citizen")) {
                getSupportFragmentManager().popBackStackImmediate();
                Utils.fragmentCall(new CitizenFragment(), getSupportFragmentManager());
            }
        } else if (f instanceof HandrailFragment) {//the fragment on which you want to handle your back press
            getSupportFragmentManager().popBackStackImmediate();
            Utils.fragmentCall(new CitizenFragment(), getSupportFragmentManager());
        } else {
            super.onBackPressed();
        }

    }
    protected void onStop() {
        super.onStop();
        String loginvalid = mAppSession.getData("Login");
        String userType = mAppSession.getData("userType");

        if (loginvalid.equals("1") && userType.equals("Cops")) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                startService(new Intent(DashboardActivity.this, ShortcutViewService.class));
            } else if (Settings.canDrawOverlays(DashboardActivity.this)) {
                startService(new Intent(DashboardActivity.this, ShortcutViewService.class));
            }
        } else if (loginvalid.equals("1") && userType.equals("citizen")) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                startService(new Intent(DashboardActivity.this, ShortcutViewService_Citizen.class));
            } else if (Settings.canDrawOverlays(DashboardActivity.this)) {
                startService(new Intent(DashboardActivity.this, ShortcutViewService_Citizen.class));
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /*
    for user premission
     */
    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
        handler.postDelayed(checkOverlaySetting, 1000);
    }

   /*
   FOR USE NOTIFCATION
    */

    public void goToNotificationSettings(String channel, Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            if (channel != null) {
                intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
            } else {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            }
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (channel != null) {
                intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
            } else {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            }
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        }
        context.startActivity(intent);
    }






}
