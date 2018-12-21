package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
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

/**
 * A simple {@link Fragment} subclass.
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

    OperatorShowAlInfo operatorShowAlInfo;
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
        initView();

        RLreportanincident.setOnClickListener(this);
        RLhandrail.setOnClickListener(this);
        RLnavigation.setOnClickListener(this);
        IVlogout.setOnClickListener(this);
        TVname.setOnClickListener(this);
        return view;
    }

    private void initView() {
        IVlogout.setVisibility(View.VISIBLE);
        IVback.setVisibility(View.GONE);
        TVname.setText(mAppSession.getData("name"));
        if (mAppSession.getData("image_url") != null&&!mAppSession.getData("image_url").equals("")) {
           Glide.with(this).load(mAppSession.getData("image_url")).into(IVprofilephoto);
         /*   Glide.with(this).load(mAppSession.getData("image_url"))
                    .error(R.mipmap.img_profile_photo).
                    .into(IVprofilephoto);*/
        }else {
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
            getAssigmentList(mFile);
        }
        else {
            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLreportanincident:
                Utils.fragmentCall(new IncidentFragment(mAppSession.getData("id")), getFragmentManager());
                mAppSession.saveData("operatorScreenBack","1");
                mAppSession.saveData("handrail","dasd");
                break;

            case R.id.RLhandrail:
                Utils.fragmentCall(new HandrailFragment(), getFragmentManager());
                mAppSession.saveData("handrail","handrail");
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



    private void getAssigmentList(RequestBody Data){


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

                           TVprofiledescription.setText(operatorShowAlInfo.getLevel());

                            TVprogressbarnumber.setText(operatorShowAlInfo.getReport());
                            TVprogressbarreports.setText(operatorShowAlInfo.getTotal_reports()+" Reports");

                            progressBar1.setMax(100);
                            progressBar1.setProgress(Integer.valueOf(operatorShowAlInfo.getProfile_percent()));

                            TVprogresspercentage.setText(operatorShowAlInfo.getProfile_percent()+"%");

                        }
                        progressDialog.dismiss();

                    }else{
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
}
