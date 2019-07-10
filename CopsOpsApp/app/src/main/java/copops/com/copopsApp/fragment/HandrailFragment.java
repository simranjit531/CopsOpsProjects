package copops.com.copopsApp.fragment;


import android.Manifest;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.shortcut.ShortcutViewService;
import copops.com.copopsApp.shortcut.ShortcutViewService_Citizen;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
/**
 * Created by Ranjan Gupta
 */

public class HandrailFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.Rlnext)
    RelativeLayout Rlnext;

    @BindView(R.id.ETotherinfoincident)
    EditText ETotherinfoincident;

    @BindView(R.id.ETobjects)
    EditText ETobjects;

    private int GALLERY = 1, CAMERA = 2;
    Context mContext;
    @BindView(R.id.llcamera)
    LinearLayout llcamera;
    ProgressDialog progressDialog;

    @BindView(R.id.llvideo)
    LinearLayout llvideo;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    AppSession mAppSession;
    String filePathVideo;
    String filePathImage;
    String mCurrentPhotoPath;
    String mCurrentVideoPath;
    Uri capturedUri = null;
    Uri compressUri = null;
    LocationManager mLocationManager;
    private boolean isNetworkEnabled;
    private boolean isGpsEnabled;
    public HandrailFragment() {
        // Required empty public constructor
    }

    double longitude;
    double latitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_handrail, container, false);

        ButterKnife.bind(this, view);
        Utils.statusCheck(getActivity());
        mContext = getActivity();
        mAppSession = mAppSession.getInstance(mContext);
        mAppSession.saveData("shortcutscreentype", "");
        getActivity().stopService(new Intent(getActivity(), ShortcutViewService_Citizen.class));

        Rlnext.setOnClickListener(this);
        llcamera.setOnClickListener(this);
        llvideo.setOnClickListener(this);
        Rltoolbar.setOnClickListener(this);

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

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Rlnext:
                if (Utils.checkConnection(getActivity()))
                    validation();
                else
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
                break;

            case R.id.llcamera:
                if (Utils.checkPermission(mContext)) {
                    //main logic or main code
                    getActivity().stopService(new Intent(getActivity(), ShortcutViewService.class));
                    //  takePhotoFromCamera();
                    dispatchTakePictureIntent();

                    // . write your main code to execute, It will execute if the permission is already given.

                } else {
                    Utils.requestPermission(getActivity());
                }
                break;


            case R.id.llvideo:
                if (Utils.checkPermission(mContext)) {
                    getActivity().stopService(new Intent(getActivity(), ShortcutViewService.class));
                    dispatchTakeVideoIntent();


                } else {
                    Utils.requestPermission(getActivity());
                }
                break;

            case R.id.Rltoolbar:
                getFragmentManager().popBackStackImmediate();
                Utils.fragmentCall(new CitizenFragment(), getFragmentManager());

                break;

        }
    }


    public void validation() {
        if (ETobjects.getText().toString().trim().equalsIgnoreCase("")) {
            Utils.showAlert(getActivity().getString(R.string.objecttext), getActivity());
        } else if (ETotherinfoincident.getText().toString().trim().equalsIgnoreCase("")) {
            Utils.showAlert(getActivity().getString(R.string.objecttextdes), getActivity());
        } else {
            try {

                Utils.hideKeyboard(getActivity());
                IncdentSetPojo mIncdentSetPojo = new IncdentSetPojo();
                mIncdentSetPojo.setDescription(ETotherinfoincident.getText().toString().trim());
                mIncdentSetPojo.setobjects(ETobjects.getText().toString().trim());
                mIncdentSetPojo.setCreated_by(mAppSession.getData("id"));
                mIncdentSetPojo.setHandrail_lat(String.valueOf(latitude));
                mIncdentSetPojo.setHandrail_lng(String.valueOf(longitude));
                mIncdentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
                mIncdentSetPojo.setdevice_language(Utils.getDeviceId(getActivity()));


                if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    File incedint_image_file;
                    File incedint_video_file;

                    MultipartBody.Part incedint_image = null;
                    MultipartBody.Part incedint_video = null;
                    if (filePathImage != null) {
                        incedint_image_file = new File(filePathImage);
                        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), incedint_image_file);
                        incedint_image = MultipartBody.Part.createFormData("handrail_image", incedint_image_file.getName(), mFile);
                    }

                    if (filePathVideo != null) {
                        incedint_video_file = new File(filePathVideo);
                        RequestBody mFile = RequestBody.create(MediaType.parse("video/*"), incedint_video_file);
                        incedint_video = MultipartBody.Part.createFormData("handrail_video", incedint_video_file.getName(), mFile);
                    }
                    Log.e("aaaa", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mIncdentSetPojo)));
                    RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(mIncdentSetPojo)));


                    Utils.fragmentCall(new HandrailSignatureFragment(incedint_image, incedint_video, mFile, filePathImage, filePathVideo), getFragmentManager());
                } else {
                    EasyPermissions.requestPermissions(this, getString(R.string.read_file), Utils.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }


    }

    // Method which will process the captured image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //verify if the image was gotten successfully
        if (requestCode == Utils.REQUEST_TAKE_CAMERA_PHOTO && resultCode == RESULT_OK) {
            filePathImage=mCurrentPhotoPath;
//            new ImageCompressionAsyncTask(mContext).execute(mCurrentPhotoPath,
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/CopOps/images");

        } else if (requestCode == Utils.REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
        //    if (data.getData() != null) {
                //create destination directory
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/CopOps/videos");
                if (f.mkdirs() || f.isDirectory())
                    //compress and output new video specs
                    new VideoCompressAsyncTask(mContext).execute(mCurrentVideoPath, f.getPath());

         //   }
        }

    }

    /**
     *
     */
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

    /**
     *
     * @param type
     * @return
     * @throws IOException
     */
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

    /**
     *
     */

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

    /**
     *
     */
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

    /**
     *
     * @param msg
     */
    public void initDialog(String msg) {
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
