package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import copops.com.copopsApp.pojo.IncdentSetPojo;
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


    AssignmentInsidentListAdapter mAdapter;

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
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("loading...");




        if (Utils.checkConnection(getActivity())) {
            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
            incdentSetPojo.setUser_id(mAppSession.getData("id"));
            incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
            incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
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

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
                break;
        }
    }

    @Override
    public void clickPosition(int pos) {

        if (assignmentListPojo.getData().get(pos).getStatus().equalsIgnoreCase("1")) {
            Utils.fragmentCall(new AssignedInterventionFragment(pos, assignmentListPojo), getFragmentManager());
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
                        Utils.showAlert(response.message(), getActivity());
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
                            Utils.showAlert(" No Assigned Intervention Found  so We can’t have access to the assigning an intervention", getActivity());

                        } else{
                            mAdapter = new AssignmentInsidentListAdapter(getActivity(), assignmentListPojo, mIncedentInterface);
                            lvtableofassignments.setHasFixedSize(true);
                            lvtableofassignments.setLayoutManager(new LinearLayoutManager(getActivity()));
                            lvtableofassignments.setItemAnimator(new DefaultItemAnimator());
                            lvtableofassignments.setAdapter(mAdapter);
                        }

                    } else {
                        Utils.showAlert(response.message(), getActivity());
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
}
