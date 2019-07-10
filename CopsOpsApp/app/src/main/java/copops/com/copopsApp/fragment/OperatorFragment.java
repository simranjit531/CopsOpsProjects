package copops.com.copopsApp.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.SortedList;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;


import copops.com.copopsApp.activity.DashboardActivity;
import copops.com.copopsApp.pojo.AssignmentListPojo;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.OperatorShowAlInfo;
import copops.com.copopsApp.pojo.RegistationPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.shortcut.GPSTracker;

import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.TrackingServices;
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
public class OperatorFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.IVprofilephoto)
    CircleImageView IVprofilephoto;
    private String[] listItems;
    @BindView(R.id.TVname)
    TextView TVname;
    @BindView(R.id.TVprogressbarnumber)
    TextView TVprogressbarnumber;
    @BindView(R.id.Tvavaiable)
    TextView Tvavaiable;
    @BindView(R.id.chatCountId)
    TextView chatCountId;
    @BindView(R.id.Tvnotavaiable)
    TextView Tvnotavaiable;
    @BindView(R.id.TVprogressbarreports)
    TextView TVprogressbarreports;
    @BindView(R.id.TVprofiledescription)
    TextView TVprofiledescription;
    @BindView(R.id.TVpsental)
    TextView TVpsental;
    @BindView(R.id.countId)
    TextView countId;
    @BindView(R.id.Tvchattext)
    TextView Tvchattext;
    @BindView(R.id.TVprogresspercentage)
    TextView TVprogresspercentage;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;
    @BindView(R.id.RLreportanincident)
    RelativeLayout RLreportanincident;
    @BindView(R.id.RLnavigation)
    RelativeLayout RLnavigation;
    @BindView(R.id.rlchat)
    RelativeLayout rlchat;
    @BindView(R.id.IVlogout)
    ImageView IVlogout;
    @BindView(R.id.viewlineId)
    View viewlineId;
    @BindView(R.id.viewline2)
    View viewline2;
    @BindView(R.id.IVback)
    ImageView IVback;
    @BindView(R.id.picId)
    ImageView picId;
    @BindView(R.id.IVpositionofincidents)
    ImageView IVpositionofincidents;
    @BindView(R.id.llintervention)
    LinearLayout llintervention;
    @BindView(R.id.RLpositionofincidents)
    RelativeLayout RLpositionofincidents;
    private LocationManager mLocationManager;
    private boolean isNetworkEnabled;
    private boolean isGpsEnabled;
    private ProgressDialog progressDialog;
    private AppSession mAppSession;
    private OperatorShowAlInfo operatorShowAlInfo;
    private double longitude;
    private double latitude;

    GPSTracker gps;
    public static final int notify = 2000;  //interval between two services(Here Service run every 5 seconds)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    public OperatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.oprator_home, container, false);
        ButterKnife.bind(this, view);
        mAppSession = mAppSession.getInstance(getActivity());
        initView();
        RLreportanincident.setOnClickListener(this);
        RLpositionofincidents.setOnClickListener(this);
        RLnavigation.setOnClickListener(this);
        IVlogout.setOnClickListener(this);
        TVname.setOnClickListener(this);
        llintervention.setOnClickListener(this);
        Tvavaiable.setOnClickListener(this);
        Tvnotavaiable.setOnClickListener(this);
        rlchat.setOnClickListener(this);
        Utils.statusCheck(getActivity());
        gps = new GPSTracker(getActivity());
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        mAppSession.saveData("latitude", String.valueOf(latitude));
        mAppSession.saveData("longitude", String.valueOf(longitude));
        mAppSession.saveData("Chat","0");

        if (mAppSession.getData("devicelanguage").equals("Fr")) {
            Tvchattext.setText("     " + getString(R.string.messaging));
        } else {
            Tvchattext.setText(getString(R.string.messaging));
        }
        return view;
    }

    //initialization  View
    private void initView() {
        IVlogout.setVisibility(View.VISIBLE);
        IVback.setVisibility(View.GONE);
        getActivity().startService(new Intent(getActivity(), TrackingServices.class));
        TVname.setText(mAppSession.getData("name"));
        if (mAppSession.getData("image_url") != null && !mAppSession.getData("image_url").equals("")) {
            Glide.with(getActivity()).load(mAppSession.getData("image_url")).into(IVprofilephoto);
        } else {
            IVprofilephoto.setImageResource(R.mipmap.img_white_dot);
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLreportanincident:
                mAppSession.saveData("handrail", "dasd");
                try {
                    if (operatorShowAlInfo.getGrade().equalsIgnoreCase(" II")) {
                        Utils.fragmentCall(new PositionOfInteervebtionsFragment(latitude, longitude), getFragmentManager());

                        if (mTimer != null) // Cancel if already existed
                            mTimer.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.RLpositionofincidents:
                mAppSession.saveData("handrail", "dasd");
                Utils.fragmentCall(new IncidentFragment(mAppSession.getData("id")), getFragmentManager());
                mAppSession.saveData("operatorScreenBack", "1");
                if (mTimer != null) // Cancel if already existed
                    mTimer.cancel();
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
                mBuilder.setTitle(getString(R.string.chooseanOption));
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
                break;
            case R.id.IVlogout:
                opendialogcustomdialog();
                break;
            case R.id.rlchat:
               Utils.fragmentCall(new ChatRecentFragment(), getFragmentManager());
                break;

            case R.id.TVname:
                Utils.fragmentCall(new Frag_Public_Profile_Shown(operatorShowAlInfo), getFragmentManager());
                break;

            case R.id.Tvnotavaiable:
                if (Utils.checkConnection(getActivity())) {
                    IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                    incdentSetPojo.setUser_id(mAppSession.getData("id"));
                    incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                    incdentSetPojo.setAvailable("0");
                    incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    getCopsAvailabilityStatus(mFile);
                } else {
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                }
                break;

            case R.id.Tvavaiable:
                if (Utils.checkConnection(getActivity())) {
                    IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                    incdentSetPojo.setUser_id(mAppSession.getData("id"));
                    incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                    incdentSetPojo.setAvailable("1");
                    incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    getCopsAvailabilityStatus(mFile);
                } else {
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                }
                break;
            case R.id.llintervention:

                if (Utils.checkConnection(getActivity())) {
                    IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                    incdentSetPojo.setUser_id(mAppSession.getData("id"));
                    incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
                    incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
                    incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                    //  incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                  //  Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    getAssigmentList(mFile);
                } else {
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                }
                mAppSession.saveData("Chat","0");
                break;
        }
    }
//Show Popup For Logout
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


                if (Utils.checkConnection(getActivity())) {
                    IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                    incdentSetPojo.setUser_id(mAppSession.getData("id"));
                    incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    logOutData(mFile);
                } else {
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                }
                if (mTimer != null) // Cancel if already existed
                    mTimer.cancel();
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

    private void getCopeStatus(RequestBody Data) {

        progressDialog.show();
        Service operator = ApiUtils.getAPIService();
        Call<OperatorShowAlInfo> getallLatLong = operator.getOperotor(Data);
        getallLatLong.enqueue(new Callback<OperatorShowAlInfo>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<OperatorShowAlInfo> call, Response<OperatorShowAlInfo> response) {
                try {
                    if (response.body() != null) {
                        operatorShowAlInfo = response.body();

                        if (operatorShowAlInfo.getStatus().equals("false")) {
                        } else {
                            if (mAppSession.getData("new_reports").equalsIgnoreCase("")) {
                                mAppSession.saveData("new_reports",operatorShowAlInfo.getNew_reports());
                            } else {
                                mAppSession.saveData("new_reports",operatorShowAlInfo.getNew_reports());
                                mTimer = new Timer();   //recreate new
                                mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
                            }
                            mAppSession.saveData("notifica",operatorShowAlInfo.getTotalMessageCount());
                            TVpsental.setText(getString(R.string.sentinel)+" "+operatorShowAlInfo.getLevel());
                            mAppSession.saveData("operatorgrade", operatorShowAlInfo.getGrade());
                            Log.e("getgrade==", "" + mAppSession.getData("operatorgrade"));
                            TVprogressbarnumber.setText(operatorShowAlInfo.getReport());
                            TVprogressbarreports.setText(operatorShowAlInfo.getCompleted_reports() + " " + getString(R.string.completedinterventions));
                            TVprogresspercentage.setText(operatorShowAlInfo.getProfile_percent() + "%");
                            progressBar1.setMax(100);
                            progressBar1.setProgress(Integer.valueOf(operatorShowAlInfo.getProfile_percent()));
                            if (operatorShowAlInfo.getAvailable().equalsIgnoreCase("0")) {
                                Tvnotavaiable.setTextColor(getResources().getColor(R.color.sedocolor));
                                Tvavaiable.setTextColor(getResources().getColor(R.color.black));
                                viewline2.setBackgroundResource(R.color.sedocolor);
                                viewlineId.setBackgroundResource(R.color.blue_shade);
                                viewlineId.setVisibility(View.INVISIBLE);
                                viewline2.setVisibility(View.VISIBLE);
                            } else {
                                Tvavaiable.setTextColor(getResources().getColor(R.color.blue_shade));
                                Tvnotavaiable.setTextColor(getResources().getColor(R.color.black));
                                viewlineId.setBackgroundResource(R.color.blue_shade);
                                viewline2.setVisibility(View.INVISIBLE);
                                viewlineId.setVisibility(View.VISIBLE);
                                viewline2.setBackgroundResource(R.color.black);
                            }
                            if (operatorShowAlInfo.getAvailable().equalsIgnoreCase("1")) {
                                if (operatorShowAlInfo.getNew_reports().equalsIgnoreCase("0")) {
                                    countId.setVisibility(View.INVISIBLE);
                                    picId.setVisibility(View.INVISIBLE);
                                    mAppSession.saveData("assignedintervationcount", "0");
                                } else {
                                    countId.setVisibility(View.VISIBLE);
                                    picId.setVisibility(View.INVISIBLE);
                                    countId.setText(operatorShowAlInfo.getNew_reports());
                                    mAppSession.saveData("assignedintervationcount", operatorShowAlInfo.getNew_reports());
                                }
                            } else {
                                if (operatorShowAlInfo.getNew_reports().equalsIgnoreCase("0")) {
                                    countId.setVisibility(View.GONE);
                                    mAppSession.saveData("assignedintervationcount", "0");
                                } else {
                                    countId.setVisibility(View.VISIBLE);
                                    picId.setVisibility(View.VISIBLE);
                                    countId.setText(operatorShowAlInfo.getNew_reports());
                                    mAppSession.saveData("assignedintervationcount", operatorShowAlInfo.getNew_reports());
                                }
                            }
                            TVprofiledescription.setText(getString(R.string.grade)+" "+operatorShowAlInfo.getGrade());
                            mAppSession.saveData("operatorlevel", operatorShowAlInfo.getLevel());
                        }
                        progressDialog.dismiss();
                    } else {
                        Utils.showAlert(getString(R.string.Notfound), getActivity());
                        progressDialog.dismiss();
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.getMessage();
                    Utils.showAlert(e.getMessage(), getActivity());
                }
            }

            @Override
            public void onFailure(Call<OperatorShowAlInfo> call, Throwable t) {
                progressDialog.dismiss();
                Utils.showAlert(t.getMessage(), getActivity());
            }
        });
    }


    private void getCopsAvailabilityStatus(RequestBody Data) {
        try {
            Service operator = ApiUtils.getAPIService();
            Call<CommanStatusPojo> getallLatLong = operator.getAvailability(Data);
            getallLatLong.enqueue(new Callback<CommanStatusPojo>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response) {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo commanStatusPojo = response.body();
                            if (commanStatusPojo.getStatus().equals("false")) {
                                Utils.showAlert(commanStatusPojo.getMessage(), getActivity());
                            } else {

                                if (commanStatusPojo.getAvailable().equalsIgnoreCase("0")) {
                                    Tvnotavaiable.setTextColor(getResources().getColor(R.color.sedocolor));
                                    Tvavaiable.setTextColor(getResources().getColor(R.color.black));
                                    viewline2.setBackgroundResource(R.color.sedocolor);
                                    viewlineId.setBackgroundResource(R.color.blue_shade);
                                    viewlineId.setVisibility(View.INVISIBLE);
                                    viewline2.setVisibility(View.VISIBLE);
                                    Utils.showAlert(commanStatusPojo.getMessage(), getActivity());
                                } else {
                                    Tvavaiable.setTextColor(getResources().getColor(R.color.blue_shade));
                                    Tvnotavaiable.setTextColor(getResources().getColor(R.color.black));
                                    viewlineId.setBackgroundResource(R.color.blue_shade);
                                    viewline2.setVisibility(View.INVISIBLE);
                                    viewlineId.setVisibility(View.VISIBLE);
                                    viewline2.setBackgroundResource(R.color.black);
                                    Utils.showAlert(commanStatusPojo.getMessage(), getActivity());
                                }

                            }
                            progressDialog.dismiss();

                        } else {
                            Utils.showAlert(getString(R.string.Notfound), getActivity());
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.getMessage();
                        Utils.showAlert(e.getMessage(), getActivity());
                    }
                }

                @Override
                public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
                    Log.d("TAG", "Error " + t.getMessage());
                    progressDialog.dismiss();
                    Utils.showAlert(t.getMessage(), getActivity());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getAssigmentList(RequestBody Data) {
        Service login = ApiUtils.getAPIService();
        Call<AssignmentListPojo> getallLatLong = login.getAssignmentList(Data);
        getallLatLong.enqueue(new Callback<AssignmentListPojo>() {
            @Override
            public void onResponse(Call<AssignmentListPojo> call, Response<AssignmentListPojo> response) {
                try {
                    if (response.body() != null) {
                        AssignmentListPojo assignmentListPojo = response.body();
                        if (assignmentListPojo.getStatus().equals("false")) {

                            Utils.fragmentCall(new AssignmentTableFragment(), getFragmentManager());
                        } else {
                            Utils.fragmentCall(new AssignmentTableFragment(), getFragmentManager());

                        }
                        progressDialog.dismiss();

                    } else {
                        Utils.showAlert(getString(R.string.Notfound), getActivity());
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.getMessage();
                    Utils.showAlert(e.getMessage(), getActivity());
                }
            }

            @Override
            public void onFailure(Call<AssignmentListPojo> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());
                progressDialog.dismiss();
                Utils.showAlert(t.getMessage(), getActivity());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();

        String aa = mAppSession.getData("countNoti");

        if (mAppSession.getData("countNoti").equals("") || mAppSession.getData("countNoti").equals("0")) {
            chatCountId.setVisibility(View.GONE);
        } else {
            chatCountId.setVisibility(View.VISIBLE);
            chatCountId.setText(mAppSession.getData("countNoti"));
        }
        if (Utils.checkConnection(getActivity())) {
            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
            incdentSetPojo.setUser_id(mAppSession.getData("id"));
            incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
            incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
            incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
            incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
            incdentSetPojo.setFcm_token(mAppSession.getData("fcm_token"));
            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            getCopeStatus(mFile);
        } else {
            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
        }

    }






    ////For Use Location
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
                Toast.makeText(getActivity(), "Location is not available now", Toast.LENGTH_LONG).show();
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


    /**
     * for logout CITIZEN AND OPERATOR use this methed
     * @param Data= user id
     */
    private void logOutData(RequestBody Data) {
        try {
            progressDialog.show();
            Service acceptInterven = ApiUtils.getAPIService();
            Call<CommanStatusPojo> acceptIntervenpCall = acceptInterven.logout(Data);
            acceptIntervenpCall.enqueue(new Callback<CommanStatusPojo>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response) {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo  logOutCommanStatusPojo = response.body();
                            if (logOutCommanStatusPojo.getStatus().equals("false")) {
                                Utils.showAlert(logOutCommanStatusPojo.getMessage(), getActivity());
                            } else {
                                SharedPreferences preferences = getActivity().getSharedPreferences("copops.com.copopsApp", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                                mAppSession.saveData("Login", "0");
                                Utils.fragmentCall(new HomeFragment(), getFragmentManager());
                            }
                            progressDialog.dismiss();

                        } else {
                            Utils.showAlert(getString(R.string.Notfound), getActivity());
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.getMessage();
                        Utils.showAlert(e.getMessage(), getActivity());
                    }
                }

                @Override
                public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
                   // Log.d("TAG", "Error " + t.getMessage());
                    progressDialog.dismiss();
                    Utils.showAlert(t.getMessage(), getActivity());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//Check APP is install Or Not
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
//Update Notification Come Or NOT
    public void update() {


        if (mAppSession.getData("notifica").equals("") || mAppSession.getData("notifica").equals("0")) {
            chatCountId.setVisibility(View.GONE);
        } else {
            chatCountId.setVisibility(View.VISIBLE);
            chatCountId.setText(mAppSession.getData("notifica"));
        }

        if (mAppSession.getData("new_reports").equalsIgnoreCase("")) {
            countId.setVisibility(View.INVISIBLE);
            picId.setVisibility(View.INVISIBLE);
        } else {
            if (operatorShowAlInfo.getAvailable().equalsIgnoreCase("1")) {


                if(mAppSession.getData("new_reports").equalsIgnoreCase("0")){
                    countId.setVisibility(View.INVISIBLE);
                }else{
                    countId.setVisibility(View.VISIBLE);
                    countId.setText(mAppSession.getData("new_reports"));

                }

                picId.setVisibility(View.INVISIBLE);
            } else {
                if(mAppSession.getData("new_reports").equalsIgnoreCase("0")){
                    countId.setVisibility(View.INVISIBLE);
                }else{
                    countId.setVisibility(View.VISIBLE);
                    countId.setText(mAppSession.getData("new_reports"));
                }
                picId.setVisibility(View.VISIBLE);
            }
        }


    }


    //class TimeDisplay for handling task
    public class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    update();

                }
            });

        }

    }
}

