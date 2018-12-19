package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.LoginPojoSetData;
import copops.com.copopsApp.pojo.RegistationPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
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
public class ResetPasswordFragment extends Fragment implements View.OnClickListener,Utils.resetPassInterFace {


    @BindView(R.id.ETemail)
    EditText etEmail;

    @BindView(R.id.RLSend)
    RelativeLayout rlSend;

    Context mContext;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    String userType;
    String userTypeRegistation;
    Utils.resetPassInterFace mResetPassInterFace;
    ProgressDialog progressDialog;

    public ResetPasswordFragment(String userType) {
        this.userType=userType;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        mResetPassInterFace=this;
        onClick();
        return view;
    }

    private void onClick(){

        if (userType.equalsIgnoreCase("citizen")) {
            userTypeRegistation = "Citizen";
         //   initView();
        } else {
            userTypeRegistation = "Cops";
        }

        Rltoolbar.setOnClickListener(this);
        rlSend.setOnClickListener(this);
    }
    public void validation() {
        if (etEmail.getText().toString().equals("")) {
            Utils.showAlert(getActivity().getString(R.string.email), mContext);

        } else if (!Utils.isValidMail(etEmail.getText().toString())) {
            Utils.showAlert(getActivity().getString(R.string.valid_email_errer), mContext);
        }else {
            Utils.hideKeyboard(getActivity());
            if (Utils.checkConnection(mContext)) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage(getActivity().getString(R.string.loading_msg));
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                LoginPojoSetData loginPojoSetData = new LoginPojoSetData();
                loginPojoSetData.setEmail_id(etEmail.getText().toString().trim());
                loginPojoSetData.setRef_user_type_id(userTypeRegistation);
                loginPojoSetData.setDevice_id(Utils.getDeviceId(mContext));
                RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(loginPojoSetData)));
                Service login = ApiUtils.getAPIService();
                Call<CommanStatusPojo> fileUpload = login.userReset(mFile);
                fileUpload.enqueue(new Callback<CommanStatusPojo>() {
                    @Override
                    public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response)

                    {
                        try {
                            if (response.body() != null) {
                                CommanStatusPojo registrationResponse = response.body();
                                if (registrationResponse.getStatus().equals("false")) {
                                    Utils.showAlert(registrationResponse.getMessage(), mContext);
                                } else {
                                    Utils.showAlertAndClick(registrationResponse.getMessage(), mContext, mResetPassInterFace);

                                }
                                progressDialog.dismiss();

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
                    }
                });
            }else{
                Utils.showAlert(getActivity().getString(R.string.internet_conection), mContext);
            }
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

            case R.id.RLSend:
                if (Utils.checkConnection(mContext))
                    validation();
                else
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), mContext);
               // validation();
                break;
        }
    }

    @Override
    public void onClick(int id) {

        if(id==1){
            Utils.fragmentCall(new LoginFragment(userType), getFragmentManager());
        }

    }
}
