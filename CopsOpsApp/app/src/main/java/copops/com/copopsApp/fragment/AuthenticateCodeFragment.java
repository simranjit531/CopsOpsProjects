package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import copops.com.copopsApp.pojo.CommanStatusPojo;
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
 * Created by Ranjan Gupta
 */
@SuppressLint("ValidFragment")
public class AuthenticateCodeFragment extends Fragment implements View.OnClickListener, View.OnKeyListener, Utils.resetPassInterFace {

    @BindView(R.id.ETfirst)
    EditText etFirst;
    @BindView(R.id.ETsecond)
    EditText etSecond;
    @BindView(R.id.ETthird)
    EditText etThird;
    @BindView(R.id.ETfourth)
    EditText etFourth;
    @BindView(R.id.ETfifth)
    EditText etFifth;
    @BindView(R.id.ETsixth)
    EditText etSixth;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.RLSend)
    RelativeLayout RLSend;
    private Context mContext;
    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;
    RegistationPojo mRegistationPojo;
    View view;
    String userType;
    AppSession mAppSession;
    String messageText = "123456";
    Utils.resetPassInterFace mResetPassInterFace;
    ProgressDialog progressDialog;

    public AuthenticateCodeFragment(String userType, RegistationPojo mRegistationPojo) {
        this.userType = userType;
        this.mRegistationPojo = mRegistationPojo;
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_authenticate_code, container, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        mResetPassInterFace = this;
        mAppSession = mAppSession.getInstance(mContext);
        tv_id.setText(getActivity().getString(R.string.otp_msg) + " " + mRegistationPojo.getEmail_id());
        onClick();
        etFirst.addTextChangedListener(new GenericTextWatcher(etFirst));
        etSecond.addTextChangedListener(new GenericTextWatcher(etSecond));
        etThird.addTextChangedListener(new GenericTextWatcher(etThird));
        etFourth.addTextChangedListener(new GenericTextWatcher(etFourth));
        etFifth.addTextChangedListener(new GenericTextWatcher(etFifth));
        etSixth.addTextChangedListener(new GenericTextWatcher(etSixth));
        etFirst.setOnKeyListener(this);
        etSecond.setOnKeyListener(this);
        etThird.setOnKeyListener(this);
        etFourth.setOnKeyListener(this);
        etFifth.setOnKeyListener(this);
        etSixth.setOnKeyListener(this);

        return view;
    }

    private void onClick() {

        if (messageText != null) {
            RLSend.setEnabled(true);
        } else {
            RLSend.setEnabled(false);
        }

        Rltoolbar.setOnClickListener(this);
        RLSend.setOnClickListener(this);

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
                if (Utils.checkConnection(mContext)) {
                    Utils.hideKeyboard(getActivity());
                    if (etFirst.getText().toString() != null && etSecond.getText().toString() != null && etThird.getText().toString() != null && etFourth.getText().toString() != null && etFifth.getText().toString() != null && etSixth.getText().toString() != null) {
                        if (mRegistationPojo.getOtp().equalsIgnoreCase(etFirst.getText().toString().trim() + etSecond.getText().toString().trim() + etThird.getText().toString().trim() + etFourth.getText().toString().trim() + etFifth.getText().toString().trim() + etSixth.getText().toString().trim())) {

                            progressDialog = new ProgressDialog(mContext);
                            progressDialog.setMessage(getString(R.string.loading));
                            progressDialog.show();
                            LoginPojoSetData loginPojoSetData = new LoginPojoSetData();
                            loginPojoSetData.setOtp(mRegistationPojo.getOtp());
                            loginPojoSetData.setEmail_id(mRegistationPojo.getEmail_id());
                            loginPojoSetData.setDevice_id(Utils.getDeviceId(mContext));
                            loginPojoSetData.setdevice_language(mAppSession.getData("devicelanguage"));
                            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(loginPojoSetData)));
                            Service otp = ApiUtils.getAPIService();
                            Call<CommanStatusPojo> fileUpload = otp.callOtp(mFile);
                            fileUpload.enqueue(new Callback<CommanStatusPojo>() {
                                @Override
                                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response)

                                {
                                    try {
                                        if (response.body() != null) {
                                            CommanStatusPojo commanStatusPojo = response.body();
                                            if (commanStatusPojo.getStatus().equals("false")) {
                                                Utils.showAlert(commanStatusPojo.getMessage(), mContext);
                                                //  Utils.fragmentCall(new CitizenFragment(userType,mRegistationPojo), getFragmentManager());
                                            } else {

                                                Utils.showAlertAndClick(commanStatusPojo.getMessage(), mContext, mResetPassInterFace);


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

                        } else {
                            Utils.showAlert(Utils.otpMsg, mContext);
                        }
                    } else {
                        Utils.showAlert(getString(R.string.pleaseenterotp), mContext);

                    }

                    break;
                } else {
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), mContext);
                }
        }
    }

    @Override
    public void onClick(int id) {
        if (id == 1) {

            mAppSession.saveData("Login", "1");

            if (userType.equalsIgnoreCase("Citizen")) {
                Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
            } else {
                Utils.fragmentCall(new OperatorFragment(), getFragmentManager());
            }
            //    Utils.fragmentCall(new CitizenFragment(), getFragmentManager());
        }
    }

//For use OTP listion
    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            switch (view.getId()) {

                case R.id.ETfirst:
                    if (s.length() == 1)
                        etSecond.requestFocus();

                    break;
                case R.id.ETsecond:
                    if (s.length() == 1)
                        etThird.requestFocus();

                    break;
                case R.id.ETthird:
                    if (s.length() == 1)
                        etFourth.requestFocus();

                    break;

                case R.id.ETfourth:
                    if (s.length() == 1)
                        etFifth.requestFocus();

                    break;

                case R.id.ETfifth:
                    if (s.length() == 1)
                        etSixth.requestFocus();
                    break;
                case R.id.ETsixth:
                    if (s.length() == 1)
                        etSixth.clearFocus();
                    Utils.hideKeyboard(getActivity());
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

    @Override
    public boolean onKey(View v, int i, KeyEvent keyEvent) {
        switch (v.getId()) {
            case R.id.ETfirst:

                break;
            case R.id.ETsecond:
                if (i == KeyEvent.KEYCODE_DEL)
                    etFirst.requestFocus();
                break;
            case R.id.ETthird:
                if (i == KeyEvent.KEYCODE_DEL)
                    etSecond.requestFocus();
                break;

            case R.id.ETfourth:
                if (i == KeyEvent.KEYCODE_DEL)
                    etThird.requestFocus();
                break;

            case R.id.ETfifth:
                if (i == KeyEvent.KEYCODE_DEL)
                    etFourth.requestFocus();
                break;
            case R.id.ETsixth:
                if (i == KeyEvent.KEYCODE_DEL)
                    etFifth.requestFocus();
                break;
        }
        return false;
    }


}
