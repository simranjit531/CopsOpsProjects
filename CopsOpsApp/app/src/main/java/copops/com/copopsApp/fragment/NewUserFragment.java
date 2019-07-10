package copops.com.copopsApp.fragment;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.adapter.NewUserListAdapter;
import copops.com.copopsApp.adapter.RecentChatAdapter;
import copops.com.copopsApp.pojo.LoginPojoSetData;
import copops.com.copopsApp.pojo.UserListPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ranjan Gupta
 */
public class NewUserFragment extends Fragment implements NewUserListAdapter.UserListInterFace, View.OnClickListener {

@BindView(R.id.newUser_list_recycler_view)
    RecyclerView newUser_list_recycler_view;
    UserListPojo userListPojo;
    NewUserListAdapter mNewUserListAdapter;
    NewUserListAdapter.UserListInterFace mUserListInterFace;

    @BindView(R.id.toolbarId)
    RelativeLayout toolbarId;


    @BindView(R.id.et_search_recent)
    EditText et_search_recent;

    AppSession mAppSession;
    ArrayList<UserListPojo.Response> resList = new ArrayList<>();

    ProgressDialog progressDialog;
    public NewUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_new_user, container, false);
        ButterKnife.bind(this,view);
        mAppSession= mAppSession.getInstance(getContext());

        if (Utils.checkConnection(getContext()))
            chatUserList(mAppSession.getData("id"));
        else
            Utils.showAlert(getActivity().getString(R.string.internet_conection), getContext());


        return view;
    }
    private void initView() {

        toolbarId.setOnClickListener(this);
        mUserListInterFace=this;

        mNewUserListAdapter = new NewUserListAdapter(getContext(),resList,mUserListInterFace);
        newUser_list_recycler_view.setHasFixedSize(true);
        newUser_list_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        newUser_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        newUser_list_recycler_view.setAdapter(mNewUserListAdapter);


        et_search_recent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable editable) {
                    String text = et_search_recent.getText().toString().trim();
                    mNewUserListAdapter.filter(text);

            }
        });

    }

//    private void filter(String text) {
//        //new array list that will hold the filtered data
//        ArrayList<UserListPojo.Response> filterdNames = new ArrayList<>();
//
//        //looping through existing elements
//        for (String s : resList) {
//            //if the existing elements contains the search input
//            if (s.toLowerCase().contains(text.toLowerCase())) {
//                //adding the element to filtered list
//                filterdNames.add(s);
//            }
//        }
//
//        //calling a method of the adapter class and passing the filtered list
//        adapter.filterList(filterdNames);
//    }


    public void chatUserList(String userId){



        initDialog(getActivity().getString(R.string.loading_msg));
        progressDialog.show();
        LoginPojoSetData loginPojoSetData = new LoginPojoSetData();
        loginPojoSetData.setUser_id(userId);
        //	loginPojoSetData.setPassword("123456");

        Log.e("@@@@", new Gson().toJson(loginPojoSetData));
        RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(loginPojoSetData));


        Service login = ApiUtils.getAPIService();
        Call<UserListPojo> fileUpload = login.userList(mFile);
        fileUpload.enqueue(new Callback<UserListPojo>() {
            @Override
            public void onResponse(Call<UserListPojo> call, Response<UserListPojo> response) {
                try {
                    if (response.body() != null) {
                         userListPojo = response.body();
                        resList.clear();
                        if (userListPojo.getStatus().equals("false")) {
                            Utils.showAlert(response.message(), getContext());
                            progressDialog.dismiss();
                        } else {
                            resList.addAll(userListPojo.getResponse());
                             initView();
                              progressDialog.dismiss();

                        }
                        //progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();
                        //Utils.showAlert(response.message(), mContext);
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.getMessage();
                    	Utils.showAlert(e.getMessage(), getActivity());
                }
            }

            @Override
            public void onFailure(Call<UserListPojo> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());
                //progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(int pos) {

        if(mAppSession.getData("id").equalsIgnoreCase(resList.get(pos).getId())){

        }else {
            Utils.fragmentCall(new ChatConversionFragment(resList.get(pos).getId(), resList.get(pos).getFirst_name() + " " +resList.get(pos).getLast_name(), mAppSession.getData("id")), getFragmentManager());
        }

    }



    public void initDialog(String msg) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(msg);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.toolbarId:
                Utils.fragmentCall(new ChatRecentFragment(), getActivity().getSupportFragmentManager());
                break;


        }

    }
}
