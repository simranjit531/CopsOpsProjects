package com.copsopsapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.copsopsapp.R;
import com.copsopsapp.fragment.Frag_Authenticate_Code;
import com.copsopsapp.fragment.Frag_Call_Number;
import com.copsopsapp.fragment.Frag_Details_of_Reporting;
import com.copsopsapp.fragment.Frag_Fireman_ambulance;
import com.copsopsapp.fragment.Frag_Handrain;
import com.copsopsapp.fragment.Frag_Home;
import com.copsopsapp.fragment.Frag_Home_Citoyen;
import com.copsopsapp.fragment.Frag_Home_Operator;
import com.copsopsapp.fragment.Frag_ID_of_Reporting;
import com.copsopsapp.fragment.Frag_Login;
import com.copsopsapp.fragment.Frag_Operator_Profile;
import com.copsopsapp.fragment.Frag_Public_Subscription;
import com.copsopsapp.fragment.Frag_Report_an_Incidents;
import com.copsopsapp.fragment.Frag_Reporting_Police;
import com.copsopsapp.fragment.Frag_Reporting_Ville;
import com.copsopsapp.fragment.Frag_Reset_Password;
import com.copsopsapp.fragment.Frag_Signature;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public TextView TVtitle;
    public ImageView IVback,IVlogout;
    public RelativeLayout Rltoolbar;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setuptoolbar();

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.CALL_PHONE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE,
                        }, 11);
            } else {

                displayScreen(0, null);

            }
        } else {
            displayScreen(0, null);

        }




    }


    public void displayScreen(int position, Bundle bundle) {

        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {

            case 0:
                fragment = new Frag_Home();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 1:
                fragment = new Frag_Authenticate_Code();
                fragment.setArguments(bundle);
                break;

            case 2:
                fragment = new Frag_Home_Citoyen();
                fragment.setArguments(bundle);
                break;

            case 3:
                fragment = new Frag_Home_Operator();
                fragment.setArguments(bundle);
                break;

            case 4:
                fragment = new Frag_Login();
                fragment.setArguments(bundle);
                break;

            case 5:
                fragment = new Frag_Operator_Profile();
                fragment.setArguments(bundle);
                break;

            case 6:
                fragment = new Frag_Public_Subscription();
                fragment.setArguments(bundle);
                break;

            case 7:
                fragment = new Frag_Reset_Password();
                fragment.setArguments(bundle);
                break;

            case 8:
                fragment = new Frag_Call_Number();
                fragment.setArguments(bundle);
                break;

            case 9:
                fragment = new Frag_Details_of_Reporting();
                fragment.setArguments(bundle);
                break;

            case 10:
                fragment = new Frag_Fireman_ambulance();
                fragment.setArguments(bundle);
                break;


            case 11:
                fragment = new Frag_Handrain();
                fragment.setArguments(bundle);
                break;


            case 12:
                fragment = new Frag_ID_of_Reporting();
                fragment.setArguments(bundle);
                break;


            case 13:
                fragment = new Frag_Report_an_Incidents();
                fragment.setArguments(bundle);
                break;


            case 14:
                fragment = new Frag_Reporting_Police();
                fragment.setArguments(bundle);
                break;


            case 15:
                fragment = new Frag_Reporting_Ville();
                fragment.setArguments(bundle);
                break;

            case 16:
                fragment = new Frag_Signature();
                fragment.setArguments(bundle);
                break;

            case 17:

                break;


            default:
                break;
        }
        if (fragment != null) {
            // fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
            transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_in_righ, R.anim.anim_slide_out_right);
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();
        }
    }


    public void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++)
            fragmentManager.popBackStack();
    }

    public void popBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isGranted = true;
        switch (requestCode) {
            case 11: {

                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        isGranted = false;
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setTitle(getResources().getString(R.string.app_name));
                        alert.setMessage(permissions[i] + " Permission Required for run app successfully.\nGo to your device setting, click on apps,\nselect your app and enable required permission for App");
                        alert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub;
                                        finish();
                                    }
                                });
                        alert.show();
                        break;
                    }
                }

            }
        }

        if (isGranted) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new Frag_Home();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();

        }
    }


    public void isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled || !isNetworkEnabled) {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.GPS_is_settings));
        alertDialog.setMessage(getResources().getString(R.string.GPS_is_not_enabled));
        alertDialog.setPositiveButton(getResources().getString(R.string.Settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void setuptoolbar() {
        IVback = (ImageView) findViewById(R.id.IVback);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        IVlogout = (ImageView) findViewById(R.id.IVlogout);
        Rltoolbar = (RelativeLayout) findViewById(R.id.Rltoolbar);

        IVback.setOnClickListener(this);
        IVlogout.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.IVback:

                popBackStack();

                break;

            case R.id.IVlogout:



                break;



        }

    }

}

