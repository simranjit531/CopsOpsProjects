package copops.com.copopsApp.utils;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import copops.com.copopsApp.R;
import copops.com.copopsApp.shortcut.ShortcutViewService;
import copops.com.copopsApp.shortcut.ShortcutViewService_Citizen;

/**
 * Created by Ranjan Gupta
 */
public class Utils {
   // public static final String CHAT_BASE_URL="ws://82.165.253.201:8080";

    public static final String CHAT_BASE_URL="ws://93.90.201.167:8080";

    public static final String APP_ID = "75246";
    public static final String AUTH_KEY = "xeF9L4KE2yg76tE";
    public static final String AUTH_SECRET = "xcUmSHx-ZWFvT36";
    public static final String ACCOUNT_KEY = "75246";
    public static String iv = "fedcba9876543210";
    public static String key = "0123456789abcdef";

    String myPrefrence = "copops.com.copopsApp";
    public static String otpMsg = "didn't match otp";

    public static final String FILE_PROVIDER_AUTHORITY = "copops.com.copopsApp.fragment";
    public static final int REQUEST_TAKE_CAMERA_PHOTO = 1;
    public static final int REQUEST_TAKE_VIDEO = 200;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final String MAPKEY = "AIzaSyBrxD_Azxlj-PY4MUR1RYzwIFuLFt0GnOM";

    //    private static YourPreference yourPreference;
    private static SharedPreferences sharedPreferences;

    public static final int PERMISSION_REQUEST_CODE = 200;
    public static String APPURL = "";

    public static final int READ_REQUEST_CODE = 300;

    public interface resetPassInterFace {

        public void onClick(int id);
    }

    public interface clossPassInterFace {

        public void onClick();
    }


    public static String getFileNameFromUrl(URL url) {

        String urlString = url.getFile();

        return urlString.substring(urlString.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }
//Show Popup For Mesage
    public static void showAlert(String mgs, Context context) {
        new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(R.string.Information)
                .setMessage(mgs)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }

                }).setIcon(R.drawable.information)
                .show();
    }
    //Show Popup For Mesage
    public static void showAlertInterFace(String mgs, Context context,clossPassInterFace mClossPassInterFace) {
        new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(R.string.Information)
                .setMessage(mgs)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mClossPassInterFace.onClick();
                    }

                }).setIcon(R.drawable.information)
                .show();
    }

    //Show Popup For Mesage
    public static void showAlertfaq(String mgs, Context context) {
        new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(R.string.FAQ)
                .setMessage(mgs)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }

                }).setIcon(R.drawable.information)
                .show();
    }

    //Show Popup For Mesage
    public static void showAlertAndClick(String mgs, Context context, final resetPassInterFace mResetPassInterFace) {
        new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(R.string.Information)
                .setMessage(mgs)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mResetPassInterFace.onClick(1);
                    }

                }).setIcon(R.drawable.information)
                .show();
    }
//Email Validation Check
    public static boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        return check;
    }


//Check Permission for all like camera,storge,Location
    public static boolean checkPermission(Context mContext) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    public static void requestPermission(Activity mContext) {

        ActivityCompat.requestPermissions(mContext,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    //Show Popup For Mesage
    public static void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, Context mContext) {
        new AlertDialog.Builder(mContext, R.style.AlertDialogTheme)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.Cancel, null)
                .create()
                .show();
    }
//For return Uri Path For Bitmap/image
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //  Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    //For return Device  Path For image,video
    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    /// get device id
    public static String getDeviceId(Context mContext) {

        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //Internet Connection Check
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean checkConnection(Context mContext) {

        NetworkInfo networkInfo = null;

        ConnectivityManager connMgr =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
             networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }


        Log.e("network", "Wifi connected: " + isWifiConn);


        Log.e("network", "Mobile connected: " + isMobileConn);
        if(null==networkInfo) {
            return false;
        }else {
            return networkInfo.isConnectedOrConnecting();
        }

    }

    /////fragment call
    public static void fragmentCall(Fragment fragment, FragmentManager fm) {
        if (fragment != null) {
            FragmentTransaction transaction = fm.beginTransaction().addToBackStack(null);
            try {
                transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_in_righ, R.anim.anim_slide_out_right);
                transaction.replace(R.id.content_frame, fragment);
                //  transaction.commit();
                transaction.commitAllowingStateLoss();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /////fragment call
    public static void fragmentCalladd(Fragment fragment, FragmentManager fm) {
        if (fragment != null) {
            FragmentTransaction transaction = fm.beginTransaction().addToBackStack(null);
            transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_in_righ, R.anim.anim_slide_out_right);
            transaction.add(R.id.content_frame, fragment);
            //  transaction.commit();
            transaction.commitAllowingStateLoss();
        }
    }



//Hide Device Keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Save Data in SharedPreferences
    public static void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }
    //Get Data in SharedPreferences
    public static String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    //Show Popup For Logout
    public static void opendialogcustomdialog(Context mContext, String text) {

        final Dialog dialog = new Dialog(mContext, R.style.DialogFragmentTheme);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        TextView TVrefuse = (TextView) dialog.findViewById(R.id.mTvNo);
        TextView TVallow = (TextView) dialog.findViewById(R.id.mTvYes);
        TextView TVcustomdescriptiontext = (TextView) dialog.findViewById(R.id.TVcustomdescriptiontext);
        TVcustomdescriptiontext.setText(text);

        TVallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        TVrefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Show Popup For Exit APP
    public static void opendialogcustomdialogoperator(final Context mContext, String text, String loginstatus, String usertype) {

        final Dialog dialog = new Dialog(mContext, R.style.DialogFragmentTheme);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        TextView TVrefuse = (TextView) dialog.findViewById(R.id.mTvNo);
        TextView TVallow = (TextView) dialog.findViewById(R.id.mTvYes);
        TextView TVcustomdescriptiontext = (TextView) dialog.findViewById(R.id.TVcustomdescriptiontext);
        TVcustomdescriptiontext.setText(text);

        TVallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    mContext.startService(new Intent(mContext, ShortcutViewService.class));
                } else if (Settings.canDrawOverlays(mContext)) {
                    mContext.startService(new Intent(mContext, ShortcutViewService.class));
                }
                // android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        TVrefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Show Popup For Exit APP
    public static void opendialogcustomdialogcitizen(final Context mContext, String text, String loginstatus, String usertype) {

        final Dialog dialog = new Dialog(mContext, R.style.DialogFragmentTheme);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        TextView TVrefuse = (TextView) dialog.findViewById(R.id.mTvNo);
        TextView TVallow = (TextView) dialog.findViewById(R.id.mTvYes);
        TextView TVcustomdescriptiontext = (TextView) dialog.findViewById(R.id.TVcustomdescriptiontext);
        TVcustomdescriptiontext.setText(text);

        TVallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    mContext.startService(new Intent(mContext, ShortcutViewService_Citizen.class));
                } else if (Settings.canDrawOverlays(mContext)) {
                    mContext.startService(new Intent(mContext, ShortcutViewService_Citizen.class));
                }
                //android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        TVrefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Check Gps on Or Not
    public static void statusCheck(Context mContext) {
        final LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(mContext);

        }
    }
    //Check Gps on Or Not
    public static void buildAlertMessageNoGps(Context mContext) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("To continue, turn on device location.?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        mContext.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    //Show Popup For Exit APP
    public static void opendialogcustomdialogClose(Context mContext, String text, final clossPassInterFace mClossPassInterFace) {

        final Dialog dialog = new Dialog(mContext, R.style.DialogFragmentTheme);
        dialog.setContentView(R.layout.closepop);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        TextView TVallow = (TextView) dialog.findViewById(R.id.mTvYes);

        TextView TVcustomdescriptiontext = (TextView) dialog.findViewById(R.id.TVcustomdescriptiontext);

        TVcustomdescriptiontext.setText(text);

        TVallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                mClossPassInterFace.onClick();

            }
        });


        dialog.show();
    }


}
