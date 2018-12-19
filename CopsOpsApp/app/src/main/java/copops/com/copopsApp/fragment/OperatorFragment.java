package copops.com.copopsApp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.adapter.AssignmentInsidentListAdapter;
import copops.com.copopsApp.pojo.AssignmentListPojo;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.OperatorShowAlInfo;
import copops.com.copopsApp.pojo.RegistationPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatorFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.IVprofilephoto)
    CircleImageView IVprofilephoto;

    @BindView(R.id.TVname)
    TextView TVname;

    @BindView(R.id.TVprogressbarnumber)
    TextView TVprogressbarnumber;

    @BindView(R.id.Tvavaiable)
    TextView Tvavaiable;

    @BindView(R.id.Tvnotavaiable)
    TextView Tvnotavaiable;


    @BindView(R.id.TVprogressbarreports)
    TextView TVprogressbarreports;


    @BindView(R.id.TVprofiledescription)
    TextView TVprofiledescription;

    @BindView(R.id.countId)
    TextView countId;

    @BindView(R.id.TVprogresspercentage)
    TextView TVprogresspercentage;


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

    @BindView(R.id.llintervention)
    LinearLayout llintervention;

    @BindView(R.id.RLpositionofincidents)
    RelativeLayout RLpositionofincidents;
    LocationManager mLocationManager;
    private boolean isNetworkEnabled;
    private boolean isGpsEnabled;
    String userType;
    RegistationPojo mRegistationPojo;

    ProgressDialog progressDialog;
    AppSession mAppSession;
    OperatorShowAlInfo operatorShowAlInfo;


    double longitude;
    double latitude;

    @BindView(R.id.viewnotavaiable)
    View viewnotavaiable;

    @BindView(R.id.viewavaiable)
    View viewavaiable;


    public OperatorFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

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


        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission() && gpsEnabled()) {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                        10, mLocationListener);
            } else {
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
            Glide.with(this).load(mAppSession.getData("image_url")).into(IVprofilephoto);
         /*   Glide.with(this).load(mAppSession.getData("image_url"))
                    .error(R.mipmap.img_profile_photo).
                    .into(IVprofilephoto);*/
        } else {
            IVprofilephoto.setImageResource(R.mipmap.img_profile_photo);
        }


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading...");


        if (Utils.checkConnection(getActivity())) {
            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
            incdentSetPojo.setUser_id(mAppSession.getData("id"));
            incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            getCopeStatus(mFile);
        } else {
            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLreportanincident:
                mAppSession.saveData("handrail", "dasd");
                if (operatorShowAlInfo.getGrade().equalsIgnoreCase("Grade II")) {
                    Utils.fragmentCall(new PositionOfInteervebtionsFragment(latitude, longitude), getFragmentManager());
                }else {
                    Utils.showAlert(getString(R.string.operator_grade_one_message), getActivity());
                }
                break;

            case R.id.RLpositionofincidents:
                mAppSession.saveData("handrail", "dasd");

                Utils.fragmentCall(new IncidentFragment(mAppSession.getData("id")), getFragmentManager());
                mAppSession.saveData("operatorScreenBack", "1");
                break;

            case R.id.RLnavigation:
                Utils.fragmentCall(new GPSPublicFragment(), getFragmentManager());
                break;

            case R.id.IVlogout:
                opendialogcustomdialog();
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
                    incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    getAssigmentList(mFile);
                } else {
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                }

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


    private void getCopeStatus(RequestBody Data) {
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

                            TVprofiledescription.setText(operatorShowAlInfo.getGrade());

                            TVprogressbarnumber.setText(operatorShowAlInfo.getReport());
                            TVprogressbarreports.setText(operatorShowAlInfo.getCompleted_reports() + " completed interventions");
                            TVprogresspercentage.setText(operatorShowAlInfo.getProfile_percent() + "%");

                            progressBar1.setMax(100);
                            progressBar1.setProgress(Integer.valueOf(operatorShowAlInfo.getProfile_percent()));

                            if (operatorShowAlInfo.getAvailable().equalsIgnoreCase("0")) {

                                Log.e("notavaiable==","getCopeStatus=="+operatorShowAlInfo.getAvailable());
                                Tvnotavaiable.setTextColor(getResources().getColor(R.color.blue_shade));
                                viewnotavaiable.setVisibility(View.VISIBLE);
                                viewavaiable.setVisibility(View.GONE);
                                Tvavaiable.setTextColor(getResources().getColor(R.color.black));

                            } else {

                                Log.e("avaiable==","getCopeStatus=="+operatorShowAlInfo.getAvailable());
                                Tvavaiable.setTextColor(getResources().getColor(R.color.blue_shade));
                                Tvnotavaiable.setTextColor(getResources().getColor(R.color.black));

                                viewnotavaiable.setVisibility(View.GONE);
                                viewavaiable.setVisibility(View.VISIBLE);

                                // Tvavaiable.setTextColor(getResources().getColor(R.color.blue_shade));

                            }

                            if (operatorShowAlInfo.getNew_reports().equalsIgnoreCase("0")) {
                                countId.setVisibility(View.INVISIBLE);

                            } else {
                                countId.setVisibility(View.VISIBLE);

                                countId.setText(operatorShowAlInfo.getNew_reports());
                            }

                        }
                        progressDialog.dismiss();

                    } else {
                        Utils.showAlert(response.message(), getActivity());
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


    private void getCopsAvailabilityStatus(RequestBody Data) {

        try {
            progressDialog.show();
            Service operator = ApiUtils.getAPIService();
            Call<CommanStatusPojo> getallLatLong = operator.getAvailability(Data);
            getallLatLong.enqueue(new Callback<CommanStatusPojo>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response)

                {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo commanStatusPojo = response.body();
                            if (commanStatusPojo.getStatus().equals("false")) {
                                Utils.showAlert(commanStatusPojo.getMessage(), getActivity());

                            } else {

                                if (commanStatusPojo.getAvailable().equalsIgnoreCase("0")) {


                            //        SpannableString spanString = new SpannableString("Not Avaiable");

                            //        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);

                            //        Tvnotavaiable.setText(spanString);
                                    // Tvnotavaiable.setTextColor(getResources().getColor(R.color.blue_shade));
                                    //  Tvavaiable.setTextColor(getResources().getColor(R.color.black));

                                    Log.e("notavaiable==","getCopsAvailabilityStatus=="+operatorShowAlInfo.getAvailable());

                                    Tvnotavaiable.setTextColor(getResources().getColor(R.color.blue_shade));
                                    viewnotavaiable.setVisibility(View.VISIBLE);
                                    viewavaiable.setVisibility(View.GONE);
                                    Tvavaiable.setTextColor(getResources().getColor(R.color.black));

                                    Utils.showAlert(commanStatusPojo.getMessage(), getActivity());


                                } else {


                                    Log.e("avaiable==","getCopsAvailabilityStatus=="+operatorShowAlInfo.getAvailable());
                                    Tvnotavaiable.setTextColor(getResources().getColor(R.color.black));
                                    viewnotavaiable.setVisibility(View.GONE);
                                    viewavaiable.setVisibility(View.VISIBLE);
                                    Tvavaiable.setTextColor(getResources().getColor(R.color.blue_shade));

                                 //   Tvnotavaiable.setTextColor(getResources().getColor(R.color.black));
                                 //   Tvavaiable.setTextColor(getResources().getColor(R.color.blue_shade));
                                    Utils.showAlert(commanStatusPojo.getMessage(), getActivity());
                                  //  Tvnotavaiable.setText("Not Avaiable");

                                }

                            }
                            progressDialog.dismiss();

                        } else {
                            Utils.showAlert(response.message(), getActivity());
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
        progressDialog.show();
        Service login = ApiUtils.getAPIService();
        Call<AssignmentListPojo> getallLatLong = login.getAssignmentList(Data);
        getallLatLong.enqueue(new Callback<AssignmentListPojo>() {
            @Override
            public void onResponse(Call<AssignmentListPojo> call, Response<AssignmentListPojo> response)

            {
                try {
                    if (response.body() != null) {
                        AssignmentListPojo assignmentListPojo = response.body();
                        if (assignmentListPojo.getStatus().equals("false")) {
                            Utils.showAlert(" No Assigned Intervention Found  so We can’t have access to the assigning an intervention", getActivity());
                        } else {
                            Utils.fragmentCall(new AssignmentTableFragment(assignmentListPojo), getFragmentManager());

                        }
                        progressDialog.dismiss();

                    } else {
                        Utils.showAlert(response.message(), getActivity());
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

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.checkConnection(getActivity())) {
            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
            incdentSetPojo.setUser_id(mAppSession.getData("id"));
            incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            getCopeStatus(mFile);
        } else {
            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
        }

    }


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


    ////Manish
    private final android.location.LocationListener mLocationListener = new android.location.LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                // mCurrentLocation = location;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
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
}
