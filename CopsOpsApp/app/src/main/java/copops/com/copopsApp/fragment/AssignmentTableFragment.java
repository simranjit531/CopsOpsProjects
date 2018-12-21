package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

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
    ProgressDialog progressDialog;
    IncedentInterface mIncedentInterface;
    AppSession mAppSession;
    AssignmentListPojo assignmentListPojo;

    public AssignmentTableFragment(AssignmentListPojo assignmentListPojo) {

        this.assignmentListPojo=assignmentListPojo;
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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading...");
        AssignmentInsidentListAdapter mAdapter = new AssignmentInsidentListAdapter(getActivity(), assignmentListPojo, mIncedentInterface);
        lvtableofassignments.setHasFixedSize(true);
        lvtableofassignments.setLayoutManager(new LinearLayoutManager(getActivity()));
        lvtableofassignments.setItemAnimator(new DefaultItemAnimator());

        if (assignmentListPojo.getStatus().equalsIgnoreCase("false")) {
            Utils.showAlert(" No Assigned Intervention Found  so We can’t have access to the assigning an intervention", getActivity());

        } else{

        lvtableofassignments.setAdapter(mAdapter);
    }



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
}
