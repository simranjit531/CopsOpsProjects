package copops.com.copopsApp.fragment;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.AssignmentListPojo;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.Utils;

/**
 * Created by Ranjan Gupta
 */
@SuppressLint("ValidFragment")
public class CloseIntervationReportFragment extends Fragment implements View.OnClickListener, Utils.resetPassInterFace {
    String dateString;
    String address;
    String reference;
    String status;


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
    RelativeLayout closeIntervation;

    ProgressDialog progressDialog;
    AppSession mAppSession;
    String insidentId;
    Utils.resetPassInterFace mResetPassInterFace;
    AssignmentListPojo assignmentListPojo_close;
    int pos;


    public CloseIntervationReportFragment(AssignmentListPojo assignmentListPojo_close,int pos) {
        this.assignmentListPojo_close = assignmentListPojo_close;
        this.pos = pos;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_close_intervation_report, container, false);

        ButterKnife.bind(this, view);

        mAppSession = mAppSession.getInstance(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));

        mResetPassInterFace = this;
        initView();

        return view;
    }

    private void initView() {
        try {


            closeIntervation.setOnClickListener(this);
            Rltoolbar.setOnClickListener(this);

            if (assignmentListPojo_close.getData().get(pos).getAddress() != null) {
                etAddressId.setText(assignmentListPojo_close.getData().get(pos).getAddress());
            }
            TVreferencenumber.setText(assignmentListPojo_close.getData().get(pos).getReference());

            //   TVreferencenumber.setText(reference);
            //dateString = dateString;
            String[] parts = assignmentListPojo_close.getData().get(pos).getCreated_at().split(" ");
            String date = parts[0]; // 004
            String time = parts[1]; // 034556
            Tvdate.setText(date);
            Tvtime.setText(time);
            if (assignmentListPojo_close.getData().get(pos).getIsAssigned().equalsIgnoreCase("pending")) {
                Tvstate.setText(getString(R.string.pending));
                Tvstate.setTextColor(getResources().getColor(R.color.btntextcolort));
            }
        } catch (Exception e) {
            e.printStackTrace();
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


                if ((descId.getText().toString().trim().equalsIgnoreCase(""))) {
                    Utils.showAlert(getActivity().getString(R.string.des), getActivity());
                } else {

                    if(assignmentListPojo_close.getStatus().equals("false")){
                        Utils.showAlert(assignmentListPojo_close.getMessage(),getActivity());
                    }else {
                        Utils.fragmentCall(new OperatorSignatureFragment(descId.getText().toString().trim(), assignmentListPojo_close.getData().get(pos).getId()), getFragmentManager());
                    }
                    }

                break;
        }

    }

    @Override
    public void onClick(int id) {
        //  Utils.fragmentCall(new OperatorSignatureFragment(), getFragmentManager());

    }
}
