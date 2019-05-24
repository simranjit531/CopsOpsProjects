package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.AssignmentListPojo;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
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
public class AssignedInterventionFragment extends Fragment implements View.OnClickListener, Utils.clossPassInterFace {
    int pos;
    AssignmentListPojo assignmentListPojo;
    Utils.clossPassInterFace mClossPassInterFace;
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

    String dateString;
    ProgressDialog progressDialog;

    AppSession mAppSession;
    CommanStatusPojo commanStatusPojo;

    public AssignedInterventionFragment() {
        // Required empty public constructor
    }
    public AssignedInterventionFragment(int pos, AssignmentListPojo assignmentListPojo) {
        // Required empty public constructor
        this.pos = pos;
        this.assignmentListPojo = assignmentListPojo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_assigned_intervention, container, false);

        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        mClossPassInterFace = this;
        mAppSession = mAppSession.getInstance(getActivity());
        Utils.statusCheck(getActivity());

        initView();
        return view;
    }

    private void initView() {
        if (Utils.checkConnection(getActivity())) {
            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
            incdentSetPojo.setUser_id(mAppSession.getData("id"));
           // incdentSetPojo.setComment(descId.getText().toString().trim());
            incdentSetPojo.setIncident_id(assignmentListPojo.getData().get(pos).getId());
            incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
            incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            getUpdate(mFile);
        } else {
            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
        }
       // getUpdate();
if(assignmentListPojo.getData().get(pos).getUser_id()==null){
    tvid.setText(R.string.Intervene);
}
      else{
            tvid.setText(R.string.close);
        }

        Rltoolbar.setOnClickListener(this);
        Rlintervenue.setOnClickListener(this);
        etAddressId.setText(assignmentListPojo.getData().get(pos).getAddress());
        objectId.setText(assignmentListPojo.getData().get(pos).getSub_category_name());

        descId.setText(assignmentListPojo.getData().get(pos).getIncident_description());

        if(assignmentListPojo.getData().get(pos).getOther_description().equalsIgnoreCase("")){
          //  otherdescId.setText(assignmentListPojo.getData().get(pos).getOther_description());
            otherdescId.setVisibility(View.GONE);
            otherHeaderTv.setVisibility(View.GONE);
        }else {
            otherdescId.setVisibility(View.VISIBLE);
            otherHeaderTv.setVisibility(View.VISIBLE);
            otherdescId.setText(assignmentListPojo.getData().get(pos).getOther_description());
        }
        TVreferencenumber.setText(assignmentListPojo.getData().get(pos).getReference());
        dateString = assignmentListPojo.getData().get(pos).getCreated_at();
        String[] parts = dateString.split(" ");
        String date = parts[0]; // 004
        String time = parts[1]; // 034556
        Tvdate.setText(date);
        Tvtime.setText(time);
        if (assignmentListPojo.getData().get(pos).getIsAssigned().equalsIgnoreCase("wait")) {
            Tvstate.setText(R.string.onwait);
            Tvstate.setTextColor(getResources().getColor(R.color.orange));
        }
        else if (assignmentListPojo.getData().get(pos).getIsAssigned().equalsIgnoreCase("pending")) {
            Tvstate.setText(R.string.pending);
            Tvstate.setTextColor(getResources().getColor(R.color.btntextcolort));
        }else if (assignmentListPojo.getData().get(pos).getIsAssigned().equalsIgnoreCase("Assigned")) {
            Tvstate.setText(R.string.Assigned);
            Tvstate.setTextColor(getResources().getColor(R.color.black));
        }
        else {
            Tvstate.setText(R.string.finished);
          //  Tvstate.setText("Finished");
            Tvstate.setTextColor(getResources().getColor(R.color.green));
          //  Rlintervenue.setVisibility(View.INVISIBLE);
            //  Tvstate.setText("Pending");
            //  Tvstate.setTextColor(getResources().getColor(R.color.black));
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


            case R.id.Rlintervenue:
                //  Utils.fragmentCall(new CloseIntervationReportFragment(dateString, assignmentListPojo.getData().get(pos).getAddress(), assignmentListPojo.getData().get(pos).getReference(), assignmentListPojo.getData().get(pos).getStatus(), assignmentListPojo.getData().get(pos).getId()), getFragmentManager());
                //Utils.fragmentCall(new OperatorFragment(), getFragmentManager());
                if (assignmentListPojo.getData().get(pos).getUser_id()==null) {

                    if (Utils.checkConnection(getActivity())) {
                        IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                        incdentSetPojo.setUser_id(mAppSession.getData("id"));
                        incdentSetPojo.setComment(descId.getText().toString().trim());
                        incdentSetPojo.setIncident_id(assignmentListPojo.getData().get(pos).getId());
                        incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                        incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                        Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                        RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                        getAssignIntervation(mFile);
                    } else {
                        Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                    }
                } else {
                    Utils.fragmentCall(new AssignmentTableFragment(), getFragmentManager());
                }


                break;
        }
    }

    @Override
    public void onClick() {
        //   Utils.fragmentCall(new CloseIntervationReportFragment(dateString, assignmentListPojo.getData().get(pos).getAddress(), assignmentListPojo.getData().get(pos).getReference(), assignmentListPojo.getData().get(pos).getStatus(), assignmentListPojo.getData().get(pos).getId()), getFragmentManager());

        if (commanStatusPojo.getStatus().equalsIgnoreCase("true")) {
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
                Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
            }
        }
        else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        }
    }


    private void getAssignIntervation(RequestBody Data) {
        progressDialog.show();
        Service acceptInterven = ApiUtils.getAPIService();
        Call<CommanStatusPojo> acceptIntervenpCall = acceptInterven.acceptInterven(Data);
        acceptIntervenpCall.enqueue(new Callback<CommanStatusPojo>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response)

            {
                try {
                    if (response.body() != null) {
                        commanStatusPojo = response.body();
                        if (commanStatusPojo.getStatus().equals("false")) {
                            //   Utils.showAlert(commanStatusPojo.getMessage(), getActivity());
                            Utils.opendialogcustomdialogClose(getActivity(), commanStatusPojo.getMessage(), mClossPassInterFace);
                        } else {

                            if(commanStatusPojo.getMessage().equalsIgnoreCase(getString(R.string.youcan))) {
                                Utils.showAlert(commanStatusPojo.getMessage(), getActivity());
                            }else {
                                Utils.opendialogcustomdialogClose(getActivity(), commanStatusPojo.getMessage(), mClossPassInterFace);
                            }

                            // Utils.showAlertAndClick(commanStatusPojo.getMessage(), getContext(), mResetPassInterFace);

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



    private void getUpdate(RequestBody Data) {

        try {

            Service operator = ApiUtils.getAPIService();
            Call<CommanStatusPojo> getallLatLong = operator.getupdate(Data);
            getallLatLong.enqueue(new Callback<CommanStatusPojo>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response)

                {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo commanStatusPojo = response.body();

                            if (commanStatusPojo.getStatus().equals("false")) {
                            } else {
                            }
                        } else {

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
