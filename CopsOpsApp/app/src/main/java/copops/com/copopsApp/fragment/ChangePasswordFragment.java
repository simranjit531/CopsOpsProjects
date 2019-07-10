package copops.com.copopsApp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.LoginPojoSetData;
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
public class ChangePasswordFragment extends Fragment implements View.OnClickListener{


    @BindView(R.id.Etpassword)
    EditText Etpassword;

    @BindView(R.id.newpassword)
    EditText newpassword;

    @BindView(R.id.conpassword)
    EditText conpassword;

    @BindView(R.id.RLsave)
    RelativeLayout RLsave;

    AppSession mAppSession;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;
    ProgressDialog progressDialog;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        RLsave.setOnClickListener(this);
        Rltoolbar.setOnClickListener(this);
        mAppSession= mAppSession.getInstance(getContext());
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
           case R.id.RLsave:
            validate();
            break;
            case R.id.Rltoolbar:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
                break;
        }
    }
//Field Validation
    private void validate(){
        if (Etpassword.getText().toString().equals("")) {
            Utils.showAlert(getString(R.string.old_password), getContext());
        }else if(Etpassword.getText().toString().equals("")){
            Utils.showAlert(getString(R.string.new_password), getContext());
        }else if(!conpassword.getText().toString().equalsIgnoreCase(newpassword.getText().toString())){
            Utils.showAlert(getString(R.string.password_not_same), getContext());

        } else if (Utils.checkConnection(getContext())) {
            LoginPojoSetData mLoginPojoSetData= new LoginPojoSetData();
            mLoginPojoSetData.setUser_id(mAppSession.getData("id"));
            mLoginPojoSetData.setdevice_language(mAppSession.getData("devicelanguage"));
            mLoginPojoSetData.setOld_password(Etpassword.getText().toString().trim());
            mLoginPojoSetData.setPassword(newpassword.getText().toString().trim());
            mLoginPojoSetData.setPassword_confirmation(conpassword.getText().toString().trim());
            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mLoginPojoSetData)));
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mLoginPojoSetData)));
            progressDialog.show();
            Service operator = ApiUtils.getAPIService();
            Call<CommanStatusPojo> getallLatLong = operator.changePassword(mFile);
            getallLatLong.enqueue(new Callback<CommanStatusPojo>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response)

                {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo operatorShowAlInfo = response.body();
                            if (operatorShowAlInfo.getStatus().equals("false")) {
                                Utils.showAlert(operatorShowAlInfo.getMessage(), getContext());
                            } else {

                                if (Utils.checkConnection(getActivity())) {
                                    IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                                    incdentSetPojo.setUser_id(mAppSession.getData("id"));
                                    incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                    logOutData(mFile);
                                } else {
                                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                                }



                            }
                            progressDialog.dismiss();

                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.getMessage();
                        Utils.showAlert(e.getMessage(), getContext());
                    }
                }

                @Override
                public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
                    Log.d("TAG", "Error " + t.getMessage());
                    progressDialog.dismiss();
                    Utils.showAlert(t.getMessage(), getContext());
                }
            });



        } else {
            Utils.showAlert(getContext().getString(R.string.internet_conection), getContext());
        }

        //  android.os.Process.killProcess(android.os.Process.myPid());
        //   System.exit(1);
    }


    /**
     * for logout CITIZEN AND OPERATOR use this methed
     * @param Data= user id
     */
    private void logOutData(RequestBody Data) {
        try {
            progressDialog.show();
            Service acceptInterven = ApiUtils.getAPIService();
            Call<CommanStatusPojo> acceptIntervenpCall = acceptInterven.logout(Data);
            acceptIntervenpCall.enqueue(new Callback<CommanStatusPojo>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response) {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo  logOutCommanStatusPojo = response.body();
                            if (logOutCommanStatusPojo.getStatus().equals("false")) {
                                Utils.showAlert(logOutCommanStatusPojo.getMessage(), getActivity());
                            } else {
                                SharedPreferences preferences = getActivity().getSharedPreferences("copops.com.copopsApp", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                                mAppSession.saveData("Login", "0");
                                Utils.fragmentCall(new LoginFragment(mAppSession.getData("copsuser")),  getFragmentManager());
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
                    // Log.d("TAG", "Error " + t.getMessage());
                    progressDialog.dismiss();
                    Utils.showAlert(t.getMessage(), getActivity());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
