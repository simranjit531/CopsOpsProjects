package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.gcm.QBGcmPushInstanceIDService;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
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
import copops.com.copopsApp.activity.DashboardActivity;
import copops.com.copopsApp.chatmodule.App;
import copops.com.copopsApp.chatmodule.utils.PushBroadcastReceiver;
import copops.com.copopsApp.chatmodule.utils.chat.ChatHelper;
import copops.com.copopsApp.chatmodule.utils.qb.QbChatDialogMessageListenerImp;
import copops.com.copopsApp.chatmodule.utils.qb.QbDialogHolder;
import copops.com.copopsApp.chatmodule.utils.qb.callback.QbEntityCallbackImpl;
import copops.com.copopsApp.pojo.CommanStatusPojo;
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
    QBIncomingMessagesManager incomingMessagesManager;
    private QBUser currentUser;
    ArrayList<QBUser> list;
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
        gps = new GPSTracker(getActivity());
        latitude=gps.getLatitude();
        longitude=gps.getLongitude();

        mAppSession.saveData("latitude",String.valueOf(latitude));
        mAppSession.saveData("longitude",String.valueOf(longitude));
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("loading...");

        String sdadsa = mAppSession.getData("freez");


        if (userType.equalsIgnoreCase("citizen")) {
            userTypeRegistation = "Citizen";
            mAppSession.saveData("type",userTypeRegistation);

        } else {
            userTypeRegistation = "Cops";
            mAppSession.saveData("type",userTypeRegistation);
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

        if(mAppSession.getData("fcm_token").equalsIgnoreCase("")) {
            Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());

            mAppSession.saveData("fcm_token", FirebaseInstanceId.getInstance().getToken());
        }
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

                                    if(userType.equalsIgnoreCase("Citizen")) {
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

                                            mAppSession.saveData("freez","1");
//                                            if (userType.equalsIgnoreCase("Citizen")) {
//                                                Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
//                                            } else {
//                                                Utils.fragmentCall(new OperatorFragment(), getFragmentManager());
//                                            }
                                        }
                                        getActivity().startService(new Intent(getActivity(),TrackingServices.class));
                                    }else{
                                        mAppSession.saveData("freez","1");
                                        mAppSession.saveData("Login", "1");
                                        mAppSession.saveData("id", registrationResponse.getId());
                                        mAppSession.saveData("name", registrationResponse.getUsername());
                                        mAppSession.saveData("userType", userType);
                                        mAppSession.saveData("user_id", registrationResponse.getUserid());
                                        mAppSession.saveData("image_url", registrationResponse.getProfile_url());
                                        mAppSession.saveData("profile_qrcode", registrationResponse.getProfile_qrcode());
                                        mAppSession.saveData("grade", registrationResponse.getGrade());


                                        getActivity().startService(new Intent(getActivity(),TrackingServices.class));


//                                        Intent alarm = new Intent(getActivity(), BackgroundBroadCast.class);
//                                        getActivity().sendBroadcast(alarm);
                                        buildUsersList();
                                    }
                                }
                                progressDialog.dismiss();

                            }else{
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
            }catch (Exception e){
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





    private void buildUsersList() {

       ProgressDialogFragment.show(getActivity().getSupportFragmentManager());
        List<String> tags = new ArrayList<>();
        tags.add(App.getSampleConfigs().getUsersTag());

        QBUsers.getUsersByTags(tags, null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> result, Bundle params) {
                ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());
                list = result;
                String aaa= mAppSession.getData("user_id");
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
                ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());
            }
        });
    }

    private void login(final QBUser user) {
       ProgressDialogFragment.show(getActivity().getSupportFragmentManager(), R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);
                ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());
                currentUser = ChatHelper.getCurrentUser();


                incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();


                incomingMessagesManager.addDialogMessageListener(new AllDialogsMessageListener());
                Utils.fragmentCall(new OperatorFragment(), getFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());
            }
        });
    }



    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            Log.d("RanjanCheck", "processMessage");


            Log.d("RanjanCheck", "processMessage");


            QBUser user=null;
            int sender= qbChatMessage.getSenderId();
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).getId().equals(sender)) {
//                    user = list.get(i);
//
//                    break;
//                }
//            }

            loadUpdatedDialog(qbChatMessage.getDialogId(),qbChatMessage);


          //  PushBroadcastReceiver.displayCustomNotificationForOrders(user.getFullName(), " "+qbChatMessage.getBody(), getActivity());
         //   PushBroadcastReceiver.displayCustomNotificationForOrders("COPOPS", " "+qbChatMessage.getBody(), getActivity());
        }
    }




    private void loadUpdatedDialog(String dialogId,QBChatMessage qbChatMessage) {
        ChatHelper.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle bundle) {
                //   isProcessingResultInProgress = false;
                QbDialogHolder.getInstance().addDialog(result);
                int count= getUnreadMsgCount(result);


                if (count == 0) {

                    mAppSession.saveData("messagecount", "0");
                } else {
                    mAppSession.saveData("messagecount", "" + count);
                }


                if (qbChatMessage.getAttachments().size()==0) {
                    PushBroadcastReceiver.displayCustomNotificationForOrders(result.getName(), " " + qbChatMessage.getBody() + "  " + "(" + count + " message)", getActivity());

                } else{
                    PushBroadcastReceiver.displayCustomNotificationForOrders(result.getName(), " " + "Attachment" + "  " + "(" + count + " message)", getActivity());

                }
              //  PushBroadcastReceiver.displayCustomNotificationForOrders(result.getName(), " "+qbChatMessage.getBody()+"  "+"("+count+" message)", getActivity());

            }

            @Override
            public void onError(QBResponseException e) {

                e.printStackTrace();

            }
        });
    }

    public int getUnreadMsgCount(QBChatDialog chatDialog){
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        if (unreadMessageCount == null) {
            return 0;
        } else {
            return unreadMessageCount;
        }
    }




    ////Manish
    private final android.location.LocationListener mLocationListener = new android.location.LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                // mCurrentLocation = location;
                latitude=location.getLatitude();
                longitude=location.getLongitude();

                mAppSession.saveData("latitude",String.valueOf(latitude));
                mAppSession.saveData("longitude",String.valueOf(longitude));

                progressDialog.dismiss();
                //  initMapFragment();
            } else {
                Toast.makeText(getActivity(), "Location is not available now", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
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

    public void startService(View view) {

    }


}
