package copops.com.copopsApp.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.IncedentAcceptResponse;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ranjan Gupta
 */
@SuppressLint("ValidFragment")
public class HandrailSignatureFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.RLnext)
    RelativeLayout RLnext;

    @BindView(R.id.mClear)
    ImageView mClear;

    @BindView(R.id.signature_pad)
    SignaturePad mSignaturePad;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    MultipartBody.Part handrail_image;
    MultipartBody.Part handrail_video;
    RequestBody mFile;
    Context mContext;
    private Uri signature_uri;

    String signature_path="";
    String filePathImage;
    String filePathVideo;
    boolean signatureCreated;
    ProgressDialog progressDialog;


    public HandrailSignatureFragment(MultipartBody.Part handrail_image,MultipartBody.Part handrail_video, RequestBody mFile, String filePathImage, String filePathVideo) {
        // Required empty public constructor
        this.handrail_image=handrail_image;
        this.handrail_video=handrail_video;
        this.mFile=mFile;
        this.filePathImage=filePathImage;
        this.filePathVideo=filePathImage;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_signature, container, false);

        ButterKnife.bind(this,view);

        RLnext.setOnClickListener(this);
        mClear.setOnClickListener(this);
        Rltoolbar.setOnClickListener(this);
        mContext = getActivity();

        mClear.setVisibility(View.INVISIBLE);


//        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

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
        switch (v.getId()){
            case R.id.RLnext:


              if (Utils.checkConnection(getActivity()))
                  siginatureApi();
              else
                  Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
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

    //Send signature on Server
    public void siginatureApi() {

        try {

            if (signatureCreated == false) {
                Utils.showAlert(getActivity().getString(R.string.validation_signature), getActivity());
            } else {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getActivity().getString(R.string.loading_msg));
                progressDialog.show();
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                signature_uri = Utils.getImageUri(mContext, signatureBitmap);

                File handrail_image_file;
                MultipartBody.Part handrail_image_siginature = null;
                if (signature_uri != null) {
                    signature_path = Utils.getRealPathFromURIPath(signature_uri, getActivity());
                    handrail_image_file = new File(signature_path);
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), handrail_image_file);
                    handrail_image_siginature = MultipartBody.Part.createFormData("signature", handrail_image_file.getName(), mFile);
                }

                Service uploadIncedentGenerate = ApiUtils.getAPIService();
                Call<IncedentAcceptResponse> fileUpload = uploadIncedentGenerate.generateHandrailSignature(handrail_image_siginature, handrail_image, handrail_video, mFile);
                fileUpload.enqueue(new Callback<IncedentAcceptResponse>() {
                    @Override
                    public void onResponse(Call<IncedentAcceptResponse> call, Response<IncedentAcceptResponse> response) {
                        try {
                            if (response.body() != null) {
                                progressDialog.dismiss();
                                IncedentAcceptResponse incedentAcceptResponse = response.body();
                                if (incedentAcceptResponse.getStatus().equals("false")) {
                                    Utils.showAlert(incedentAcceptResponse.getMessage(), mContext);
                                } else {
                                    //   Utils.fragmentCall(new IncedentReportFragment(), getFragmentManager());
                                    Utils.fragmentCall(new IncedentReportFragment(incedentAcceptResponse,"handrail","noname"), getFragmentManager());
                                    if (filePathImage != null) {
                                        File fdelete = new File(filePathImage);
                                        if (fdelete.exists()) {
                                            if (fdelete.delete()) {
                                            } else {

                                            }
                                        }
                                    }
                                    if (filePathVideo != null) {
                                        File fdeletevideo = new File(filePathVideo);
                                        if (fdeletevideo.exists()) {
                                            if (fdeletevideo.delete()) {

                                            } else {

                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
                                Utils.showAlert(response.message(), mContext);
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Utils.showAlert(e.getMessage(), mContext);
                        }
                    }

                    @Override
                    public void onFailure(Call<IncedentAcceptResponse> call, Throwable t) {
                        // Log.d(TAG, "Error " + t.getMessage());
                        progressDialog.dismiss();
                        Utils.showAlert(t.getMessage(), mContext);
                    }
                });
            }
        }catch (Exception e){
           e.printStackTrace();
            Utils.showAlert(e.getMessage(), mContext);
        }
    }
    }

