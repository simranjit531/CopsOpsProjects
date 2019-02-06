package copops.com.copopsApp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.adapter.IncidentSubTypeAdapter;
import copops.com.copopsApp.interfaceview.IncedentInterface;
import copops.com.copopsApp.pojo.IncidentSetPojo;
import copops.com.copopsApp.pojo.IncidentSubPojo;
import copops.com.copopsApp.pojo.IncidentTypePojo;
import copops.com.copopsApp.pojo.LoginPojoSetData;
import copops.com.copopsApp.pojo.RegistationPojo;
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
public class IncidentTypeFragment extends Fragment implements View.OnClickListener, IncedentInterface {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    @BindView(R.id.mHeader)
    TextView mHeader;

    @BindView(R.id.mSubHeader)
    TextView mSubHeader;


    Context mContext;
    ProgressDialog progressDialog;
    IncidentTypePojo mIncidentTypeResponse;
    IncedentInterface mIncedentInterface;

    IncidentSubPojo incidentSubPojo;
    int pos;
    AppSession mAppSession;
    String incedentTypeId;
    String userId;

    public IncidentTypeFragment(IncidentTypePojo mIncidentTypeResponse, int pos, String userId) {
        this.mIncidentTypeResponse = mIncidentTypeResponse;
        this.pos = pos;
        this.userId = userId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_reporting_common, container, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        mIncedentInterface = this;
        mAppSession = mAppSession.getInstance(getActivity());
        onClick();
        mHeader.setText(mIncidentTypeResponse.getData().get(pos).getIncident_name());
        mSubHeader.setText(mIncidentTypeResponse.getData().get(pos).getIncident_description());

        return view;
    }

    private void onClick() {
        incedentTypeId = mIncidentTypeResponse.getData().get(pos).getIncident_id();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        Rltoolbar.setOnClickListener(this);

        if (Utils.checkConnection(mContext))
            getIncedentSubType();
        else
            Utils.showAlert(getActivity().getString(R.string.internet_conection), mContext);
    }

    public void getIncedentSubType() {
        try {

            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(getActivity().getString(R.string.loading_msg));
            progressDialog.show();
            IncidentSetPojo incidentSetPojo = new IncidentSetPojo();

            incidentSetPojo.setIncident_id(mIncidentTypeResponse.getData().get(pos).getIncident_id());
            incidentSetPojo.setDevice_id(Utils.getDeviceId(mContext));
            incidentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));

            Service incedentTypeData = ApiUtils.getAPIService();
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incidentSetPojo)));
            //  RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"),  "6ofmYMOSHsnj+NTBDXo6107Iqzaw+Rx0X2brlpHmVYk=:ZmVkY2JhOTg3NjU0MzIxMA==");

            Call<IncidentSubPojo> fileUpload = incedentTypeData.getIncedentSubTypeData(mFile);

            fileUpload.enqueue(new Callback<IncidentSubPojo>() {
                @Override
                public void onResponse(Call<IncidentSubPojo> call, Response<IncidentSubPojo> response) {
                    try {
                        if (response.body() != null) {
                            incidentSubPojo = response.body();
                            progressDialog.dismiss();
                            if (incidentSubPojo.getStatus().equals("false")) {
                                progressDialog.dismiss();
                            } else {

                                progressDialog.dismiss();
                                IncidentSubTypeAdapter adapter = new IncidentSubTypeAdapter(mContext, incidentSubPojo, mIncedentInterface);
                                recyclerView.setAdapter(adapter);
                                @SuppressLint("WrongConstant") GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(manager);
                            }
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.getMessage();
                        Utils.showAlert(e.getMessage(), mContext);
                    }
                }

                @Override
                public void onFailure(Call<IncidentSubPojo> call, Throwable t) {
                    Log.d("TAG", "Error " + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showAlert(e.getMessage(), mContext);
        }

    }

    /////All click listners
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


        mAppSession.saveData("screen", "0");

        Utils.fragmentCall(new IncedentGenerateFragment(incedentTypeId, incidentSubPojo, pos, userId, mAppSession.getData("screen")), getFragmentManager());
    }
}
