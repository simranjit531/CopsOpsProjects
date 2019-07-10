package copops.com.copopsApp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;

import copops.com.copopsApp.fragment.AssignmentTableFragment;
import copops.com.copopsApp.pojo.AssignmentListPojo;

import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.shortcut.ShortcutViewService;
import copops.com.copopsApp.shortcut.ShortcutViewService_Citizen;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ranjan Gupta
 */
public class AssigenedIntervation extends AppCompatActivity implements Utils.clossPassInterFace {
    private Context mContext;
    private int pos;
    AssignmentListPojo assignmentListPojo;
    @BindView(R.id.Tvdate)
    TextView Tvdate;
    @BindView(R.id.Tvtime)
    TextView Tvtime;
    @BindView(R.id.Tvstate)
    TextView Tvstate;
    @BindView(R.id.desHeaderTv)
    TextView desHeaderTv;
    @BindView(R.id.TVreferencenumber)
    TextView TVreferencenumber;
    @BindView(R.id.tvid)
    TextView tvid;
    @BindView(R.id.otherHeaderTv)
    TextView otherHeaderTv;

    @BindView(R.id.etAddressId)
    EditText etAddressId;
    @BindView(R.id.objectId)
    EditText objectId;
    @BindView(R.id.otherdescId)
    EditText otherdescId;
    @BindView(R.id.descId)
    EditText descId;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;
    @BindView(R.id.Rlintervenue)
    RelativeLayout Rlintervenue;

    private String intervationId = "";
    private String intervationDateTime = "";
    private String intervationAddress = "";
    private String intervationObject = "";
    private String intervationDescp = "";
    private String intervationOthDescp = "";
    private String intervationStatus = "";
    private String intervationRef = "";

    private ProgressDialog progressDialog;
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private Utils.clossPassInterFace mClossPassInterFace;
    private AppSession mAppSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_intervation);
        mContext = AssigenedIntervation.this;
        ButterKnife.bind(this);
        mAppSession = mAppSession.getInstance(getApplicationContext());
        NotificationManager notificationManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getString(R.string.loading));
        mainThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopService(new Intent(mContext, ShortcutViewService.class));

                stopService(new Intent(mContext, ShortcutViewService_Citizen.class));
            }
        }, 1000);
        Intent intent = getIntent();
        String value = intent.getStringExtra("remMsg");
        setReomteMsg(value);
        mClossPassInterFace = this;

        if (mAppSession.getData("copstatus").equalsIgnoreCase("0")) {
            Rlintervenue.setVisibility(View.VISIBLE);
            tvid.setText(getString(R.string.close));

        }
        Rlintervenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvid.getText().toString().equalsIgnoreCase(getString(R.string.Intervene))) {

                    if (Utils.checkConnection(mContext)) {
                        IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                        incdentSetPojo.setUser_id(mAppSession.getData("id"));
                        incdentSetPojo.setComment(descId.getText().toString().trim());
                        incdentSetPojo.setIncident_id(intervationId);
                        incdentSetPojo.setDevice_id(Utils.getDeviceId(mContext));
                        incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                        Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                        RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                        getAssignIntervation(mFile);
                    } else {
                        Utils.showAlert(mContext.getString(R.string.internet_conection), mContext);
                    }
                } else {
                    try {
                        mAppSession.saveData("notifictaionmove", "1");
                        startActivity(new Intent(mContext, DashboardActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void setReomteMsg(String value) {


        try {
            JSONObject jsonObject = new JSONObject(value);
            String str_incident = jsonObject.getString("incident");
            JSONObject jsonObject1 = new JSONObject(str_incident);
            intervationId = jsonObject1.getString("id");
            intervationDateTime = jsonObject1.getString("created_at");
            intervationAddress = jsonObject1.getString("address");
            intervationObject = jsonObject1.getString("sub_category_name");
            intervationDescp = jsonObject1.getString("incident_description");
            intervationOthDescp = jsonObject1.getString("other_description");
            intervationStatus = jsonObject1.getString("status");
            intervationRef = jsonObject1.getString("reference");
            otherdescId.setText("" + intervationOthDescp);
            TVreferencenumber.setText("" + intervationRef);
            etAddressId.setText("" + intervationAddress);
            objectId.setText("" + intervationObject);
            descId.setText("" + intervationDescp);
            if (Utils.checkConnection(this)) {
                IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                incdentSetPojo.setUser_id(mAppSession.getData("id"));
                incdentSetPojo.setIncident_id(intervationId);
                incdentSetPojo.setDevice_id(Utils.getDeviceId(this));
                incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                getUpdate(mFile);
            } else {
                Utils.showAlert(this.getString(R.string.internet_conection), this);
            }
            String[] parts = intervationDateTime.split(" ");
            String date = parts[0]; // 004
            String time = parts[1]; // 034556
            Tvdate.setText("" + date);
            Tvtime.setText("" + time);
            if (intervationStatus.equals("1")) {
                Tvstate.setText(R.string.onwait);
                Tvstate.setTextColor(getResources().getColor(R.color.orange));
            } else if (intervationStatus.equals("2")) {
                Tvstate.setText(R.string.pending);
                Tvstate.setTextColor(getResources().getColor(R.color.btntextcolort));
            } else if (intervationStatus.equals("3")) {
                Tvstate.setText(R.string.Assigned);
                Tvstate.setTextColor(getResources().getColor(R.color.black));
            } else {
                Tvstate.setText(R.string.finished);
                Tvstate.setTextColor(getResources().getColor(R.color.green));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clickBack(View v) {
        startActivity(new Intent(mContext, DashboardActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopService(new Intent(mContext, ShortcutViewService.class));
        stopService(new Intent(mContext, ShortcutViewService_Citizen.class));
    }

    private void getAssignIntervation(RequestBody Data) {
        progressDialog.show();
        Service acceptInterven = ApiUtils.getAPIService();
        Call<CommanStatusPojo> acceptIntervenpCall = acceptInterven.acceptInterven(Data);
        acceptIntervenpCall.enqueue(new Callback<CommanStatusPojo>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response) {
                try {
                    if (response.body() != null) {
                        CommanStatusPojo commanStatusPojo = response.body();
                        if (commanStatusPojo.getStatus().equals("false")) {
                            Utils.opendialogcustomdialogClose(mContext, commanStatusPojo.getMessage(), mClossPassInterFace);
                        } else {

                            if (commanStatusPojo.getMessage().equalsIgnoreCase(getString(R.string.youcan))) {
                                Utils.showAlert(commanStatusPojo.getMessage(), mContext);
                            } else {
                                Utils.opendialogcustomdialogClose(mContext, commanStatusPojo.getMessage(), mClossPassInterFace);
                            }
                        }
                        progressDialog.dismiss();

                    } else {
                        Utils.showAlert(getString(R.string.Notfound), mContext);
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.getMessage();
                    Utils.showAlert(e.getMessage(), mContext);
                }
            }
            @Override
            public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());
                progressDialog.dismiss();
                Utils.showAlert(t.getMessage(), mContext);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext, DashboardActivity.class));
        finish();
    }
    @Override
    public void onClick() {
        try {
            mAppSession.saveData("notifictaionmove", "1");
            startActivity(new Intent(mContext, DashboardActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("ResourceAsColor")
    private void getUpdate(RequestBody Data) {
        try {
            Service operator = ApiUtils.getAPIService();
            Call<CommanStatusPojo> getallLatLong = operator.getupdate(Data);
            getallLatLong.enqueue(new Callback<CommanStatusPojo>() {
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response) {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo commanStatusPojo = response.body();
                            if (commanStatusPojo.getStatus().equals("false")) {
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
                    Log.d("TAG", "Error " + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
