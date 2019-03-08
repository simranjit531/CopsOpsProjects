package copops.com.copopsApp.fragment;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.RegistationPjoSetData;
import copops.com.copopsApp.pojo.RegistationPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.shortcut.GPSTracker;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.MyDatePickerFragment;
import copops.com.copopsApp.utils.TrackingServices;
import copops.com.copopsApp.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class RegistationFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.Etdate)
    EditText datePicker;

    @BindView(R.id.Etlastname)
    EditText etlName;

    @BindView(R.id.Etfirstname)
    EditText etfName;

    @BindView(R.id.Etphonenumber)
    EditText etPhoneNo;

    @BindView(R.id.Etemail)
    EditText etEmail;

    @BindView(R.id.Etpassword)
    EditText etPassword;

    @BindView(R.id.Etconfirmpassword)
    EditText etCPassword;

    @BindView(R.id.IVcamera)
    ImageView ivCamera;


    @BindView(R.id.IV_IDcardcoud)
    ImageView IV_IDcardcoud;


    @BindView(R.id.IV_IDcardvideocoud)
    ImageView IV_IDcardvideocoud;


    @BindView(R.id.IV_businesscardcoud)
    ImageView IV_businesscardcoud;

    @BindView(R.id.IV_businesscardvideocoud)
    ImageView IV_businesscardvideocoud;

    @BindView(R.id.IVgallery)
    ImageView ivGallery;

    @BindView(R.id.IVprofilephoto)
    CircleImageView IVprofilephoto;

    @BindView(R.id.IV_IDcardcamera)
    ImageView IV_IDcardcamera;

    @BindView(R.id.IV_IDcardvideocamera)
    ImageView IV_IDcardvideocamera;

    @BindView(R.id.IV_businesscardcamera)
    ImageView IV_businesscardcamera;

    @BindView(R.id.IV_businesscardvideocamera)
    ImageView IV_businesscardvideocamera;

    @BindView(R.id.IVgreenmam)
    ImageView IVgreenmam;

    @BindView(R.id.IVgreenwoman)
    ImageView IVgreenwoman;

    @BindView(R.id.RLnext)
    RelativeLayout rLNext;

    LocationManager mLocationManager;
    private boolean isNetworkEnabled;
    private boolean isGpsEnabled;
    double longitude;
    double latitude;


//    @BindView(R.id.mSexRadioGroup)
//    RadioGroup mGenderRadioGroup;

//    @BindView(R.id.mMaleRadioButton)
//    RadioButton mMaleRadioButton;
//
//    @BindView(R.id.mFemaleRadioButton)
//    RadioButton mFemaleRadioButton;


    @BindView(R.id.mailIdTv)
    RelativeLayout mailIdTv;

    @BindView(R.id.femailId)
    RelativeLayout femailId;
//
//
//    @BindView(R.id.IVgreenwoman)
//    ImageView ivWoman;

    @BindView(R.id.rlidcard)
    RelativeLayout rlidcard;

    @BindView(R.id.rlbusinesscard)
    RelativeLayout rlbusinesscard;

    @BindView(R.id.lldesignbusinesscard)
    LinearLayout lldesignbusinesscard;

    @BindView(R.id.lldesignIDcard)
    LinearLayout lldesignIDcard;

    private Fragment fragment;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    private Context mContext;

    private Uri idCardUri_1;
    private Uri idCardUri_2;
    private Uri idBusinessCardUri_1;
    private Uri idBusinessCardUri_2;
    private String profilePicUri;
    private Uri profileUri;

    String filePathprofilePic;
    String filePathidCard_1;
    String filePathidCard_2;
    String fileBusCard_1;
    String fileBusCard_2 ;
    private String stringDate;

    AppSession mAppSession;

    private String userTypeRegistation;

    private String data;
    private String gender = "";

    ProgressDialog progressDialog;

    String TAG = "TAG";
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2, IDCARD_1 = 0, IDCARD_2 = 0, IDBUSINESSCARD_1 = 0, IDBUSINESSCARD_2 = 0;

    DatePickerDialog.OnDateSetListener dateSetListener;

    private String userType;
    Service mAPIService;
    GPSTracker gps;

    public RegistationFragment(String userType) {
        this.userType = userType;


        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_operter_registation, container, false);
        ButterKnife.bind(this, view);
        dateSetListener = this;

        mAPIService = ApiUtils.getAPIService();

        gps = new GPSTracker(getActivity());
        Utils.statusCheck(getActivity());

        latitude=gps.getLatitude();
        longitude=gps.getLongitude();
        mAppSession = mAppSession.getInstance(mContext);

        try {
            mAppSession.saveData("latitude", String.valueOf(latitude));
            mAppSession.saveData("longitude", String.valueOf(longitude));
        }catch (Exception e){
            e.printStackTrace();
        }

        if (userType.equalsIgnoreCase("citizen")) {
            userTypeRegistation = "Citizen";
            initView();
        } else {
            userTypeRegistation = "Cops";
        }


        onClick();
        mContext = getActivity();

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getString(R.string.loading));


//        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        if (checkPermission() && gpsEnabled()) {
//            if (isNetworkEnabled) {
//                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
//                        10, mLocationListener);
//            } else {
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
//                        10, mLocationListener);
//            }
//        }
        return view;
    }


    private void initView() {

        rlidcard.setVisibility(View.GONE);
        rlbusinesscard.setVisibility(View.GONE);
        lldesignbusinesscard.setVisibility(View.GONE);
        lldesignIDcard.setVisibility(View.GONE);
    }

    private void onClick() {

        Rltoolbar.setOnClickListener(this);
        datePicker.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivGallery.setOnClickListener(this);
        IV_businesscardcamera.setOnClickListener(this);
        IV_businesscardvideocamera.setOnClickListener(this);
        IV_IDcardvideocamera.setOnClickListener(this);
        IV_IDcardcamera.setOnClickListener(this);
        rLNext.setOnClickListener(this);
        femailId.setOnClickListener(this);
        mailIdTv.setOnClickListener(this);
        IV_IDcardcoud.setOnClickListener(this);
        IV_IDcardvideocoud.setOnClickListener(this);
        IV_businesscardcoud.setOnClickListener(this);
        IV_businesscardvideocoud.setOnClickListener(this);

//        mGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if(mMaleRadioButton.isChecked())
//                    gender = "Male";
//                else
//                    gender = "Female";
//            }
//        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        stringDate = view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth();

        datePicker.setText(view.getDayOfMonth() + " / " + (view.getMonth() + 1) + " / " + view.getYear());
    }

    /*all field validation*/
    public void validation() {
        if (etlName.getText().toString().equals("")) {
            Utils.showAlert(getActivity().getString(R.string.last_name), mContext);
        } else if (etfName.getText().toString().equals("")) {
            Utils.showAlert(getActivity().getString(R.string.first_name), mContext);
        } else if (datePicker.getText().toString().equals("")) {
            Utils.showAlert(getActivity().getString(R.string.dob), mContext);

        } else if (etPhoneNo.getText().toString().equals("")) {

            Utils.showAlert(getActivity().getString(R.string.phone_number), mContext);

        } else if (etPhoneNo.getText().length() < 10) {

            Utils.showAlert(getActivity().getString(R.string.phone_number_10), mContext);

        } else if (etEmail.getText().toString().equals("")) {

            Utils.showAlert(getActivity().getString(R.string.email), mContext);

        } else if (gender.equals("")) {

            Utils.showAlert(getActivity().getString(R.string.Male_female), mContext);

        } else if (etPassword.getText().toString().equals("")) {
            Utils.showAlert(getActivity().getString(R.string.password), mContext);

        } else if (filePathprofilePic == null) {
            Utils.showAlert(getActivity().getString(R.string.profile_pic), mContext);

        }  else if (etCPassword.getText().toString().equals("")) {
            Utils.showAlert(getActivity().getString(R.string.con_password), mContext);
        } else if (!etCPassword.getText().toString().equalsIgnoreCase(etPassword.getText().toString())) {
            Utils.showAlert(getActivity().getString(R.string.password_not_same), mContext);

        } else if (!Utils.isValidMail(etEmail.getText().toString())) {
            Utils.showAlert(getActivity().getString(R.string.valid_email_errer), mContext);
        } else if (userType.equalsIgnoreCase("Cops") && filePathidCard_1 == null) {
                Utils.showAlert(getActivity().getString(R.string.card1), mContext);
            } else if (userType.equalsIgnoreCase("Cops") && filePathidCard_2 == null) {
                Utils.showAlert(getActivity().getString(R.string.card2), mContext);
            } else if (userType.equalsIgnoreCase("Cops") && fileBusCard_1 == null) {
                Utils.showAlert(getActivity().getString(R.string.bussiness1), mContext);
            } else if (userType.equalsIgnoreCase("Cops") && fileBusCard_2 == null) {
                Utils.showAlert(getActivity().getString(R.string.bussiness2), mContext);
            }else{
            try {
                mAppSession.saveData("latitude", String.valueOf(latitude));
                mAppSession.saveData("longitude", String.valueOf(longitude));

                Utils.hideKeyboard(getActivity());
                RegistationPjoSetData registationPjoSetData = new RegistationPjoSetData();
                registationPjoSetData.setFirst_name(etfName.getText().toString().trim());
                registationPjoSetData.setLast_name(etlName.getText().toString().trim());
                registationPjoSetData.setDate_of_birth(stringDate);
                registationPjoSetData.setGender(gender);
                registationPjoSetData.setEmail_id(etEmail.getText().toString().trim());
                registationPjoSetData.setRef_user_type_id(userTypeRegistation);
                registationPjoSetData.setPhone_number(etPhoneNo.getText().toString().trim());
                registationPjoSetData.setUser_password(etPassword.getText().toString().trim());
                registationPjoSetData.setFcm_token(mAppSession.getData("fcm_token"));
                registationPjoSetData.setdevice_language(mAppSession.getData("devicelanguage"));
                //   registationPjoSetData.setReg_latitude(String.valueOf(latitude));

                registationPjoSetData.setReg_longitude(String.valueOf(longitude));
                registationPjoSetData.setReg_latitude(String.valueOf(latitude));
                registationPjoSetData.setUser_password(etPassword.getText().toString().trim());
                registationPjoSetData.setDevice_id(Utils.getDeviceId(mContext));


                if (EasyPermissions.hasPermissions(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    progressDialog.show();
                    File filePrrofile;
                    File fileId_1;
                    File fileId_2;
                    File file_bus_1;
                    File file_bus_2;

                    MultipartBody.Part fileToUploadProfile = null;
                    MultipartBody.Part fileToUploadIdCard_1 = null;
                    MultipartBody.Part fileToUploadIdCard_2 = null;
                    MultipartBody.Part fileToUploadProfileBusCard_1 = null;
                    MultipartBody.Part fileToUploadProfileBusCard_2 = null;

                    if (filePathprofilePic != null) {
                       /* Bitmap bitmapImage = BitmapFactory.decodeFile(filePathprofilePic);
                        int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);*/

                        filePrrofile = new File(filePathprofilePic);
                        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), filePrrofile);
                        fileToUploadProfile = MultipartBody.Part.createFormData("profile_image", filePrrofile.getName(), mFile);
                    }


                    if (filePathidCard_1 != null) {
                     //   filePathidCard_1 = Utils.getRealPathFromURIPath(filePathidCard_1, getActivity());
                        fileId_1 = new File(filePathidCard_1);
                        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), fileId_1);
                        fileToUploadIdCard_1 = MultipartBody.Part.createFormData("id_card_front", fileId_1.getName(), mFile);
                    }

                    if (filePathidCard_2 != null) {
                      //  filePathidCard_2 = Utils.getRealPathFromURIPath(idCardUri_2, getActivity());
                        fileId_2 = new File(filePathidCard_2);
                        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), fileId_2);
                        fileToUploadIdCard_2 = MultipartBody.Part.createFormData("id_card_back", fileId_2.getName(), mFile);
                    }

                    if (fileBusCard_1 != null) {
                      //  fileBusCard_1 = Utils.getRealPathFromURIPath(idBusinessCardUri_1, getActivity());
                        file_bus_1 = new File(fileBusCard_1);

                        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file_bus_1);
                        fileToUploadProfileBusCard_1 = MultipartBody.Part.createFormData("business_card_front", file_bus_1.getName(), mFile);
                    }

                    if (fileBusCard_2 != null) {
                     //   fileBusCard_2 = Utils.getRealPathFromURIPath(idBusinessCardUri_2, getActivity());
                        file_bus_2 = new File(fileBusCard_2);
                        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file_bus_2);
                        fileToUploadProfileBusCard_2 = MultipartBody.Part.createFormData("business_card_back", file_bus_2.getName(), mFile);
                    }
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(registationPjoSetData)));
                    Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(registationPjoSetData)));
                    Service uploadImage = ApiUtils.getAPIService();
                    Call<RegistationPojo> fileUpload = uploadImage.registationWithuploadFile(fileToUploadProfile, fileToUploadIdCard_1, fileToUploadIdCard_2, fileToUploadProfileBusCard_1, fileToUploadProfileBusCard_2, mFile);
                    fileUpload.enqueue(new Callback<RegistationPojo>() {
                        @Override
                        public void onResponse(Call<RegistationPojo> call, Response<RegistationPojo> response) {
                            try {
                                if (response.body() != null) {
                                    RegistationPojo registrationResponse = response.body();
                                    if (registrationResponse.getStatus().equals("false")) {

                                        if(registrationResponse.getMessage().equalsIgnoreCase(getString(R.string.eamil_taken))){
                                            Utils.showAlert(getString(R.string.eamil_taken), mContext);
                                        }else{
                                            Utils.showAlert(registrationResponse.getMessage(), mContext);
                                        }

                                    } else {

                                        mAppSession.saveData("id", registrationResponse.getId());
                                        mAppSession.saveData("name", registrationResponse.getUsername());
                                        mAppSession.saveData("userType", userType);
                                        mAppSession.saveData("image_url", registrationResponse.getProfile_url());
                                        mAppSession.saveData("grade", registrationResponse.getGrade());
                                        mAppSession.saveData("profile_qrcode", registrationResponse.getProfile_qrcode());

                                        if (mAppSession.getData("userType").equalsIgnoreCase("Citizen")) {
                                            Utils.fragmentCall(new AuthenticateCodeFragment(userType, registrationResponse), getFragmentManager());
                                        } else {
                                            Utils.fragmentCall(new LoginFragment(mAppSession.getData("userType")), getFragmentManager());
                                        }


                                        getActivity().startService(new Intent(getActivity(), TrackingServices.class));
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
                        public void onFailure(Call<RegistationPojo> call, Throwable t) {
                            Log.d(TAG, "Error " + t.getMessage());
                            progressDialog.dismiss();
                            Utils.showAlert(t.getMessage(), mContext);
                        }
                    });
                } else {
                    EasyPermissions.requestPermissions(this, getString(R.string.read_file), Utils.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
                Utils.showAlert(e.getMessage(), mContext);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //verify if the image was gotten successfully
        if (requestCode == CAMERA && resultCode == RESULT_OK) {


            if (IDCARD_1 == 1) {

               // filePathidCard_1=filePathidCard_1;
                       // idCardUri_1 = Utils.getImageUri(mContext, thumbnail);
                       IDCARD_1 = 0;
                    } else if (IDCARD_2 == 2) {
                      //  idCardUri_2 = Utils.getImageUri(mContext, thumbnail);
                       IDCARD_2 = 0;


            //    filePathidCard_2=profilePicUri;

                    } else if (IDBUSINESSCARD_1 == 1) {
                    //    idBusinessCardUri_1 = Utils.getImageUri(mContext, thumbnail);
                        IDBUSINESSCARD_1 = 0;
             //   fileBusCard_1=profilePicUri;
                    } else if (IDBUSINESSCARD_2 == 2) {
                      //  idBusinessCardUri_2 = Utils.getImageUri(mContext, thumbnail);
                        IDBUSINESSCARD_2 = 0;

             //   fileBusCard_2=profilePicUri;
                    } else {

                Bitmap bitmap = BitmapFactory.decodeFile(profilePicUri);

                filePathprofilePic=profilePicUri;
              //  mImageView.setImageBitmap(bitmap);
                IVprofilephoto.setImageBitmap(bitmap);
            //    profilePicUri = Utils.getRealPathFromURIPath(profileUri, getActivity());
              //  filePathImage=mCurrentPhotoPath;

                   //     IVprofilephoto.setImageBitmap(thumbnail);

                      //  Bitmap b=scaleBitmap(thumbnail,400,800);
                     //   profileUri = Utils.getImageUri(getActivity(), b);
                     //   profilePicUri = Utils.getRealPathFromURIPath(profileUri, getActivity());
                    }





        } else if (requestCode == GALLERY && resultCode == RESULT_OK) {



            if (data != null) {

                try {

                    if (IDCARD_1 == 1) {
                        //idCardUri_1 = Utils.getImageUri(mContext, thumbnail);
                        Uri contentURI = data.getData();
                        //added by bbh
                       /* Bitmap bitmapImage = BitmapFactory.decodeFile("Your path");
                        int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);*/

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        File file = new File(profilePicUri1);
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                 //       idCardUri_1 = Utils.getImageUri(getActivity(), bitmap);

                        filePathidCard_1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        //  profilePicUri = Utils.getRealPathFromURIPath(profileUri, getActivity());
                        IDCARD_1 = 0;
                    } else if (IDCARD_2 == 2) {
                        Uri contentURI = data.getData();
                        //  idCardUri_2 = Utils.getImageUri(mContext, thumbnail);


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        File file = new File(profilePicUri1);
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                        //idCardUri_2 = Utils.getImageUri(getActivity(), bitmap);
                        filePathidCard_2 = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        IDCARD_2 = 0;

                    } else if (IDBUSINESSCARD_1 == 1) {
                        Uri contentURI = data.getData();
                        //   idBusinessCardUri_1 = Utils.getImageUri(mContext, thumbnail);

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        File file = new File(profilePicUri1);
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                        //idBusinessCardUri_1 = Utils.getImageUri(getActivity(), bitmap);
                        fileBusCard_1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        IDBUSINESSCARD_1 = 0;
                    } else if (IDBUSINESSCARD_2 == 2) {
                        Uri contentURI = data.getData();
                        //idBusinessCardUri_2 = Utils.getImageUri(mContext, thumbnail);

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        File file = new File(profilePicUri1);
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                      //  idBusinessCardUri_2 = Utils.getImageUri(getActivity(), bitmap);
                        fileBusCard_2 = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        IDBUSINESSCARD_2 = 0;
                    } else {
                        Uri contentURI = data.getData();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        File file = new File(profilePicUri1);
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.close();
                        profileUri = Utils.getImageUri(getActivity(), bitmap);
                     //   profilePicUri = Utils.getRealPathFromURIPath(profileUri, getActivity());
                        filePathprofilePic = Utils.getRealPathFromURIPath(contentURI, getActivity());
                        // Toast.makeText(mContext, "Image Saved!", Toast.LENGTH_SHORT).show();
                        IVprofilephoto.setImageBitmap(bitmap);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    //  Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
            //    if (data.getData() != null) {
            //create destination directory
//            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/CopOps/videos");
//            if (f.mkdirs() || f.isDirectory())
//                //compress and output new video specs
//                new HandrailFragment.VideoCompressAsyncTask(mContext).execute(mCurrentVideoPath, f.getPath());

            //   }
        }

    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALLERY) {
//
//            if (data != null) {
//                Uri contentURI = data.getData();
//                try {
//
//                    if (IDCARD_1 == 1) {
//                        //idCardUri_1 = Utils.getImageUri(mContext, thumbnail);
//
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
//                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
//                        File file = new File(profilePicUri1);
//                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
//                        os.close();
//                        idCardUri_1 = Utils.getImageUri(getActivity(), bitmap);
//                        //  profilePicUri = Utils.getRealPathFromURIPath(profileUri, getActivity());
//                        IDCARD_1 = 0;
//                    } else if (IDCARD_2 == 2) {
//                        //  idCardUri_2 = Utils.getImageUri(mContext, thumbnail);
//
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
//                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
//                        File file = new File(profilePicUri1);
//                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
//                        os.close();
//                        idCardUri_2 = Utils.getImageUri(getActivity(), bitmap);
//                        IDCARD_2 = 0;
//
//                    } else if (IDBUSINESSCARD_1 == 1) {
//                        //   idBusinessCardUri_1 = Utils.getImageUri(mContext, thumbnail);
//
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
//                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
//                        File file = new File(profilePicUri1);
//                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
//                        os.close();
//                        idBusinessCardUri_1 = Utils.getImageUri(getActivity(), bitmap);
//                        IDBUSINESSCARD_1 = 0;
//                    } else if (IDBUSINESSCARD_2 == 2) {
//                        //idBusinessCardUri_2 = Utils.getImageUri(mContext, thumbnail);
//
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
//                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
//                        File file = new File(profilePicUri1);
//                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
//                        os.close();
//                        idBusinessCardUri_2 = Utils.getImageUri(getActivity(), bitmap);
//                        IDBUSINESSCARD_2 = 0;
//                    } else {
//
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
//                        String profilePicUri1 = Utils.getRealPathFromURIPath(contentURI, getActivity());
//                        File file = new File(profilePicUri1);
//                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
//                        os.close();
//                        profileUri = Utils.getImageUri(getActivity(), bitmap);
//                        profilePicUri = Utils.getRealPathFromURIPath(profileUri, getActivity());
//                        // Toast.makeText(mContext, "Image Saved!", Toast.LENGTH_SHORT).show();
//                        IVprofilephoto.setImageBitmap(bitmap);
//
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    //  Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        } else if (requestCode == CAMERA) {
//            try {
//
//
//                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//
//                if (thumbnail != null) {
//
//
//
//
//                    if (IDCARD_1 == 1) {
//                       // idCardUri_1 = Utils.getImageUri(mContext, thumbnail);
//                       // IDCARD_1 = 0;
//                    } else if (IDCARD_2 == 2) {
//                      //  idCardUri_2 = Utils.getImageUri(mContext, thumbnail);
//                      //  IDCARD_2 = 0;
//
//                    } else if (IDBUSINESSCARD_1 == 1) {
//                    //    idBusinessCardUri_1 = Utils.getImageUri(mContext, thumbnail);
//                     //   IDBUSINESSCARD_1 = 0;
//                    } else if (IDBUSINESSCARD_2 == 2) {
//                      //  idBusinessCardUri_2 = Utils.getImageUri(mContext, thumbnail);
//                     //   IDBUSINESSCARD_2 = 0;
//                    } else {
//
//                        IVprofilephoto.setImageBitmap(thumbnail);
//
//                      //  Bitmap b=scaleBitmap(thumbnail,400,800);
//                     //   profileUri = Utils.getImageUri(getActivity(), b);
//                     //   profilePicUri = Utils.getRealPathFromURIPath(profileUri, getActivity());
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }




    private void dispatchTakePictureIntent() {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");*/


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createMediaFile(Utils.TYPE_IMAGE);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("LOG_TAG", "Error occurred while creating the file");

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {





                if (IDCARD_1 == 1) {
                //    idCardUri_1 = Utils.getImageUri(mContext, thumbnail);

                    idCardUri_1 = FileProvider.getUriForFile(mContext,
                            Utils.FILE_PROVIDER_AUTHORITY, photoFile);

                    Log.d("LOG_TAG", "Log1: " + String.valueOf(idCardUri_1));

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, idCardUri_1);
                   // IDCARD_1 = 0;
                } else if (IDCARD_2 == 2) {
                   // idCardUri_2 = Utils.getImageUri(mContext, thumbnail);
                    idCardUri_2 = FileProvider.getUriForFile(mContext,
                            Utils.FILE_PROVIDER_AUTHORITY, photoFile);

                    Log.d("LOG_TAG", "Log1: " + String.valueOf(idCardUri_2));

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, idCardUri_2);

                  //  IDCARD_2 = 0;

                } else if (IDBUSINESSCARD_1 == 1) {
                  //  idBusinessCardUri_1 = Utils.getImageUri(mContext, thumbnail);

                    idBusinessCardUri_1 = FileProvider.getUriForFile(mContext,
                            Utils.FILE_PROVIDER_AUTHORITY,
                            photoFile);

                 //   Log.d("LOG_TAG", "Log1: " + String.valueOf(idBusinessCardUri_1));

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, idBusinessCardUri_1);
                 //   IDBUSINESSCARD_1 = 0;
                } else if (IDBUSINESSCARD_2 == 2) {
                    idBusinessCardUri_2 = FileProvider.getUriForFile(mContext,
                            Utils.FILE_PROVIDER_AUTHORITY,
                            photoFile);

                  //  Log.d("LOG_TAG", "Log1: " + String.valueOf(idBusinessCardUri_2));

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, idBusinessCardUri_2);
                  //  idBusinessCardUri_2 = Utils.getImageUri(mContext, thumbnail);
               //     IDBUSINESSCARD_2 = 0;
                }else{
                    profileUri = FileProvider.getUriForFile(mContext,
                            Utils.FILE_PROVIDER_AUTHORITY,
                            photoFile);

                    Log.d("LOG_TAG", "Log1: " + String.valueOf(profileUri));

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, profileUri);
                }

                // Get the content URI for the image file


                startActivityForResult(takePictureIntent, CAMERA);

            }
        }
    }


// Show only images, no videos or anything else


    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Utils.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext.getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(mContext.getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            Utils.showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                Utils.requestPermission(getActivity());
                                            }
                                        }
                                    }, mContext);
                        }
                    }
                }
                break;
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    /////All click listners
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Rltoolbar:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
                break;


            case R.id.mailIdTv:
                IVgreenmam.setImageResource(R.mipmap.img_green_dot);
                IVgreenwoman.setImageResource(R.mipmap.img_white_dot);
                gender = "Male";
                break;


            case R.id.femailId:
                IVgreenwoman.setImageResource(R.mipmap.img_green_dot);
                IVgreenmam.setImageResource(R.mipmap.img_white_dot);
                gender = "Female";
                break;

            case R.id.Etdate:
                Utils.hideKeyboard(getActivity());
                DialogFragment newFragment = new MyDatePickerFragment(dateSetListener);
                newFragment.show(getActivity().getSupportFragmentManager(), "date picker");
                break;
            case R.id.IVcamera:
                if (Utils.checkPermission(mContext)) {
                    //main logic or main code
                   // takePhotoFromCamera();

                    dispatchTakePictureIntent();

                    // . write your main code to execute, It will execute if the permission is already given.

                } else {
                    Utils.requestPermission(getActivity());
                }
                break;
            case R.id.IVgallery:
                if (Utils.checkPermission(mContext)) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
                        choosePhotoFromGallary();
                    }
                } else {

                    choosePhotoFromGallary();
                }


                break;
            case R.id.IV_IDcardcoud:
                if (Utils.checkPermission(mContext)) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
                        choosePhotoFromGallary();
                        IDCARD_1 = 1;
                    }
                } else {

                    choosePhotoFromGallary();
                    IDCARD_1 = 1;
                }

                break;

            case R.id.IV_IDcardvideocoud:
                if (Utils.checkPermission(mContext)) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
                        choosePhotoFromGallary();
                        IDCARD_2 = 2;
                    }
                } else {

                    choosePhotoFromGallary();
                    IDCARD_2 = 2;
                }

                break;

            case R.id.IV_businesscardcamera:
                if (Utils.checkPermission(mContext)) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
                        IDBUSINESSCARD_1 = 1;
                        dispatchTakePictureIntent();
                    }
                } else {
                    IDBUSINESSCARD_1 = 1;
                    dispatchTakePictureIntent();
                }
                break;

            case R.id.IV_businesscardcoud:
                if (Utils.checkPermission(mContext)) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
                        IDBUSINESSCARD_1 = 1;
                        choosePhotoFromGallary();
                    }
                } else {
                    IDBUSINESSCARD_1 = 1;
                    choosePhotoFromGallary();
                }
                break;


            case R.id.IV_businesscardvideocoud:
                if (Utils.checkPermission(mContext)) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
                        IDBUSINESSCARD_2 = 2;
                        choosePhotoFromGallary();
                    }
                } else {
                    IDBUSINESSCARD_2 = 2;
                    choosePhotoFromGallary();
                }
                break;

            case R.id.IV_businesscardvideocamera:
                if (Utils.checkPermission(mContext)) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
                        IDBUSINESSCARD_2 = 2;
                        dispatchTakePictureIntent();
                    }
                } else {
                    IDBUSINESSCARD_2 = 2;
                    dispatchTakePictureIntent();
                }
                break;


            case R.id.IV_IDcardvideocamera:
                if (Utils.checkPermission(mContext)) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
                        IDCARD_2 = 2;
                        dispatchTakePictureIntent();
                    }
                } else {
                    IDCARD_2 = 2;

                    dispatchTakePictureIntent();
                }
                break;

            case R.id.IV_IDcardcamera:

                if (Utils.checkPermission(mContext)) {
                    if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
                        IDCARD_1 = 1;
                       // takePhotoFromCamera();
                        dispatchTakePictureIntent();
                    }
                } else {

                    IDCARD_1 = 1;
                 //   takePhotoFromCamera();
                    dispatchTakePictureIntent();

                }

                break;

            case R.id.RLnext:
                if (Utils.checkConnection(mContext))
                    validation();
                else
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), mContext);
                break;

        }
    }


    private boolean checkPermission() {
        boolean check = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!check) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        }
        return true;
//    }
    }

    private boolean gpsEnabled() {
        isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            return false;
        }
        return true;
    }


    ////Manish
    private final android.location.LocationListener mLocationListener = new android.location.LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                // mCurrentLocation = location;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //  initMapFragment();
            } else {
                Toast.makeText(getActivity(), "Location is not available now", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }



    private File createMediaFile(int type) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName;
        File file;
        File storageDir;
        if(type==Utils.TYPE_IMAGE) {
            fileName ="JPEG_" + timeStamp + "_";
        }else{
            fileName ="VID_" + timeStamp + "_";
        }
        if(type==1) {


            if (IDCARD_1 == 1) {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                file = File.createTempFile(fileName,  ".jpg" ,storageDir      /* directory */);

                filePathidCard_1 = file.getAbsolutePath();
            } else if (IDCARD_2 == 2) {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                file = File.createTempFile(fileName,  ".jpg" ,storageDir      /* directory */);
                filePathidCard_2 = file.getAbsolutePath();

            } else if (IDBUSINESSCARD_1 == 1) {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                file = File.createTempFile(fileName,  ".jpg" ,storageDir      /* directory */);
                fileBusCard_1 = file.getAbsolutePath();

            } else if (IDBUSINESSCARD_2 == 2) {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                file = File.createTempFile(fileName,  ".jpg" ,storageDir      /* directory */);
                fileBusCard_2 = file.getAbsolutePath();
            }else{
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                file = File.createTempFile(fileName,  ".jpg" ,storageDir      /* directory */);

                profilePicUri = file.getAbsolutePath();
            }


        }else{

            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

            file = File.createTempFile(fileName,  ".mp4" ,storageDir      /* directory */);

        }


        return file;
    }
}
