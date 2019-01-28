package copops.com.copopsApp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.QBPushManager;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.chatmodule.App;
import copops.com.copopsApp.chatmodule.ui.activity.DialogsActivity;
import copops.com.copopsApp.chatmodule.utils.PushBroadcastReceiver;
import copops.com.copopsApp.chatmodule.utils.chat.ChatHelper;
import copops.com.copopsApp.chatmodule.utils.qb.QbChatDialogMessageListenerImp;
import copops.com.copopsApp.chatmodule.utils.qb.QbDialogHolder;
import copops.com.copopsApp.chatmodule.utils.qb.QbDialogUtils;
import copops.com.copopsApp.chatmodule.utils.qb.callback.QBPushSubscribeListenerImpl;
import copops.com.copopsApp.chatmodule.utils.qb.callback.QbEntityCallbackImpl;
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


    String[] listItems;

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
    LocationManager mLocationManager;
    private boolean isNetworkEnabled;
    private boolean isGpsEnabled;
    String userType;
    RegistationPojo mRegistationPojo;

    ProgressDialog progressDialog;
    AppSession mAppSession;
    OperatorShowAlInfo operatorShowAlInfo;

    AssignmentListPojo assignmentListPojo_close;
    double longitude;
    double latitude;
    ArrayList<QBUser> list;

    QBIncomingMessagesManager incomingMessagesManager;
    private QBUser currentUser;
    QBChatMessage qbChatMessage1;

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
        rlchat.setOnClickListener(this);

        buildUsersList();
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission() && gpsEnabled()) {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                        10, mLocationListener);
                progressDialog.show();
            } else {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        10, mLocationListener);
                progressDialog.show();
            }
        }
        return view;
    }


    private void initView() {
        IVlogout.setVisibility(View.VISIBLE);
        IVback.setVisibility(View.GONE);

        //   String abb=mAppSession.getData("messagecount");


        TVname.setText(mAppSession.getData("name"));
        if (mAppSession.getData("image_url") != null && !mAppSession.getData("image_url").equals("")) {
            Glide.with(getActivity()).load(mAppSession.getData("image_url")).into(IVprofilephoto);

//            Glide.with(getActivity()).load(mAppSession.getData("image_url")).diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model,
//                                                   Target<GlideDrawable> target, boolean isFirstResource) {
//                            e.printStackTrace();
//                            //progressBar.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model,
//                                                       Target<GlideDrawable> target, boolean isFromMemoryCache,
//                                                       boolean isFirstResource) {
//                           // progressBar.setVisibility(View.GONE);
//                            return false;
//                        }
//                    })
//                    .error(R.drawable.ic_error_white)
//                    .dontTransform()
//                    .into(IVprofilephoto);

            //   GlideApp.with(getActivity()).load(mAppSession.getData("image_url")).into(IVprofilephoto);


//
//            Glide.with(getActivity())
//                    .load(mAppSession.getData("image_url"))
//                    .apply(new RequestOptions().error(R.drawable.ic_error_white)).apply(new RequestOptions().override(Consts.PREFERRED_IMAGE_SIZE_FULL, Consts.PREFERRED_IMAGE_SIZE_FULL))
//                    .into(IVprofilephoto);


         /*   Glide.with(this).load(mAppSession.getData("image_url"))
                    .error(R.mipmap.img_profile_photo).
                    .into(IVprofilephoto);*/
        } else {
            IVprofilephoto.setImageResource(R.mipmap.img_white_dot);
        }


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));


//        if (Utils.checkConnection(getActivity())) {
//            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
//            incdentSetPojo.setUser_id(mAppSession.getData("id"));
//            incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
//            incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
//            incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
//            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
//            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
//            getCopeStatus(mFile);
//        } else {
//            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
//        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLreportanincident:
                mAppSession.saveData("handrail", "dasd");
                if (operatorShowAlInfo.getGrade().equalsIgnoreCase("Grade II")) {
                    Utils.fragmentCall(new PositionOfInteervebtionsFragment(latitude, longitude), getFragmentManager());
                }
                break;

            case R.id.RLpositionofincidents:
                mAppSession.saveData("handrail", "dasd");

                Utils.fragmentCall(new IncidentFragment(mAppSession.getData("id")), getFragmentManager());
                mAppSession.saveData("operatorScreenBack", "1");
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
                mBuilder.setTitle("Choose an Option");
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
                //  Utils.fragmentCall(new GPSPublicFragment(), getFragmentManager());
                break;

            case R.id.IVlogout:
                opendialogcustomdialog();
                break;

            case R.id.rlchat:
                Intent mIntent = new Intent(getActivity(), DialogsActivity.class);

                startActivity(mIntent);
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

//                if (operatorShowAlInfo.getAvailable().equalsIgnoreCase("0")) {
//                    if (Utils.checkConnection(getActivity())) {
//                        IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
//
//                        incdentSetPojo.setUser_id(mAppSession.getData("id"));
//                        incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
//                        Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
//                        RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
//                        getAssignIntervationData(mFile);
//                    } else {
//                        Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
//                    }
//
//
//                } else {
                if (Utils.checkConnection(getActivity())) {
                    IncdentSetPojo incdentSetPojo = new IncdentSetPojo();

                    incdentSetPojo.setUser_id(mAppSession.getData("id"));
                    incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
                    incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
                    //  incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    getAssigmentList(mFile);
                } else {
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                }

                //  }
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
                userLogout();
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
                            TVpsental.setText(operatorShowAlInfo.getLevel());
                            mAppSession.saveData("operatorgrade", operatorShowAlInfo.getGrade());
                            Log.e("getgrade==",""+mAppSession.getData("operatorgrade"));
                            TVprogressbarnumber.setText(operatorShowAlInfo.getReport());
                            TVprogressbarreports.setText(operatorShowAlInfo.getCompleted_reports() + " "+getString(R.string.completedinterventions));
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
                                    mAppSession.saveData("assignedintervationcount","0");
                                } else {
                                    countId.setVisibility(View.VISIBLE);
                                    picId.setVisibility(View.INVISIBLE);
                                    countId.setText(operatorShowAlInfo.getNew_reports());
                                    mAppSession.saveData("assignedintervationcount",operatorShowAlInfo.getNew_reports());
                                }
                            } else {
                                if (operatorShowAlInfo.getNew_reports().equalsIgnoreCase("0")) {
                                    countId.setVisibility(View.GONE);
                                    mAppSession.saveData("assignedintervationcount","0");
                                } else {
                                    countId.setVisibility(View.VISIBLE);
                                    picId.setVisibility(View.VISIBLE);
                                    countId.setText(operatorShowAlInfo.getNew_reports());
                                    mAppSession.saveData("assignedintervationcount",operatorShowAlInfo.getNew_reports());
                                }
                            }


                            TVprofiledescription.setText(operatorShowAlInfo.getGrade());

                            mAppSession.saveData("operatorlevel", operatorShowAlInfo.getLevel());


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
                                    // Tvavaiable.setTextColor(getResources().getColor(R.color.blue_shade));
                                    Utils.showAlert(commanStatusPojo.getMessage(), getActivity());
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

                            Utils.fragmentCall(new AssignmentTableFragment(), getFragmentManager());
                        } else {
                            Utils.fragmentCall(new AssignmentTableFragment(), getFragmentManager());

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

        String aa = mAppSession.getData("messagecount");

        if (mAppSession.getData("messagecount").equals("") || mAppSession.getData("messagecount").equals("0")) {
            chatCountId.setVisibility(View.GONE);
        } else {
            chatCountId.setVisibility(View.VISIBLE);
            chatCountId.setText(mAppSession.getData("messagecount"));
        }

        //loadUpdatedDialog(qbChatMessage1.getDialogId(),qbChatMessage1);

        if (Utils.checkConnection(getActivity())) {
            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
            incdentSetPojo.setUser_id(mAppSession.getData("id"));
            incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
            incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
            incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
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


    private void getAssignIntervationData(RequestBody Data) {
        try {


            progressDialog.show();
            Service acceptInterven = ApiUtils.getAPIService();
            Call<AssignmentListPojo> acceptIntervenpCall = acceptInterven.assignedData(Data);
            acceptIntervenpCall.enqueue(new Callback<AssignmentListPojo>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<AssignmentListPojo> call, Response<AssignmentListPojo> response)

                {
                    try {
                        if (response.body() != null) {
                            assignmentListPojo_close = response.body();
                            if (assignmentListPojo_close.getStatus().equals("false")) {
                                Utils.showAlert(assignmentListPojo_close.getMessage(), getActivity());
                            } else {
                                //  Utils.fragmentCall(new CloseIntervationReportFragment(assignmentListPojo_close), getFragmentManager());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            SubscribeService.unSubscribeFromPushes(getActivity());
        } else {
            logoutREST();
        }
    }

    private void logoutREST() {
        QBUsers.signOut().performAsync(null);
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


    private void buildUsersList() {
        //  ProgressDialogFragment.show(getActivity().getSupportFragmentManager());
        List<String> tags = new ArrayList<>();
        tags.add(App.getSampleConfigs().getUsersTag());
        QBUsers.getUsersByTags(tags, null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> result, Bundle params) {
                list = result;
                //   ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getLogin().equalsIgnoreCase(mAppSession.getData("user_id"))) {
                        QBUser user = list.get(i);
                        user.setPassword(App.getSampleConfigs().getUsersPassword());
                        //user.setPassword(mAppSession.getData("user_id"));
                        login(user);
                        break;
                    }
                }
            }

            @Override
            public void onError(QBResponseException e) {
                //    ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());
            }
        });
    }

    private void login(final QBUser user) {
        //  ProgressDialogFragment.show(getActivity().getSupportFragmentManager(), R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);
                //   ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());

                try {
                    currentUser = ChatHelper.getCurrentUser();
                    incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
                    incomingMessagesManager.addDialogMessageListener(new OperatorFragment.AllDialogsMessageListener());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(QBResponseException e) {
                //   ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());
            }
        });
    }


    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            Log.d("RanjanCheck", "processMessage");
            QBUser user = null;
            int sender = qbChatMessage.getSenderId();
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).getId().equals(sender)) {
//                     user = list.get(i);
//
//                    break;
//                }
//            }
            // createDialog(list);

            loadUpdatedDialog(qbChatMessage.getDialogId(), qbChatMessage);


        }
    }


    private void loadUpdatedDialog(String dialogId, QBChatMessage qbChatMessage) {
        ChatHelper.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle bundle) {
                //   isProcessingResultInProgress = false;
                QbDialogHolder.getInstance().addDialog(result);
                int count = getUnreadMsgCount(result);

                qbChatMessage1 = qbChatMessage;
                if (count == 0) {
                    chatCountId.setVisibility(View.GONE);
                    mAppSession.saveData("messagecount", "0");
                } else {
                    chatCountId.setVisibility(View.VISIBLE);
                    chatCountId.setText("" + count);
                    mAppSession.saveData("messagecount", "" + count);
                }

                if (qbChatMessage.getAttachments() != null) {
                    PushBroadcastReceiver.displayCustomNotificationForOrders(result.getName(), " " + "Attachment" + "  " + "(" + count + " message)", getActivity());
                } else{
                    PushBroadcastReceiver.displayCustomNotificationForOrders(result.getName(), " " + qbChatMessage.getBody() + "  " + "(" + count + " message)", getActivity());

            }
            }

            @Override
            public void onError(QBResponseException e) {

                e.printStackTrace();

            }
        });
    }

    public int getUnreadMsgCount(QBChatDialog chatDialog) {
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        if (unreadMessageCount == null) {
            return 0;
        } else {
            return unreadMessageCount;
        }
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
