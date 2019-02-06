package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;


import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.IncedentAcceptResponse;
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

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class IncedentReportFragment extends Fragment implements View.OnClickListener{

    IncedentAcceptResponse incedentAcceptResponse;

    @BindView(R.id.TVreferencenumber)
    TextView TVreferencenumber;

    @BindView(R.id.RLfinish)
    RelativeLayout RLfinish;

    @BindView(R.id.barId)
    ImageView barId;

    String screentype="";
    String parentname="";

//    @BindView(R.id.Rltoolbar)
//    RelativeLayout Rltoolbar;

    AppSession mAppSession;


    public IncedentReportFragment(IncedentAcceptResponse incedentAcceptResponse, String screentype,String parentname) {

        this.incedentAcceptResponse=incedentAcceptResponse;
        this.screentype=screentype;
        this.parentname=parentname;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_incedent_report, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {

        mAppSession=mAppSession.getInstance(getActivity());
        Log.e("incedentAcceptResponse",""+incedentAcceptResponse);
        if(incedentAcceptResponse.getReference()!=null) {
            TVreferencenumber.setText(incedentAcceptResponse.getReference());
        } if(incedentAcceptResponse.getQrcode_url()!=null){
          // Glide.with(this).load(incedentAcceptResponse.getQrcode_url()).into(barId);
            Glide.with(getActivity())
                    .load(incedentAcceptResponse.getQrcode_url()).override(250, 250)
                    .into(barId);
//            Glide
//                    .with(getActivity())
//                    .load(incedentAcceptResponse.getQrcode_url())
//                    .apply(new RequestOptions().override(250, 250))
//                    .into(barId);

        }

        RLfinish.setOnClickListener(this);
//        Rltoolbar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.RLfinish:

                if(mAppSession.getData("").equalsIgnoreCase("userType")){
                    Utils.fragmentCall(new Frag_Call_Number(incedentAcceptResponse.getHelpline_number(),parentname), getFragmentManager());
                }else if(mAppSession.getData("handrail").equalsIgnoreCase("handrail")){
                    Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
                }else if(mAppSession.getData("city").equalsIgnoreCase("city")){
                    if(mAppSession.getData("userType").equalsIgnoreCase("Citizen")) {
                        Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
                    }else{
                        Utils.fragmentCall(new OperatorFragment(), getFragmentManager());

                        if (Utils.checkConnection(getActivity())) {
                            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                            incdentSetPojo.setUser_id(mAppSession.getData("id"));
                            incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                            incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
                            incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
                            incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                            getCopeStatus(mFile);
                        } else {
                            Utils.showAlert( getActivity().getString(R.string.internet_conection), getActivity());
                        }
                    }
                 //   if(mAppSession.getData("userType").equalsIgnoreCase("userType")){
                   // Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
                }else{
                    Utils.fragmentCall(new Frag_Call_Number(incedentAcceptResponse.getHelpline_number(),parentname), getFragmentManager());
                }


//                if (screentype.equals("handrail")){
//                    Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
//                }else if (parentname.equals("city")){
//                    Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
//                }else{
//                    Utils.fragmentCall(new Frag_Call_Number(incedentAcceptResponse.getHelpline_number(),parentname), getFragmentManager());
//                }


                break;

//            case R.id.Rltoolbar:
//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStackImmediate();
//                }
//                break;
        }


    }


    private void getCopeStatus(RequestBody Data) {

        //   progressDialog.show();
        Service operator = ApiUtils.getAPIService();
        Call<OperatorShowAlInfo> getallLatLong = operator.getOperotor(Data);
        getallLatLong.enqueue(new Callback<OperatorShowAlInfo>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<OperatorShowAlInfo> call, Response<OperatorShowAlInfo> response)

            {
                try {
                    if (response.body() != null) {
                        OperatorShowAlInfo operatorShowAlInfo = response.body();

                        if (operatorShowAlInfo.getStatus().equals("false")) {
                            //  Utils.showAlert(registrationResponse.getMessage(), getActivity());
                        } else {


                            String new_reports =operatorShowAlInfo.getNew_reports();
                            mAppSession.saveData("new_reports",new_reports);



                        }

                    }} catch (Exception e) {

                    e.getMessage();

                }
            }

            @Override
            public void onFailure(Call<OperatorShowAlInfo> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());


            }
        });
    }
}
