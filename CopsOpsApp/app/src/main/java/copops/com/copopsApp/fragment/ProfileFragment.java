package copops.com.copopsApp.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.LoginPojoSetData;
import copops.com.copopsApp.pojo.OperatorShowAlInfo;
import copops.com.copopsApp.pojo.ProfilePojo;
import copops.com.copopsApp.pojo.RegistationPjoSetData;
import copops.com.copopsApp.pojo.RegistationPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
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
 * Created by Ranjan Gupta
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    @BindView(R.id.rlllll)
    LinearLayout lll;

    @BindView(R.id.Etconfirmpassword)
    EditText newEtconfirmpassword;

    @BindView(R.id.mailIdTv)
    RelativeLayout mailIdTv;

    @BindView(R.id.femailId)
    RelativeLayout femailId;
    @BindView(R.id.Etlastname)
    EditText Etlastname;

    @BindView(R.id.Etfirstname)
    EditText Etfirstname;

    @BindView(R.id.Etdate)
    EditText Etdate;

    @BindView(R.id.Etphonenumber)
    EditText Etphonenumber;


    @BindView(R.id.Etemail)
    EditText Etemail;

    @BindView(R.id.changePassword)
    RelativeLayout changePassword;

    @BindView(R.id.Etpassword)
    EditText Etpassword;

    private String stringDate;
    DatePickerDialog.OnDateSetListener dateSetListener;

    @BindView(R.id.conforpassword)
    EditText conforpassword;

    @BindView(R.id.RLnext)
    RelativeLayout RLnext;

    @BindView(R.id.IVgreenmam)
    ImageView IVgreenmam;

    @BindView(R.id.IVgreenwoman)
    ImageView IVgreenwoman;

    @BindView(R.id.IVcamera)
    ImageView IVcamera;

    @BindView(R.id.IVgallery)
    ImageView IVgallery;

    @BindView(R.id.IVprofilephoto)
    CircleImageView IVprofilephoto;
    private ProgressDialog progressDialog;
    private AppSession mAppSession;
    private int GALLERY = 1, CAMERA = 2;
    private String gender = "";

    private Uri profileUri;
    private String profilePicUri;
    String filePathprofilePic;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mAppSession = mAppSession.getInstance(getContext());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        Rltoolbar.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        RLnext.setOnClickListener(this);
        newEtconfirmpassword.setVisibility(View.GONE);
        lll.setVisibility(View.GONE);

        IVcamera.setOnClickListener(this);
        IVgallery.setOnClickListener(this);

        femailId.setOnClickListener(this);
        mailIdTv.setOnClickListener(this);

        Etdate.setOnClickListener(this);

        dateSetListener = this;

        if (Utils.checkConnection(getActivity())) {
            IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
            incdentSetPojo.setUser_id(mAppSession.getData("id"));
            incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
            Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
            getProfileData(mFile);
        } else {
            Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
        }

    }

    @Override
    public void onClick(View v) {

        switch ((v.getId())) {
            case R.id.Rltoolbar:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
                break;

            case R.id.changePassword:
                // opendialogcustomdialogPassword(getContext(),mAppSession,progressDialog,getFragmentManager());


                Utils.fragmentCall(new ChangePasswordFragment(), getFragmentManager());

                break;
            case R.id.Etdate:
                Utils.hideKeyboard(getActivity());
                DialogFragment newFragment = new MyDatePickerFragment(dateSetListener);
                newFragment.show(getActivity().getSupportFragmentManager(), "date picker");
                break;

            case R.id.RLnext:
                // newEtconfirmpassword.setVisibility(View.GONE);
                //   lll.setVisibility(View.GONE);

                update();
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
            case R.id.IVgallery:
                if (Utils.checkPermission(getContext())) {
                    if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
                        choosePhotoFromGallary();
                    }
                } else {

                    choosePhotoFromGallary();
                }
                break;


            case R.id.IVcamera:
                if (Utils.checkPermission(getContext())) {
                    //main logic or main code
                    // takePhotoFromCamera();

                    dispatchTakePictureIntent();

                    // . write your main code to execute, It will execute if the permission is already given.

                } else {
                    Utils.requestPermission(getActivity());
                }
                break;
        }

    }


    public static void opendialogcustomdialogPassword(Context mContext, AppSession mAppSession, ProgressDialog progressDialog, FragmentManager manager) {

        final Dialog dialog = new Dialog(mContext, R.style.MyDialogTheme1);
        dialog.setContentView(R.layout.change_password_pop_up);
        dialog.setCancelable(false);
        //   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        EditText Etpassword = (EditText) dialog.findViewById(R.id.Etpassword);
        EditText newpassword = (EditText) dialog.findViewById(R.id.newpassword);
        EditText conpassword = (EditText) dialog.findViewById(R.id.conpassword);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        RelativeLayout RLsave = (RelativeLayout) dialog.findViewById(R.id.RLsave);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RLsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Etpassword.getText().toString().equals("")) {
                    Utils.showAlert(mContext.getString(R.string.old_password), mContext);
                } else if (Etpassword.getText().toString().equals("")) {
                    Utils.showAlert(mContext.getString(R.string.new_password), mContext);
                } else if (!conpassword.getText().toString().equalsIgnoreCase(newpassword.getText().toString())) {
                    Utils.showAlert(mContext.getString(R.string.password_not_same), mContext);

                } else if (Utils.checkConnection(mContext)) {
                    LoginPojoSetData mLoginPojoSetData = new LoginPojoSetData();

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
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onResponse(Call<CommanStatusPojo> call, Response<CommanStatusPojo> response) {
                            try {
                                if (response.body() != null) {
                                    CommanStatusPojo operatorShowAlInfo = response.body();
                                    if (operatorShowAlInfo.getStatus().equals("false")) {

                                        dialog.dismiss();
                                    } else {
                                        dialog.dismiss();

                                        if (manager.getBackStackEntryCount() > 0) {
                                            manager.popBackStackImmediate();
                                        }
                                        Utils.fragmentCall(new LoginFragment(mAppSession.getData("copsuser")), manager);


                                    }
                                    progressDialog.dismiss();

                                }

                                dialog.dismiss();

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
                            Utils.showAlert(t.getMessage(), mContext);
                        }
                    });


                } else {
                    Utils.showAlert(mContext.getString(R.string.internet_conection), mContext);
                }

                //  android.os.Process.killProcess(android.os.Process.myPid());
                //   System.exit(1);
            }
        });
        dialog.show();
    }


    private void getProfileData(RequestBody Data) {


        progressDialog.show();
        Service operator = ApiUtils.getAPIService();
        Call<ProfilePojo> getallLatLong = operator.getProfile(Data);
        getallLatLong.enqueue(new Callback<ProfilePojo>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<ProfilePojo> call, Response<ProfilePojo> response) {
                try {
                    if (response.body() != null) {
                        ProfilePojo operatorShowAlInfo = response.body();
                        if (operatorShowAlInfo.getStatus().equals("false")) {

                        } else {
                            if (operatorShowAlInfo.getMessage().getGender().equalsIgnoreCase("Male")) {
                                IVgreenmam.setImageResource(R.mipmap.img_green_dot);
                                gender = operatorShowAlInfo.getMessage().getGender();
                            } else {
                                IVgreenwoman.setImageResource(R.mipmap.img_green_dot);
                                gender = operatorShowAlInfo.getMessage().getGender();
                            }
                            Etemail.setText(operatorShowAlInfo.getMessage().getEmail_id());
                            Etlastname.setText(operatorShowAlInfo.getMessage().getLast_name());
                            Etfirstname.setText(operatorShowAlInfo.getMessage().getFirst_name());
                            Etdate.setText(operatorShowAlInfo.getMessage().getDate_of_birth());
                            Etphonenumber.setText(operatorShowAlInfo.getMessage().getPhone_number());
                            Glide.with(getActivity()).load(mAppSession.getData("image_url")).into(IVprofilephoto);

                        }
                        progressDialog.dismiss();

                    } else {
                        // Utils.showAlert(getString(R.string.Notfound), getActivity());
                        Utils.showAlert(getString(R.string.Notfound), getActivity());
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.getMessage();
                    Utils.showAlert(e.getMessage(), getActivity());
                }
            }

            @Override
            public void onFailure(Call<ProfilePojo> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());
                progressDialog.dismiss();
                Utils.showAlert(t.getMessage(), getActivity());
            }
        });
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }


    private void dispatchTakePictureIntent() {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");*/


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
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


                profileUri = FileProvider.getUriForFile(getContext(),
                        Utils.FILE_PROVIDER_AUTHORITY,
                        photoFile);

                Log.d("LOG_TAG", "Log1: " + String.valueOf(profileUri));

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, profileUri);


                // Get the content URI for the image file


                startActivityForResult(takePictureIntent, CAMERA);

            }
        }
    }

    private File createMediaFile(int type) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName;
        File file = null;
        File storageDir;
        if (type == Utils.TYPE_IMAGE) {
            fileName = "JPEG_" + timeStamp + "_";
        } else {
            fileName = "VID_" + timeStamp + "_";
        }
        if (type == 1) {


            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            file = File.createTempFile(fileName, ".jpg", storageDir      /* directory */);

            profilePicUri = file.getAbsolutePath();


        }


        return file;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //verify if the image was gotten successfully
        if (requestCode == CAMERA && resultCode == RESULT_OK) {


            Bitmap bitmap = BitmapFactory.decodeFile(profilePicUri);

            filePathprofilePic = profilePicUri;
            //  mImageView.setImageBitmap(bitmap);
            IVprofilephoto.setImageBitmap(bitmap);
            //    profilePicUri = Utils.getRealPathFromURIPath(profileUri, getActivity());
            //  filePathImage=mCurrentPhotoPath;

            //     IVprofilephoto.setImageBitmap(thumbnail);

            //  Bitmap b=scaleBitmap(thumbnail,400,800);
            //   profileUri = Utils.getImageUri(getActivity(), b);
            //   profilePicUri = Utils.getRealPathFromURIPath(profileUri, getActivity());


        } else if (requestCode == GALLERY && resultCode == RESULT_OK) {


            if (data != null) {

                try {

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

    public void update() {
        try {
            RegistationPjoSetData registationPjoSetData = new RegistationPjoSetData();
            registationPjoSetData.setFirst_name(Etfirstname.getText().toString().trim());
            registationPjoSetData.setLast_name(Etlastname.getText().toString().trim());
            registationPjoSetData.setDate_of_birth(Etdate.getText().toString());
            registationPjoSetData.setUser_id(mAppSession.getData("id"));
            registationPjoSetData.setGender(gender);
            registationPjoSetData.setEmail_id(Etemail.getText().toString().trim());
            registationPjoSetData.setPhone_number(Etphonenumber.getText().toString().trim());
            registationPjoSetData.setdevice_language(mAppSession.getData("devicelanguage"));
            registationPjoSetData.setDevice_id(Utils.getDeviceId(getContext()));
            if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                progressDialog.show();
                File filePrrofile;
                File fileId_1;
                File fileId_2;
                File file_bus_1;
                File file_bus_2;

                MultipartBody.Part fileToUploadProfile = null;

                if (filePathprofilePic != null) {
                    filePrrofile = new File(filePathprofilePic);
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), filePrrofile);
                    fileToUploadProfile = MultipartBody.Part.createFormData("profile_image", filePrrofile.getName(), mFile);
                }

                RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(registationPjoSetData)));
                Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(registationPjoSetData)));
                Service uploadImage = ApiUtils.getAPIService();
                Call<RegistationPojo> fileUpload = uploadImage.updateProfile(fileToUploadProfile, mFile);
                fileUpload.enqueue(new Callback<RegistationPojo>() {
                    @Override
                    public void onResponse(Call<RegistationPojo> call, Response<RegistationPojo> response) {
                        try {
                            if (response.body() != null) {
                                RegistationPojo registrationResponse = response.body();
                                if (registrationResponse.getStatus().equals("false")) {

                                    Utils.showAlert(registrationResponse.getMessage(), getContext());

                                } else {

                                    //   mAppSession.saveData("Login", "1");
                                    mAppSession.saveData("id", registrationResponse.getId());
                                    mAppSession.saveData("user_id", registrationResponse.getUserid());
                                    mAppSession.saveData("name", registrationResponse.getUsername());
                                    mAppSession.saveData("type", mAppSession.getData("tyep"));
                                    mAppSession.saveData("image_url", registrationResponse.getProfile_url());
                                    mAppSession.saveData("profile_qrcode", registrationResponse.getProfile_qrcode());
                                    mAppSession.saveData("grade", registrationResponse.getGrade());


                                    Glide.with(getActivity()).load(mAppSession.getData("image_url")).into(IVprofilephoto);
                                    //    mAppSession.saveData("freez", "1");


                                    if (Utils.checkConnection(getActivity())) {
                                        IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                                        incdentSetPojo.setUser_id(mAppSession.getData("id"));
                                        incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                                        Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                        RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                        getProfileData(mFile);
                                    } else {
                                        Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                                    }

                                }
                                Utils.showAlert(registrationResponse.getMessage(), getContext());
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
                                Utils.showAlert(response.message(), getContext());
                            }
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.getMessage();
                            Utils.showAlert(e.getMessage(), getContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistationPojo> call, Throwable t) {
                        //   Log.d(TAG, "Error " + t.getMessage());
                        progressDialog.dismiss();
                        Utils.showAlert(t.getMessage(), getContext());
                    }
                });
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), Utils.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            Utils.showAlert(e.getMessage(), getContext());
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String mm = "" + (view.getMonth() + 1);
        String dd = "" + view.getDayOfMonth();
        if (mm.length() == 1) {
            mm = "0" + (view.getMonth() + 1);
        }
        if (dd.length() == 1) {
            dd = "0" + view.getDayOfMonth();
        }
        Etdate.setText(view.getYear() + "-" + mm + "-" + dd);
    }
}