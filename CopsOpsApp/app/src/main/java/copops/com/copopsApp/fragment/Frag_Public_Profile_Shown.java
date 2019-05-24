package copops.com.copopsApp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;

import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.LoginPojoSetData;
import copops.com.copopsApp.pojo.OperatorShowAlInfo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class Frag_Public_Profile_Shown extends Fragment implements View.OnClickListener, Utils.clossPassInterFace {
    Utils.clossPassInterFace mClossPassInterFace;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;


    @BindView(R.id.TVname)
    TextView TVname;

    @BindView(R.id.TVprogressbarnumber)
    TextView TVprogressbarnumber;


    ProgressDialog progressDialog;

    @BindView(R.id.RLfaq)
    RelativeLayout RLfaq;

    @BindView(R.id.RLmedical)
    RelativeLayout RLmedical;
    @BindView(R.id.RLprofile)
    RelativeLayout RLprofile;


    @BindView(R.id.TVprogressbarreports)
    TextView TVprogressbarreports;

    @BindView(R.id.TVprofiledescription)
    TextView TVprofiledescription;


    @BindView(R.id.grade)
    TextView grade;

    @BindView(R.id.TVprogresspercentage)
    TextView TVprogresspercentage;
    @BindView(R.id.IVqrcode)
    ImageView IVqrcode;

    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    OperatorShowAlInfo operatorShowAlInfo;

    AppSession mAppSession;

    public Frag_Public_Profile_Shown(OperatorShowAlInfo operatorShowAlInfo) {

        this.operatorShowAlInfo = operatorShowAlInfo;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_public_profile, container, false);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));

        mClossPassInterFace = this;
        mAppSession = mAppSession.getInstance(getActivity());
        Rltoolbar.setOnClickListener(this);
        TVname.setText(mAppSession.getData("name"));
        RLmedical.setOnClickListener(this);
        RLprofile.setOnClickListener(this);
        TVprogressbarnumber.setText(operatorShowAlInfo.getReport());

        if (mAppSession.getData("userType").equalsIgnoreCase("citizen")) {
            grade.setVisibility(View.GONE);


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


        } else {
            grade.setVisibility(View.VISIBLE);

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

            grade.setText(getString(R.string.grade) + " " + operatorShowAlInfo.getGrade());
        }


        TVprofiledescription.setText(getString(R.string.sentinel) + " " + operatorShowAlInfo.getLevel());
        TVprogressbarreports.setText(operatorShowAlInfo.getCompleted_reports() + " " + getString(R.string.Signalements));

        if (operatorShowAlInfo.getProfile_percent().equalsIgnoreCase("0")) {

        } else {
            TVprogresspercentage.setText(operatorShowAlInfo.getProfile_percent() + "%");
        }

        progressBar1.setMax(100);
        progressBar1.setProgress(Integer.valueOf(operatorShowAlInfo.getProfile_percent()));

        RLfaq.setOnClickListener(this);


        if (mAppSession.getData("profile_qrcode") != null) {


            Glide.with(getActivity())
                    .load(mAppSession.getData("profile_qrcode"))
                    .override(250, 250)
                    .into(IVqrcode);
//            RequestOptions myOptions = new RequestOptions()
//                    .placeholder(R.drawable.ic_error_white)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .dontAnimate()
//                    .skipMemoryCache(true)
//                    ;
//            Glide
//                    .with(getActivity())
//                    .load(mAppSession.getData("profile_qrcode"))
//                    .apply(myOptions.override(250, 250))
//                    .into(IVqrcode);


        } else {

            IVqrcode.setImageResource(R.mipmap.img_qrcode);
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Rltoolbar:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
                break;
            case R.id.RLfaq:
                Utils.showAlertfaq(getString(R.string.faq), getActivity());
                break;
            case R.id.RLprofile:
                Utils.fragmentCall(new ProfileFragment(), getFragmentManager());
                break;


            case R.id.RLmedical:
                medicareDialog(getContext(), mAppSession, progressDialog, operatorShowAlInfo.getMedical_file(), mClossPassInterFace);
                break;
        }


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
                            //  Utils.showAlert(registrationResponse.getMessage(), getActivity());
                        } else {


                            if (mAppSession.getData("new_reports").equalsIgnoreCase("")) {

                                mAppSession.saveData("new_reports", operatorShowAlInfo.getNew_reports());


                            } else {

                                mAppSession.saveData("new_reports", operatorShowAlInfo.getNew_reports());
//                                mTimer = new Timer();   //recreate new
//                                mTimer.scheduleAtFixedRate(new OperatorFragment.TimeDisplay(), 0, notify);   //Schedule task
                            }
                            mAppSession.saveData("notifica", operatorShowAlInfo.getTotalMessageCount());
                            //  TVpsental.setText(getString(R.string.sentinel)+" "+operatorShowAlInfo.getLevel());
                            mAppSession.saveData("operatorgrade", operatorShowAlInfo.getGrade());
                            Log.e("getgrade==", "" + mAppSession.getData("operatorgrade"));
                            TVprogressbarnumber.setText(operatorShowAlInfo.getReport());
                            TVprogressbarreports.setText(operatorShowAlInfo.getCompleted_reports() + " " + getString(R.string.completedinterventions));
                            TVprogresspercentage.setText(operatorShowAlInfo.getProfile_percent() + "%");
                            progressBar1.setMax(100);
                            progressBar1.setProgress(Integer.valueOf(operatorShowAlInfo.getProfile_percent()));


                            TVprofiledescription.setText(getString(R.string.sentinel) + " " + operatorShowAlInfo.getLevel());
                            mAppSession.saveData("operatorlevel", operatorShowAlInfo.getLevel());
                        }
                        progressDialog.dismiss();


                    } else {
                        // Utils.showAlert(getString(R.string.Notfound), getActivity());

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
                Log.d("TAG", "Error " + t.getMessage());
                progressDialog.dismiss();
                Utils.showAlert(t.getMessage(), getActivity());
            }
        });
    }


    private void getAssigmentList(RequestBody Data) {


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
                            //  Utils.showAlert(registrationResponse.getMessage(), getActivity());

                        } else {

                            TVprofiledescription.setText(getString(R.string.sentinel) + " " + operatorShowAlInfo.getLevel());

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

    public static void medicareDialog(Context mContext, AppSession mAppSession, ProgressDialog progressDialog, String medicalText, Utils.clossPassInterFace mClossPassInterFace) {

        final Dialog dialog = new Dialog(mContext, R.style.MyDialogTheme1);
        dialog.setContentView(R.layout.medicare_dilog);
        dialog.setCancelable(false);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        RelativeLayout RLsave = (RelativeLayout) dialog.findViewById(R.id.RLsave);
        EditText mediET = (EditText) dialog.findViewById(R.id.mediET);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        if (medicalText != null) {
            mediET.setText(medicalText);
        }

        //   TVcustomdescriptiontext.setText(text);

        RLsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mediET.getText().toString().equalsIgnoreCase("")) {
                    Utils.showAlert(mContext.getString(R.string.medical), mContext);
                } else {

                    if (Utils.checkConnection(mContext)) {
                        LoginPojoSetData mLoginPojoSetData = new LoginPojoSetData();

                        mLoginPojoSetData.setUser_id(mAppSession.getData("id"));
                        mLoginPojoSetData.setdevice_language(mAppSession.getData("devicelanguage"));
                        mLoginPojoSetData.setMedical_file(mediET.getText().toString());
                        //  incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                        Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mLoginPojoSetData)));
                        RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mLoginPojoSetData)));

                        try {


                            progressDialog.show();
                            Service acceptInterven = ApiUtils.getAPIService();
                            Call<CommanStatusPojo> acceptIntervenpCall = acceptInterven.saveMedical(mFile);
                            acceptIntervenpCall.enqueue(new Callback<CommanStatusPojo>() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response) {
                                    try {
                                        if (response.body() != null) {
                                            CommanStatusPojo logOutCommanStatusPojo = response.body();
                                            if (logOutCommanStatusPojo.getStatus().equals("false")) {
                                                Utils.showAlert(logOutCommanStatusPojo.getMessage(), mContext);
                                            } else {
                                                Utils.showAlertInterFace(logOutCommanStatusPojo.getMessage(), mContext, mClossPassInterFace);
                                            }
                                            progressDialog.dismiss();

                                        } else {
                                            //  Utils.showAlert(getString(R.string.Notfound), getActivity());
                                            Utils.showAlert(mContext.getString(R.string.Notfound), mContext);
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // logOutData(mFile);
                    } else {
                        Utils.showAlert(mContext.getString(R.string.internet_conection), mContext);
                    }
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick() {
        if (mAppSession.getData("userType").equalsIgnoreCase("citizen")) {
            grade.setVisibility(View.GONE);


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


        } else {
            grade.setVisibility(View.VISIBLE);

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

            grade.setText(getString(R.string.grade) + " " + operatorShowAlInfo.getGrade());
        }
    }
}
