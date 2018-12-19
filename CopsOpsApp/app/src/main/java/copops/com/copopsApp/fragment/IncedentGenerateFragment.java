package copops.com.copopsApp.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.IncedentAcceptResponse;
import copops.com.copopsApp.pojo.IncidentSubPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.Utils;
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
public class IncedentGenerateFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.RLenvoyer)
    RelativeLayout RLenvoyer;
    @BindView(R.id.ETdescribetheincident)
    EditText ETdescribetheincident;
    @BindView(R.id.ETotherinfoincident)
    EditText ETotherinfoincident;

    @BindView(R.id.CBburglary)
    CheckBox CBburglary;


    @BindView(R.id.llcamera)
    LinearLayout llcamera;

    @BindView(R.id.llvideo)
    LinearLayout llvideo;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;


    ProgressDialog progressDialog;

    IncidentSubPojo incidentSubPojo;
    String incedentTypeId;

    LocationManager  mLocationManager;
    int pos;
    Context mContext;
    String userId;
    String filePathVideo;
    String filePathImage;
    String mCurrentPhotoPath;
    String mCurrentVideoPath;
    Uri capturedUri = null;
    Uri compressUri = null;
    String screeen;
    String strparentname="";
    private boolean isNetworkEnabled;
    private boolean isGpsEnabled;
    double longitude;
    double latitude;

    public IncedentGenerateFragment(String incedentTypeId, IncidentSubPojo incidentSubPojo, int pos,String userId,String screeen) {
        this.incedentTypeId = incedentTypeId;
        this.incidentSubPojo = incidentSubPojo;
        this.pos = pos;
        this.userId = userId;
        this.screeen = screeen;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_incedent_generate, container, false);
        ButterKnife.bind(this, v);
        mContext=getActivity();

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission() && gpsEnabled()) {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                        10, mLocationListener);
            } else {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        10, mLocationListener);
            }
        }
//        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
//        if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//        } else {
//            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//        }
//        LocationManager lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        longitude = location.getLongitude();
//        latitude = location.getLatitude();

        Log.e("parent ID",""+ incidentSubPojo.getData().get(pos).getIncident_parent_id());

        if (Integer.parseInt(incidentSubPojo.getData().get(pos).getIncident_parent_id())==1){
            strparentname="police";
        }else if (Integer.parseInt(incidentSubPojo.getData().get(pos).getIncident_parent_id())==2){
            strparentname="medical";
        }else if (Integer.parseInt(incidentSubPojo.getData().get(pos).getIncident_parent_id())==3){
            strparentname="city";
        }


        CBburglary.setText("     " + incidentSubPojo.getData().get(pos).getIncident_name());
        onClick();
        return v;
    }

    private void  onClick(){
        RLenvoyer.setOnClickListener(this);
        llcamera.setOnClickListener(this);
        llvideo.setOnClickListener(this);
        Rltoolbar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLenvoyer:
                if (Utils.checkConnection(getActivity()))
                    validation();
                else
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                break;
            case R.id.Rltoolbar:

                if(screeen=="1"){

                    getFragmentManager().popBackStack(  getFragmentManager().getBackStackEntryAt(  getFragmentManager().getBackStackEntryCount()-2).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                 //   getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Utils.fragmentCall(new GPSPublicFragment(), getFragmentManager());

                }else{
                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStackImmediate();
                    }
                }

                break;
            case R.id.llcamera:
                if (Utils.checkPermission(mContext)) {
                    //main logic or main code
                    dispatchTakePictureIntent();

                } else {
                    Utils.requestPermission(getActivity());
                }
                break;

            case R.id.llvideo:
                if (Utils.checkPermission(mContext)) {
                    //main logic or main code
                    dispatchTakeVideoIntent();
                    // . write your main code to execute, It will execute if the permission is already given.

                } else {
                    Utils.requestPermission(getActivity());
                }
                break;
        }
    }

    public void validation() {
        if (ETdescribetheincident.getText().toString().trim().equalsIgnoreCase("")) {
            Utils.showAlert(getActivity().getString(R.string.des), getActivity());
        }/* else if (ETotherinfoincident.getText().toString().trim().equalsIgnoreCase("")) {
            Utils.showAlert(getActivity().getString(R.string.other_des), getActivity());
        }else if (filePathImage == null && filePathVideo == null) {
            Utils.showAlert(getActivity().getString(R.string.path), getActivity());
        }*/

        else {

            initDialog(getActivity().getString(R.string.loading_msg));
            progressDialog.show();
            Utils.hideKeyboard(getActivity());
            IncdentSetPojo mIncdentSetPojo = new IncdentSetPojo();
            mIncdentSetPojo.setRef_incident_category_id(incedentTypeId);
            mIncdentSetPojo.setRef_incident_subcategory_id(incidentSubPojo.getData().get(pos).getIncident_id());
            mIncdentSetPojo.setIncident_description(ETdescribetheincident.getText().toString().trim());
            mIncdentSetPojo.setOther_description(ETotherinfoincident.getText().toString().trim());
            mIncdentSetPojo.setCreated_by(userId);
            mIncdentSetPojo.setIncident_lat(String.valueOf(latitude));
            mIncdentSetPojo.setIncident_lng(String.valueOf(longitude));
            mIncdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
            if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
               // progressDialog.show();
                File incedint_image_file;
                File incedint_video_file;
                MultipartBody.Part incedint_image = null;
                MultipartBody.Part incedint_video = null;
                if (filePathImage != null) {
                    incedint_image_file = new File(filePathImage);
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), incedint_image_file);
                    incedint_image = MultipartBody.Part.createFormData("incident_image", incedint_image_file.getName(), mFile);
                }

                if (filePathVideo != null) {
                    incedint_video_file = new File(filePathVideo);
                    RequestBody mFile = RequestBody.create(MediaType.parse("video/*"), incedint_video_file);
                    incedint_video = MultipartBody.Part.createFormData("incident_video", incedint_video_file.getName(), mFile);
                }

                Log.e("nmnd",EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mIncdentSetPojo)));
                RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mIncdentSetPojo)));

                Service uploadIncedentGenerate = ApiUtils.getAPIService();
                Call<IncedentAcceptResponse> fileUpload = uploadIncedentGenerate.generateIncedent(incedint_image,incedint_video,mFile);
                fileUpload.enqueue(new Callback<IncedentAcceptResponse>() {
                    @Override
                    public void onResponse(Call<IncedentAcceptResponse> call, Response<IncedentAcceptResponse> response) {
                        try {
                            if (response.body() != null){
                                IncedentAcceptResponse incedentAcceptResponse = response.body();
                                if(incedentAcceptResponse.getStatus().equals("false")){
                                    Utils.showAlert(incedentAcceptResponse.getMessage(),mContext);
                                }else{

                                    Utils.fragmentCall(new IncedentReportFragment(incedentAcceptResponse,"incidentreports",strparentname), getFragmentManager());
                                    if(mCurrentPhotoPath!=null) {
                                        File mCurrentPhotodelete = new File(mCurrentPhotoPath);
                                        if (mCurrentPhotodelete.exists()) {
                                            if (mCurrentPhotodelete.delete()) {

                                              //  Log.e("current file Deleted :", mCurrentPhotoPath);
                                            } else {

                                              //  Log.e("currentf not Deleted :", mCurrentPhotoPath);
                                            }
                                        }
                                    }


                                    if(mCurrentVideoPath!=null) {
                                        File mCurrentPhotodeletedeletevideo = new File(mCurrentVideoPath);
                                        if (mCurrentPhotodeletedeletevideo.exists()) {
                                            if (mCurrentPhotodeletedeletevideo.delete()) {
                                             //   Log.e("fileelete Dd :", mCurrentVideoPath);

                                            } else {
                                             //   Log.e("currentf not Deleted :", mCurrentVideoPath);

                                            }
                                        }
                                    }

                                    if(filePathImage!=null) {
                                        File fdelete = new File(filePathImage);
                                        if (fdelete.exists()) {
                                            if (fdelete.delete()) {
                                                // System.out.println("file Deleted :" + filePathImage);
                                            } else {
                                                //  System.out.println("file not Deleted :" + filePathImage);
                                            }
                                        }
                                    }
                                    if(filePathVideo!=null) {
                                        File fdeletevideo = new File(filePathVideo);
                                        if (fdeletevideo.exists()) {
                                            if (fdeletevideo.delete()) {
                                                //   System.out.println("file Deleted :" + filePathImage);
                                            } else {
                                                //   System.out.println("file not Deleted :" + filePathImage);
                                            }
                                        }
                                    }
                                }
                                progressDialog.dismiss();

                            }else{
                                progressDialog.dismiss();
                                Utils.showAlert(response.message(),mContext);
                            }
                        } catch (Exception e)
                        {
                            progressDialog.dismiss();
                            e.getMessage();
                            Utils.showAlert(e.getMessage(),mContext);
                        }
                    }

                    @Override
                    public void onFailure(Call<IncedentAcceptResponse> call, Throwable t) {
                       // Log.d(TAG, "Error " + t.getMessage());
                        progressDialog.dismiss();
                        Utils.showAlert(t.getMessage(),mContext);
                    }
                });
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), Utils.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

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

                // Get the content URI for the image file
                capturedUri = FileProvider.getUriForFile(mContext,
                        Utils.FILE_PROVIDER_AUTHORITY,
                        photoFile);

                Log.d("LOG_TAG", "Log1: " + String.valueOf(capturedUri));

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);

                startActivityForResult(takePictureIntent, Utils.REQUEST_TAKE_CAMERA_PHOTO);

            }
        }
    }


    // Method which will process the captured image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //verify if the image was gotten successfully
        if (requestCode == Utils.REQUEST_TAKE_CAMERA_PHOTO && resultCode == RESULT_OK) {

                new ImageCompressionAsyncTask(mContext).execute(mCurrentPhotoPath,
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/CopOps/images");

        }

        else if (requestCode == Utils.REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
          //  Uri uri=data.getData();

          //  if (uri != null) {
                //create destination directory
                File f = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/CopOps/videos");
                if (f.mkdirs() || f.isDirectory())


                    //compress and output new video specs
                    new VideoCompressAsyncTask(mContext).execute(mCurrentVideoPath, f.getPath());

         //   }
        }

    }
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takeVideoIntent.resolveActivity(mContext.getPackageManager()) != null) {
            try {

                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1800);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                capturedUri = FileProvider.getUriForFile(mContext,
                        Utils.FILE_PROVIDER_AUTHORITY,
                        createMediaFile(Utils.TYPE_VIDEO));

                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);
                Log.d("LOG_TAG", "VideoUri: " + capturedUri.toString());
                startActivityForResult(takeVideoIntent, Utils.REQUEST_TAKE_VIDEO);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


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
             storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            file = File.createTempFile(fileName,  ".jpg" ,storageDir      /* directory */);
            mCurrentPhotoPath = file.getAbsolutePath();
        }else{

            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

            file = File.createTempFile(fileName,  ".mp4" ,storageDir      /* directory */);
            mCurrentVideoPath = file.getAbsolutePath();

        }

        return file;
    }

    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {
        Context mContext;
        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initDialog(getActivity().getString(R.string.compress_msg));
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... paths) {
            filePathVideo = null;
            try {

                filePathVideo = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return filePathVideo;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            progressDialog.dismiss();
            File imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            Log.i("Copops", "Path: " + compressedFilePath);
        }
    }



    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {

            filePathImage = SiliCompressor.with(mContext).compress(params[0], new File(params[1]));
            return filePathImage;



        }

        @Override
        protected void onPostExecute(String s) {

            File imageFile = new File(s);
            compressUri = Uri.fromFile(imageFile);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), compressUri);
                String name = imageFile.getName();
                float length = imageFile.length() / 1024f; // Size in KB
                int compressWidth = bitmap.getWidth();
                int compressHieght = bitmap.getHeight();
                String text = String.format(Locale.US, "Name: %s\nSize: %fKB\nWidth: %d\nHeight: %d", name, length, compressWidth, compressHieght);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void initDialog(String msg)
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(msg);
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
            // displayLocationSettingsRequest(getActivity());
//    if (!isGpsEnabled) {
//            Toast.makeText(m_activity, "GPS is not enabled", Toast.LENGTH_LONG).show();
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
                latitude=location.getLatitude();
                longitude=location.getLongitude();
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
}
