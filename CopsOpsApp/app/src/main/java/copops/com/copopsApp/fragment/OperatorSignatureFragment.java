package copops.com.copopsApp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;

import java.io.File;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.IncedentAcceptResponse;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("ValidFragment")
public class OperatorSignatureFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    @BindView(R.id.mClear)
    ImageView mClear;

    @BindView(R.id.signature_pad)
    SignaturePad mSignaturePad;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    @BindView(R.id.RLnext)
    RelativeLayout RLnext;

    AppSession mAppSession;

    String desc;
    boolean signatureCreated;
    String insidentId;

    ProgressDialog progressDialog;
    public OperatorSignatureFragment(String desc,String insidentId) {
        // Required empty public constructor
        this.desc=desc;
        this.insidentId=insidentId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_operator_signature, container, false);

        ButterKnife.bind(this,view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading...");
        mAppSession=mAppSession.getInstance(getActivity());

        mClear.setOnClickListener(this);
        RLnext.setOnClickListener(this);
        Rltoolbar.setOnClickListener(this);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                signatureCreated = true;
            }

            @Override
            public void onSigned() {
                signatureCreated = true;
                mClear.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClear() {
                signatureCreated = false;
                mClear.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLnext:

                try {
                    if (signatureCreated == false) {
                        Utils.showAlert(getActivity().getString(R.string.validation_signature), getActivity());
                    } else {
                        if (Utils.checkConnection(getActivity())) {
                            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                            incdentSetPojo.setUser_id(mAppSession.getData("id"));
                            incdentSetPojo.setComment(desc);
                            incdentSetPojo.setIncident_id(insidentId);
                            incdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                            Uri signature_uri = Utils.getImageUri(getActivity(), signatureBitmap);

                            File handrail_image_file;
                            MultipartBody.Part handrail_image_siginature = null;
                            if (signature_uri != null) {
                                String signature_path = Utils.getRealPathFromURIPath(signature_uri, getActivity());
                                handrail_image_file = new File(signature_path);
                                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), handrail_image_file);
                                handrail_image_siginature = MultipartBody.Part.createFormData("signature", handrail_image_file.getName(), mFile);
                            }
                            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                            getCopsCloseStatus(mFile, handrail_image_siginature);
                        } else {
                            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;

            case R.id.mClear:
                mSignaturePad.clear();
                signatureCreated = false;
                break;
            case R.id.Rltoolbar:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
                break;
        }
    }






    private void getCopsCloseStatus(RequestBody Data, MultipartBody.Part handrail_image_siginature) {

        try {
            progressDialog.show();
            Service operator = ApiUtils.getAPIService();
            Call<IncedentAcceptResponse> getallLatLong = operator.close(Data, handrail_image_siginature);
            getallLatLong.enqueue(new Callback<IncedentAcceptResponse>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<IncedentAcceptResponse> call, Response<IncedentAcceptResponse> response)

                {
                    try {
                        if (response.body() != null) {
                            IncedentAcceptResponse incedentAcceptResponse = response.body();
                            if (incedentAcceptResponse.getStatus().equals("false")) {
                                Utils.showAlert(incedentAcceptResponse.getMessage(), getActivity());

                            } else {

                                Utils.fragmentCall(new ReportFinishFragment(incedentAcceptResponse), getFragmentManager());

                            }
                            progressDialog.dismiss();

                        } else {
                            Utils.showAlert(response.message(), getActivity());
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.getMessage();
                        Utils.showAlert(e.getMessage(), getActivity());
                    }
                }

                @Override
                public void onFailure(Call<IncedentAcceptResponse> call, Throwable t) {
                    Log.d("TAG", "Error " + t.getMessage());
                    progressDialog.dismiss();
                    Utils.showAlert(t.getMessage(), getActivity());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
