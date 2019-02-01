package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.LoginPojoSetData;
import copops.com.copopsApp.services.ApiUtils;
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
public class HomeFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.llcitizenhome)
    LinearLayout llcitizenhome;

    @BindView(R.id.llcopshome)
    LinearLayout llcopshome;

    Fragment fragment;


    AppSession mAppSession;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);



        mAppSession=mAppSession.getInstance(getActivity());
        onClick();

        if(mAppSession.getData("user_id").equalsIgnoreCase("")){

        }else{


            LoginPojoSetData mLoginPojoSetData = new LoginPojoSetData();
            mLoginPojoSetData.setUser_id(mAppSession.getData("id"));
            Log.e("taackingdata", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mLoginPojoSetData)));
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mLoginPojoSetData)));

            freeze(mFile);
            //freeze()

        }
        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());

        mAppSession.saveData("fcm_token",FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void onClick() {
        llcitizenhome.setOnClickListener(this);
        llcopshome.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llcitizenhome:
                Utils.fragmentCall(new LoginFragment("citizen"), getFragmentManager());
                break;


            case R.id.llcopshome:
                Utils.fragmentCall(new LoginFragment("Cops"), getFragmentManager());
                break;
        }
    }




    private void freeze(RequestBody Data) {

        try {

            copops.com.copopsApp.services.Service operator = ApiUtils.getAPIService();
            Call<CommanStatusPojo> getallLatLong = operator.freeze(Data);
            getallLatLong.enqueue(new Callback<CommanStatusPojo>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response)

                {
                    try {
                        if (response.body() != null) {
                            CommanStatusPojo commanStatusPojo = response.body();

                            if (commanStatusPojo.getStatus().equals("false")) {
                                if(commanStatusPojo.getIsfreeze().equalsIgnoreCase("0")){
                                    Utils.showAlert(commanStatusPojo.getMessage(), getActivity());
                                }

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
