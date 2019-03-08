package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.adapter.AssignmentInsidentListAdapter;
import copops.com.copopsApp.interfaceview.IncedentInterface;
import copops.com.copopsApp.pojo.AssignmentListPojo;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.shortcut.ShortcutViewService;
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
public class AssignmentTableFragment extends Fragment implements View.OnClickListener, IncedentInterface {


    @BindView(R.id.lvtableofassignments)
    RecyclerView lvtableofassignments;
    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    //    ProgressDialog progressDialog;
    IncedentInterface mIncedentInterface;
    AppSession mAppSession;
    AssignmentListPojo assignmentListPojo;

    ProgressDialog progressDialog;
    AssignmentInsidentListAdapter mAdapter;
    AssignmentListPojo assignmentListPojo_close;
//    public AssignmentTableFragment(AssignmentListPojo assignmentListPojo) {
//
//        this.assignmentListPojo=assignmentListPojo;
//        // Required empty public constructor
//    }


    public AssignmentTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_assignment_table, container, false);
        ButterKnife.bind(this, view);
        mIncedentInterface = this;
        mAppSession = mAppSession.getInstance(getActivity());
        Rltoolbar.setOnClickListener(this);
        getActivity().stopService(new Intent(getActivity(), ShortcutViewService.class));

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));


        if (Utils.checkConnection(getActivity())) {
            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
            incdentSetPojo.setUser_id(mAppSession.getData("id"));
            incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
            incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
            incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
            //  incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            getAssigmentListFirst(mFile);
        } else {
            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
        }

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.checkConnection(getActivity())) {
                    IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                    incdentSetPojo.setUser_id(mAppSession.getData("id"));
                    incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
                    incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
                    incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                    //  incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    getAssigmentList(mFile);
                } else {
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                }
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Rltoolbar:
                Utils.fragmentCall(new OperatorFragment(), getActivity().getSupportFragmentManager());

//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStackImmediate();
//                }
                break;
        }
    }

    @Override
    public void clickPosition(int pos) {

        Log.e("getIsAssigned==", "" + assignmentListPojo.getData().get(pos).getIsAssigned());
        Log.e("status==", "" + assignmentListPojo.getData().get(pos).getStatus());
        Log.e("getCreated_at==", "" + assignmentListPojo.getData().get(pos).getCreated_at());
        Log.e("getCreated_by==", "" + assignmentListPojo.getData().get(pos).getCreated_by());

        if (assignmentListPojo.getData().get(pos).getIsAssigned().equalsIgnoreCase("wait")) {

            if (assignmentListPojo.getPending().equals("1")) {
                Utils.showAlert(getActivity().getString(R.string.intervin), getActivity());
            } else {
                Utils.fragmentCall(new AssignedInterventionFragment(pos, assignmentListPojo), getFragmentManager());
            }
        } else if (assignmentListPojo.getData().get(pos).getIsAssigned().equalsIgnoreCase("pending")) {

            String sadada =  mAppSession.getData("id");

            if(assignmentListPojo.getData().get(pos).getUser_id().equalsIgnoreCase(mAppSession.getData("id"))) {
                //   rejectcustomdialog(getActivity(), pos);
             // String sadada =  mAppSession.getData("user_id");
                Utils.fragmentCall(new CloseIntervationReportFragment(assignmentListPojo, pos), getFragmentManager());
            }else{
                Utils.fragmentCall(new AssignedInterventionFragment(pos, assignmentListPojo), getFragmentManager());
            }


//            if (Utils.checkConnection(getActivity())) {
//                IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
//
//                incdentSetPojo.setUser_id(mAppSession.getData("id"));
//                incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
//                Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
//                RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
//                getAssignIntervationData(mFile);
//            } else {
//                Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
//            }
        } else if (assignmentListPojo.getData().get(pos).getIsAssigned().equalsIgnoreCase("Finished ")) {
            Utils.fragmentCall(new AssignedInterventionFragment(pos, assignmentListPojo), getFragmentManager());
        } else if (assignmentListPojo.getData().get(pos).getIsAssigned().equalsIgnoreCase("Assigned ")) {
            //  Utils.fragmentCall(new AssignedInterventionFragment(pos, assignmentListPojo), getFragmentManager());
        }
    }

    private void getAssigmentList(RequestBody Data) {

//        progressDialog.show();
        Service login = ApiUtils.getAPIService();
        Call<AssignmentListPojo> getallLatLong = login.getAssignmentList(Data);
        getallLatLong.enqueue(new Callback<AssignmentListPojo>() {
            @Override
            public void onResponse(Call<AssignmentListPojo> call, Response<AssignmentListPojo> response)

            {
                try {
                    if (response.body() != null) {
                        assignmentListPojo = response.body();
//                        progressDialog.dismiss();
                        swipeContainer.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();

                    } else {
                        Utils.showAlert(getString(R.string.Notfound), getActivity());
                    }

                } catch (Exception e) {
                    swipeContainer.setRefreshing(false);
//                    progressDialog.dismiss();
                    e.getMessage();
                    Utils.showAlert(e.getMessage(), getActivity());
                }
            }

            @Override
            public void onFailure(Call<AssignmentListPojo> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());
                swipeContainer.setRefreshing(false);
//                progressDialog.dismiss();
                Utils.showAlert(t.getMessage(), getActivity());
            }
        });
    }


    private void getAssigmentListFirst(RequestBody Data) {
        //  progressDialog.show();
        Service login = ApiUtils.getAPIService();
        Call<AssignmentListPojo> getallLatLong = login.getAssignmentList(Data);
        getallLatLong.enqueue(new Callback<AssignmentListPojo>() {
            @Override
            public void onResponse(Call<AssignmentListPojo> call, Response<AssignmentListPojo> response)

            {
                try {
                    if (response.body() != null) {
                        assignmentListPojo = response.body();

                        if (assignmentListPojo.getStatus().equalsIgnoreCase("false")) {
                            Utils.showAlert(getString(R.string.NoAssigned), getActivity());

                        } else {
                            mAdapter = new AssignmentInsidentListAdapter(getActivity(), assignmentListPojo, mIncedentInterface);
                            lvtableofassignments.setHasFixedSize(true);
                            lvtableofassignments.setLayoutManager(new LinearLayoutManager(getActivity()));
                            lvtableofassignments.setItemAnimator(new DefaultItemAnimator());
                            lvtableofassignments.setAdapter(mAdapter);
                        }

                    } else {
                        Utils.showAlert(getString(R.string.Notfound), getActivity());
                    }

                } catch (Exception e) {
                    //  progressDialog.dismiss();
                    e.getMessage();
                    Utils.showAlert(e.getMessage(), getActivity());
                }
            }

            @Override
            public void onFailure(Call<AssignmentListPojo> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());
                //    progressDialog.dismiss();
                Utils.showAlert(t.getMessage(), getActivity());
            }
        });
    }

    public void rejectcustomdialog(Context mContext, int pos) {

        final Dialog dialog = new Dialog(mContext, R.style.DialogFragmentTheme);
        dialog.setContentView(R.layout.rejectdilog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        TextView acceptId = (TextView) dialog.findViewById(R.id.acceptId);
        TextView rejectId = (TextView) dialog.findViewById(R.id.rejectId);
        //   TextView TVcustomdescriptiontext = (TextView) dialog.findViewById(R.id.TVcustomdescriptiontext);
        //  TVcustomdescriptiontext.setText(text);

        acceptId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Utils.fragmentCall(new CloseIntervationReportFragment(assignmentListPojo, pos), getFragmentManager());
            }
        });

        rejectId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkConnection(getActivity())) {
                    IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                    incdentSetPojo.setUser_id(mAppSession.getData("id"));
                    incdentSetPojo.setIncident_id(assignmentListPojo.getData().get(pos).getId());
                    incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));

                    //  incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                    rejected(mFile);
                } else {
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                }
                //    rejected();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    //  user_id,incident_id key
    //   http://13.233.74.84/api/auth/incident/rejected
    private void rejected(RequestBody Data) {
        try {
            progressDialog.show();
            Service acceptInterven = ApiUtils.getAPIService();
            Call<CommanStatusPojo> acceptIntervenpCall = acceptInterven.rejected(Data);
            acceptIntervenpCall.enqueue(new Callback<CommanStatusPojo>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response)

                {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo mCommanStatusPojo = response.body();
                            if (mCommanStatusPojo.getStatus().equals("false")) {
                                Utils.showAlert(mCommanStatusPojo.getMessage(), getActivity());
                            } else {
                                Utils.showAlert(mCommanStatusPojo.getMessage(), getActivity());
                                if (Utils.checkConnection(getActivity())) {
                                    IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                                    incdentSetPojo.setUser_id(mAppSession.getData("id"));
                                    incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
                                    incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
                                    incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                                    //  incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                    getAssigmentListFirst(mFile);
                                } else {
                                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                                }

                                //  Utils.fragmentCall(new CloseIntervationReportFragment(assignmentListPojo_close,pos), getFragmentManager());
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

}
