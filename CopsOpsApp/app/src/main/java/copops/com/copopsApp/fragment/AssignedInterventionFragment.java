package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.activity.DashboardActivity;
import copops.com.copopsApp.pojo.AssignmentListPojo;
import copops.com.copopsApp.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class AssignedInterventionFragment extends Fragment implements View.OnClickListener,Utils.clossPassInterFace {
    int pos;
    AssignmentListPojo assignmentListPojo;
    Utils.clossPassInterFace mClossPassInterFace;
    @BindView(R.id.Tvdate)
    TextView Tvdate;
    @BindView(R.id.Tvtime)
    TextView Tvtime;
    @BindView(R.id.Tvstate)
    TextView Tvstate;

    @BindView(R.id.TVreferencenumber)
    TextView TVreferencenumber;

    @BindView(R.id.etAddressId)
    EditText etAddressId;
    @BindView(R.id.objectId)
    EditText objectId;

    @BindView(R.id.descId)
    EditText descId;
    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    @BindView(R.id.Rlintervenue)
    RelativeLayout Rlintervenue;

    String dateString;

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

        mClossPassInterFace=this;

        initView();
        return view;
    }

    private void initView() {

        Rltoolbar.setOnClickListener(this);
        Rlintervenue.setOnClickListener(this);
        etAddressId.setText(assignmentListPojo.getData().get(pos).getAddress());
        objectId.setText(assignmentListPojo.getData().get(pos).getSub_category_name());
        descId.setText(assignmentListPojo.getData().get(pos).getOther_description());
        TVreferencenumber.setText(assignmentListPojo.getData().get(pos).getReference());
        dateString = assignmentListPojo.getData().get(pos).getCreated_at();
        String[] parts = dateString.split(" ");
        String date = parts[0]; // 004
        String time = parts[1]; // 034556
        Tvdate.setText(date);
        Tvtime.setText(time);
        if (assignmentListPojo.getData().get(pos).getStatus().equalsIgnoreCase("1")) {
            Tvstate.setText("Pending");
            Tvstate.setTextColor(getResources().getColor(R.color.btntextcolort));
        } else {
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
                Utils.fragmentCall(new CloseIntervationReportFragment(dateString, assignmentListPojo.getData().get(pos).getAddress(), assignmentListPojo.getData().get(pos).getReference(), assignmentListPojo.getData().get(pos).getStatus(), assignmentListPojo.getData().get(pos).getId()), getFragmentManager());


                // Utils.opendialogcustomdialogClose(getActivity(),getActivity().getString(R.string.exit_message),mClossPassInterFace);

                break;
        }
    }

    @Override
    public void onClick() {
        Utils.fragmentCall(new CloseIntervationReportFragment(dateString, assignmentListPojo.getData().get(pos).getAddress(), assignmentListPojo.getData().get(pos).getReference(), assignmentListPojo.getData().get(pos).getStatus(), assignmentListPojo.getData().get(pos).getId()), getFragmentManager());

    }
}
