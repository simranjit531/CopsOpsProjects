package copops.com.copopsApp.fragment;



import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.location.LocationManager;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;


import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;

import copops.com.copopsApp.pojo.LoginPojoSetData;
import copops.com.copopsApp.pojo.RegistationPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.shortcut.GPSTracker;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.TrackingServices;
import copops.com.copopsApp.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class LoginFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.Tvregister)
    TextView Tvregister;

    @BindView(R.id.Tvforgot)
    TextView tvFargatPass;

    @BindView(R.id.ETemail)
    EditText etEmail;

    @BindView(R.id.ETpassword)
    EditText etPassword;


    @BindView(R.id.RLlogin)
    RelativeLayout rLlogin;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    Context mContext;
    ProgressDialog progressDialog;
    String userType;

    String userTypeRegistation;
    AppSession mAppSession;

    double longitude;
    double latitude;

    GPSTracker gps;

    LocationManager mLocationManager;
    private boolean isNetworkEnabled;
    private boolean isGpsEnabled;

    public LoginFragment(String userType) {

        this.userType = userType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_operater, container, false);
        ButterKnife.bind(this, view);
        onClick();
        mContext = getActivity();
        mAppSession = mAppSession.getInstance(mContext);
        mAppSession.saveData("countNoti","0");
        gps =  new GPSTracker(getContext());
        if (gps!=null)
        {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        mAppSession.saveData("latitude", String.valueOf(latitude));
        mAppSession.saveData("longitude", String.valueOf(longitude));
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("loading...");
        String sdadsa = mAppSession.getData("freez");
        if (userType.equalsIgnoreCase("citizen")) {
            userTypeRegistation = "Citizen";
            mAppSession.saveData("type", userTypeRegistation);

        } else {
            userTypeRegistation = "Cops";
            mAppSession.saveData("type", userTypeRegistation);
            Log.d("FCMToken", "token "+ FirebaseInstanceId.getInstance().getToken());
            mAppSession.saveData("fcm_token",FirebaseInstanceId.getInstance().getToken());
        }




//
//        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        if (checkPermission() && gpsEnabled()) {
//            if (isNetworkEnabled) {
//                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
//                        10, mLocationListener);
//            } else {
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
//                        10, mLocationListener);
//            }
//        }
        //    progressDialog.show();
//        String SDSADSAD= mAppSession.getData("fcm_token");
//        if (mAppSession.getData("fcm_token").equalsIgnoreCase("")) {
//            Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());
//
//            mAppSession.saveData("fcm_token", FirebaseInstanceId.getInstance().getToken());
//        }
        return view;
    }

    private void onClick() {
        Rltoolbar.setOnClickListener(this);
        rLlogin.setOnClickListener(this);
        Tvregister.setOnClickListener(this);
        tvFargatPass.setOnClickListener(this);
        //   etEmail.setCursorVisible(false);

        etEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail.setCursorVisible(true);
            }
        });
    }

    public void validation() {
        if (etEmail.getText().toString().equals("")) {
            Utils.showAlert(getActivity().getString(R.string.email), mContext);
        } else if (etPassword.getText().toString().equals("")) {
            Utils.showAlert(getActivity().getString(R.string.password), mContext);
        } else if (!Utils.isValidMail(etEmail.getText().toString())) {
            Utils.showAlert(getActivity().getString(R.string.valid_email_errer), mContext);
        } else {

            try {
                Utils.hideKeyboard(getActivity());
                LoginPojoSetData loginPojoSetData = new LoginPojoSetData();
                loginPojoSetData.setEmail_id(etEmail.getText().toString().trim());
                loginPojoSetData.setUser_password(etPassword.getText().toString());
                loginPojoSetData.setRef_user_type_id(userTypeRegistation);
                loginPojoSetData.setDevice_id(Utils.getDeviceId(mContext));
                loginPojoSetData.setIncident_lat(mAppSession.getData("latitude"));
                loginPojoSetData.setIncident_lng(mAppSession.getData("longitude"));
                loginPojoSetData.setdevice_language(mAppSession.getData("devicelanguage"));
                loginPojoSetData.setFcm_token(mAppSession.getData("fcm_token"));
                Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(loginPojoSetData)));
                RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(loginPojoSetData)));
                Service login = ApiUtils.getAPIService();
                Call<RegistationPojo> fileUpload = login.userLogin(mFile);
                fileUpload.enqueue(new Callback<RegistationPojo>() {
                    @Override
                    public void onResponse(Call<RegistationPojo> call, Response<RegistationPojo> response)
                    {
                        try {
                            if (response.body() != null) {
                                RegistationPojo registrationResponse = response.body();
                                if (registrationResponse.getStatus().equals("false")) {
                                    Utils.showAlert(registrationResponse.getMessage(), mContext);
                                } else {
                                    if (userType.equalsIgnoreCase("Citizen")) {
                                        if (registrationResponse.getVerified().equals("0")) {
                                            Utils.fragmentCall(new AuthenticateCodeFragment(userType, registrationResponse), getFragmentManager());
                                        } else {
                                            mAppSession.saveData("Login", "1");
                                            mAppSession.saveData("id", registrationResponse.getId());
                                            mAppSession.saveData("user_id", registrationResponse.getUserid());
                                            mAppSession.saveData("name", registrationResponse.getUsername());
                                            mAppSession.saveData("userType", userType);
                                            mAppSession.saveData("image_url", registrationResponse.getProfile_url());
                                            mAppSession.saveData("profile_qrcode", registrationResponse.getProfile_qrcode());
                                            mAppSession.saveData("grade", registrationResponse.getGrade());
                                            Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
                                            mAppSession.saveData("freez", "1");
//                                            if (userType.equalsIgnoreCase("Citizen")) {
//                                                Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
//                                            } else {
//                                                Utils.fragmentCall(new OperatorFragment(), getFragmentManager());
//                                            }
                                        }
                                        getActivity().startService(new Intent(getActivity(), TrackingServices.class));
                                    } else {
                                        mAppSession.saveData("freez", "1");
                                        mAppSession.saveData("Login", "1");
                                        mAppSession.saveData("id", registrationResponse.getId());
                                        mAppSession.saveData("name", registrationResponse.getUsername());
                                        mAppSession.saveData("userType", userType);
                                        mAppSession.saveData("user_id", registrationResponse.getUserid());
                                        mAppSession.saveData("image_url", registrationResponse.getProfile_url());
                                        mAppSession.saveData("profile_qrcode", registrationResponse.getProfile_qrcode());
                                        mAppSession.saveData("grade", registrationResponse.getGrade());
                                        getActivity().startService(new Intent(getActivity(), TrackingServices.class));
//                                        Intent alarm = new Intent(getActivity(), BackgroundBroadCast.class);
//                                        getActivity().sendBroadcast(alarm);
                                      //  buildUsersList();

                                        Utils.fragmentCall(new OperatorFragment(), getFragmentManager());
                                    }
                                }
                                progressDialog.dismiss();

                            } else {
                                Utils.showAlert(response.message(), mContext);
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Utils.showAlert(e.getMessage(), mContext);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistationPojo> call, Throwable t) {
                        Log.d("TAG", "Error " + t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Utils.showAlert(e.getMessage(), mContext);
            }
        }

    }

    /////All click listners
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Rltoolbar:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
                break;

            case R.id.RLlogin:
                if (Utils.checkConnection(mContext))
                    validation();
                else
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), mContext);
                break;
            case R.id.Tvregister:
                Utils.fragmentCall(new RegistationFragment(userType), getFragmentManager());
                break;
            case R.id.Tvforgot:
                Utils.fragmentCall(new ResetPasswordFragment(userType), getFragmentManager());
                break;
        }
    }



















}
