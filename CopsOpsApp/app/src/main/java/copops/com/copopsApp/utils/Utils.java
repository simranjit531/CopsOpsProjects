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
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import copops.com.copopsApp.R;
import copops.com.copopsApp.shortcut.ShortcutViewService;


public class Utils {

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

    public static void showAlert(String mgs, Context context) {
        new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle("Information")
                .setMessage(mgs)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }

                }).setIcon(R.drawable.information)
                .show();
    }


    public static void showAlertAndClick(String mgs, Context context, final resetPassInterFace mResetPassInterFace) {
        new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle("Information")
                .setMessage(mgs)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mResetPassInterFace.onClick(1);
                    }

                }).setIcon(R.drawable.information)
                .show();
    }

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


    public static String saveImage(Bitmap myBitmap, String IMAGE_DIRECTORY, Context mContext) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(mContext,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, Context mContext) {
        new AlertDialog.Builder(mContext, R.style.AlertDialogTheme)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //  Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

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

    //Connection Check

    public static boolean checkConnection(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null == activeNetwork)
            return false;
        return activeNetwork.isConnectedOrConnecting();
    }

    /////fragment call
    public static void fragmentCall(Fragment fragment, FragmentManager fm) {
        if (fragment != null) {
            FragmentTransaction transaction = fm.beginTransaction().addToBackStack(null);
            transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_in_righ, R.anim.anim_slide_out_right);
            transaction.replace(R.id.content_frame, fragment);
            //  transaction.commit();
            transaction.commitAllowingStateLoss();
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


    public SharedPreferences getSharedPref(Context mContext) {
        return mContext.getSharedPreferences(myPrefrence, Context.MODE_PRIVATE);
    }


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


    public static void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }


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


              //  android.os.Process.killProcess(android.os.Process.myPid());
             //   System.exit(1);
            }
        });

//        TVrefuse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }

    public static String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        return (totalMinutes / 60) + ":" + minutes;
    }

}
