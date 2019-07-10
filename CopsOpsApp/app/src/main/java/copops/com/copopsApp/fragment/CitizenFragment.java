package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.OperatorShowAlInfo;
import copops.com.copopsApp.pojo.RegistationPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.shortcut.ShortcutViewService;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.EncryptUtils;

import copops.com.copopsApp.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ranjan Gupta
 */
@SuppressLint("ValidFragment")
public class CitizenFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.IVprofilephoto)
    CircleImageView IVprofilephoto;

    @BindView(R.id.TVname)
    TextView TVname;

    @BindView(R.id.TVprogresspercentage)
    TextView TVprogresspercentage;

    @BindView(R.id.TVprofiledescription)
    TextView TVprofiledescription;


    @BindView(R.id.TVprogressbarnumber)
    TextView TVprogressbarnumber;


    @BindView(R.id.TVprogressbarreports)
    TextView TVprogressbarreports;


    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    @BindView(R.id.RLreportanincident)
    RelativeLayout RLreportanincident;

    @BindView(R.id.RLnavigation)
    RelativeLayout RLnavigation;

    @BindView(R.id.IVlogout)
    ImageView IVlogout;

    @BindView(R.id.IVback)
    ImageView IVback;

    @BindView(R.id.RLhandrail)
    RelativeLayout RLhandrail;

    String userType;
    RegistationPojo mRegistationPojo;
    AppSession mAppSession;
    ProgressDialog progressDialog;


    double longitude;
    double latitude;

    LocationManager mLocationManager;
    private boolean isNetworkEnabled;
    private boolean isGpsEnabled;
    OperatorShowAlInfo operatorShowAlInfo;

    String[] listItems;

    public CitizenFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.citizen_home, container, false);

        ButterKnife.bind(this, view);
        mAppSession = mAppSession.getInstance(getActivity());
        getActivity().stopService(new Intent(getActivity(), ShortcutViewService.class));


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        initView();
        mAppSession.saveData("Chat","0");
        RLreportanincident.setOnClickListener(this);
        RLhandrail.setOnClickListener(this);
        RLnavigation.setOnClickListener(this);
        IVlogout.setOnClickListener(this);
        TVname.setOnClickListener(this);


        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission() && gpsEnabled()) {
            if (isNetworkEnabled) {

                progressDialog.show();
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                        10, mLocationListener);
            } else {
                progressDialog.show();
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        10, mLocationListener);
            }
        }


        return view;
    }

    private void initView() {
        IVlogout.setVisibility(View.VISIBLE);
        IVback.setVisibility(View.GONE);
        TVname.setText(mAppSession.getData("name"));
        if (mAppSession.getData("image_url") != null && !mAppSession.getData("image_url").equals("")) {
            Glide.with(getActivity()).load(mAppSession.getData("image_url")).into(IVprofilephoto);
         /*   Glide.with(this).load(mAppSession.getData("image_url"))
                    .error(R.mipmap.img_profile_photo).
                    .into(IVprofilephoto);*/
        } else {
            IVprofilephoto.setImageResource(R.mipmap.img_white_dot);
        }



        if (Utils.checkConnection(getActivity())) {
            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
            incdentSetPojo.setUser_id(mAppSession.getData("id"));
            incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
            incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
            incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
            incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            getAssigmentList(mFile);
        } else {
            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLreportanincident:
                Utils.fragmentCall(new IncidentFragment(mAppSession.getData("id")), getFragmentManager());
                mAppSession.saveData("operatorScreenBack", "1");
                mAppSession.saveData("handrail", "dasd");
                break;

            case R.id.RLhandrail:
                Utils.fragmentCall(new HandrailFragment(), getFragmentManager());
                mAppSession.saveData("handrail", "handrail");
                break;

            case R.id.RLnavigation:
                boolean isAppInstalledwaze = appInstalledOrNot("com.waze");
                boolean isAppInstalledgooglemap = appInstalledOrNot("com.google.android.apps.maps");

                if (isAppInstalledwaze == true && isAppInstalledgooglemap == true) {
                    listItems = getResources().getStringArray(R.array.select_map_google_waze);
                } else if (isAppInstalledgooglemap == true) {
                    listItems = getResources().getStringArray(R.array.select_map_google);
                } else if (isAppInstalledwaze == true) {
                    listItems = getResources().getStringArray(R.array.select_waze);
                }


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle(R.string.chooseanOption);
                mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("listitemes==", "" + listItems[i]);

                        if (listItems[i].equals("Google Maps")) {
                            dialogInterface.dismiss();
                            Intent LaunchIntent = getActivity().getPackageManager()
                                    .getLaunchIntentForPackage("com.google.android.apps.maps");
                            startActivity(LaunchIntent);
                        } else {
                            dialogInterface.dismiss();
                            Intent LaunchIntent = getActivity().getPackageManager()
                                    .getLaunchIntentForPackage("com.waze");
                            startActivity(LaunchIntent);
                        }


                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();


                // Utils.fragmentCall(new GPSPublicFragment(), getFragmentManager());
                break;

            case R.id.IVlogout:
                opendialogcustomdialog();
                break;

            case R.id.TVname:
                Utils.fragmentCall(new Frag_Public_Profile_Shown(operatorShowAlInfo), getFragmentManager());
                break;

        }
    }

    public void opendialogcustomdialog() {

        final Dialog dialog = new Dialog(getActivity(), R.style.DialogFragmentTheme);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        TextView TVrefuse = (TextView) dialog.findViewById(R.id.mTvNo);
        TextView TVallow = (TextView) dialog.findViewById(R.id.mTvYes);
        TextView TVcustomdescriptiontext = (TextView) dialog.findViewById(R.id.TVcustomdescriptiontext);
        TVcustomdescriptiontext.setText(getActivity().getString(R.string.logout_message));

        TVallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mAppSession.saveData("Login", "0");
                SharedPreferences preferences = getActivity().getSharedPreferences("copops.com.copopsApp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Utils.fragmentCall(new HomeFragment(), getFragmentManager());
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


    private void getAssigmentList(RequestBody Data) {


        progressDialog.show();
        Service operator = ApiUtils.getAPIService();
        Call<OperatorShowAlInfo> getallLatLong = operator.getOperotor(Data);
        getallLatLong.enqueue(new Callback<OperatorShowAlInfo>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<OperatorShowAlInfo> call, Response<OperatorShowAlInfo> response)

            {
                try {
                    if (response.body() != null) {
                        operatorShowAlInfo = response.body();
                        if (operatorShowAlInfo.getStatus().equals("false")) {
                            //  Utils.showAlert(registrationResponse.getMessage(), getActivity());

                        } else {

                            TVprofiledescription.setText(getString(R.string.sentinel)+" "+operatorShowAlInfo.getLevel());

                            TVprogressbarnumber.setText(operatorShowAlInfo.getReport());
                            Log.e("gettotalreports==", "" + operatorShowAlInfo.getTotal_reports());
                            if (operatorShowAlInfo.getTotal_reports().equals("1")) {
                                TVprogressbarreports.setText(operatorShowAlInfo.getTotal_reports() + " " + getString(R.string.report));
                            } else {
                                TVprogressbarreports.setText(operatorShowAlInfo.getTotal_reports() + " " + getString(R.string.reports));
                            }


                            progressBar1.setMax(100);
                            progressBar1.setProgress(Integer.valueOf(operatorShowAlInfo.getProfile_percent()));

                            TVprogresspercentage.setText(operatorShowAlInfo.getProfile_percent() + "%");

                        }
                        progressDialog.dismiss();

                    } else {
                        // Utils.showAlert(getString(R.string.Notfound), getActivity());
                        Utils.showAlert(getString(R.string.Notfound), getActivity());
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.getMessage();
                    Utils.showAlert(e.getMessage(), getActivity());
                }
            }

            @Override
            public void onFailure(Call<OperatorShowAlInfo> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());
                progressDialog.dismiss();
                Utils.showAlert(t.getMessage(), getActivity());
            }
        });
    }


    ////Manish
    private final android.location.LocationListener mLocationListener = new android.location.LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                // mCurrentLocation = location;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                progressDialog.dismiss();
                mAppSession.saveData("latitude", String.valueOf(latitude));
                mAppSession.saveData("longitude", String.valueOf(longitude));

                Log.e("latitude", String.valueOf(latitude));
                Log.e("longitude", String.valueOf(longitude));

                //  initMapFragment();
            } else {
                Toast.makeText(getActivity(), getString(R.string.Locationisnotavailablenow), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    private boolean checkPermission() {
        boolean check = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!check) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        }
        return true;
//    }
    }

    private boolean gpsEnabled() {
        isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            // displayLocationSettingsRequest(getActivity());
//    if (!isGpsEnabled) {
//            Toast.makeText(m_activity, "GPS is not enabled", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


}
