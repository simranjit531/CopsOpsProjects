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
import android.view.View;


import com.google.firebase.iid.FirebaseInstanceId;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.ErrorUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import copops.com.copopsApp.R;
import copops.com.copopsApp.chatmodule.App;
import copops.com.copopsApp.chatmodule.ui.activity.DialogsActivity;
import copops.com.copopsApp.chatmodule.utils.PushBroadcastReceiver;
import copops.com.copopsApp.chatmodule.utils.chat.ChatHelper;
import copops.com.copopsApp.chatmodule.utils.qb.QbChatDialogMessageListenerImp;
import copops.com.copopsApp.fragment.AssignmentTableFragment;
import copops.com.copopsApp.fragment.AuthenticateCodeFragment;
import copops.com.copopsApp.fragment.CitizenFragment;
import copops.com.copopsApp.fragment.Frag_Call_Number;
import copops.com.copopsApp.fragment.HomeFragment;
import copops.com.copopsApp.fragment.IncedentGenerateFragment;
import copops.com.copopsApp.fragment.IncedentReportFragment;
import copops.com.copopsApp.fragment.OperatorFragment;
import copops.com.copopsApp.fragment.SpleshFragment;
import copops.com.copopsApp.shortcut.AssignmentTableFragmentShortcut;
import copops.com.copopsApp.shortcut.PositionOfInteervebtionsFragmentShortcut;
import copops.com.copopsApp.shortcut.ShortcutViewService;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.Utils;

public class DashboardActivity extends AppCompatActivity {


    Fragment fragment = null;
    AppSession mAppSession;

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private Handler handler;
    private Runnable checkOverlaySetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
      //  Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());
        //   buildUsersList();



        //   QBSettings.getInstance().setStoringMehanism(StoringMechanism.UNSECURED); //call before init method for QBSettings

        mAppSession = mAppSession.getInstance(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        handler = new Handler();
        checkOverlaySetting = new Runnable() {
            @Override
            @TargetApi(23)
            public void run() {
                if (Settings.canDrawOverlays(DashboardActivity.this)) {
                    //You have the permission, re-launch MainActivity
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

    private void fragmentContener() {
        // Utils.fragmentCall(new SpleshFragment(), getSupportFragmentManager());

        Log.e("shortcutscreentype==", mAppSession.getData("shortcutscreentype"));

        if (mAppSession.getData("shortcutscreentype").equals("reportanincident")) {
            Utils.fragmentCall(new PositionOfInteervebtionsFragmentShortcut(), getSupportFragmentManager());
        } else if (mAppSession.getData("shortcutscreentype").equals("assignedintervation")) {
            Utils.fragmentCall(new AssignmentTableFragmentShortcut(), getSupportFragmentManager());
        } else {
            String intent= getIntent().getStringExtra("notification");

            //  String notification = mAppSession.getData("notification");
            if(intent==null) {
                Utils.fragmentCall(new SpleshFragment(), getSupportFragmentManager());
            }else{
                Utils.fragmentCall(new AssignmentTableFragment(), getSupportFragmentManager());
            }
        }


    }

    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (f instanceof CitizenFragment) {//the fragment on which you want to handle your back press
            Utils.opendialogcustomdialog(DashboardActivity.this, DashboardActivity.this.getString(R.string.exit_message));
        } else if (f instanceof HomeFragment) {//the fragment on which you want to handle your back press
            Utils.opendialogcustomdialog(DashboardActivity.this, DashboardActivity.this.getString(R.string.exit_message));
        } else if (f instanceof OperatorFragment) {//the fragment on which you want to handle your back press
            // Utils.opendialogcustomdialog(DashboardActivity.this, DashboardActivity.this.getString(R.string.exit_message));
            Utils.opendialogcustomdialogoperator(DashboardActivity.this, DashboardActivity.this.getString(R.string.exit_message), mAppSession.getData("Login"), mAppSession.getData("userType"));

        } else if (f instanceof IncedentReportFragment) {//the fragment on which you want to handle your back press

            if (mAppSession.getData("").equalsIgnoreCase("1")) {

            }

        } else if (f instanceof AuthenticateCodeFragment) {//the fragment on which you want to handle your back press

        }else if (f instanceof AssignmentTableFragment) {//the fragment on which you want to handle your back press
            Utils.fragmentCall(new OperatorFragment(), getSupportFragmentManager());
        } else if (f instanceof Frag_Call_Number) {//the fragment on which you want to handle your back press

        } else if (f instanceof IncedentGenerateFragment) {

            // FragmentManager fm = getFragmentManager();
            int count = getSupportFragmentManager().getBackStackEntryCount();
            int count2 = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 4) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                //   Utils.fragmentCall(new GPSPublicFragment(), getSupportFragmentManager());
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }

            }
//            if(mAppSession.getData("screen")=="1"){
//
//              //  getFragmentManager().popBackStack(  getFragmentManager().getBackStackEntryAt(  getFragmentManager().getBackStackEntryCount()-2).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                //   getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                Utils.fragmentCall(new GPSPublicFragment(), getSupportFragmentManager());
//
//            }else{
//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStackImmediate();
//                }
//            }
            //   getFragmentManager().popBackStack(  getFragmentManager().getBackStackEntryAt( getFragmentManager().getBackStackEntryCount()-2).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //   getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            // Utils.fragmentCall(new GPSPublicFragment(), getSupportFragmentManager());
        } else if (f instanceof AssignmentTableFragmentShortcut) {//the fragment on which you want to handle your back press
            Utils.fragmentCall(new OperatorFragment(), getSupportFragmentManager());
        } else if (f instanceof PositionOfInteervebtionsFragmentShortcut) {//the fragment on which you want to handle your back press
            Utils.fragmentCall(new OperatorFragment(), getSupportFragmentManager());
        } else {
            super.onBackPressed();
        }

    }


    protected void onStop() {
        super.onStop();
        Log.e("copsopsstop", "loginvalid==" + mAppSession.getData("Login") + "==userType==" + mAppSession.getData("userType"));

        String loginvalid = mAppSession.getData("Login");
        String userType = mAppSession.getData("userType");

        if (loginvalid.equals("1") && userType.equals("Cops")) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                startService(new Intent(DashboardActivity.this, ShortcutViewService.class));
            } else if (Settings.canDrawOverlays(DashboardActivity.this)) {
                startService(new Intent(DashboardActivity.this, ShortcutViewService.class));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("copsopsstart", "loginvalid==" + mAppSession.getData("Login") + "==userType==" + mAppSession.getData("userType"));

        String loginvalid = mAppSession.getData("Login");
        String userType = mAppSession.getData("userType");
        if (loginvalid.equals("1") && userType.equals("Cops")) {
            stopService(new Intent(DashboardActivity.this, ShortcutViewService.class));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

      /*  String loginvalid = mAppSession.getData("Login");
        String userType = mAppSession.getData("userType");
        Log.e("copsopsdestroy", "loginvalid==" + mAppSession.getData("Login") + "==userType==" + mAppSession.getData("userType"));
        if (loginvalid.equals("1") && userType.equals("Cops")) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                startService(new Intent(DashboardActivity.this, ShortcutViewService.class));
            } else if (Settings.canDrawOverlays(DashboardActivity.this)) {
                startService(new Intent(DashboardActivity.this, ShortcutViewService.class));
            }
        }*/

    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
        handler.postDelayed(checkOverlaySetting, 1000);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
