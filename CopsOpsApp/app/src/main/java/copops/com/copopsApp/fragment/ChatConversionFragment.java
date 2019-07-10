package copops.com.copopsApp.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.util.Log;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.iceteck.silicompressorr.SiliCompressor;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.adapter.MessageAdapter;

import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.ChatHolder;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.Utils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ranjan Gupta
 */
@SuppressLint("ValidFragment")
public class ChatConversionFragment extends Fragment implements View.OnClickListener {


    private FFmpeg ffmpeg;

    WebSocketClient mWebSocketClient;
    @BindView(R.id.list_chat_messages)
    RecyclerView list_chat_messages;

    @BindView(R.id.button_chat_send)
    ImageButton button_chat_send;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @BindView(R.id.edit_chat_message)
    EditText edit_chat_message;

    @BindView(R.id.userneameTv)
    TextView userneameTv;

    @BindView(R.id.Rltoolbar)
    RelativeLayout Rltoolbar;

    ArrayList<ChatHolder> size = new ArrayList<>();
    @BindView(R.id.button_chat_attachment)
    ImageButton button_chat_attachment;
    String uploadDoucment;
    String mCurrentPhotoPath;
    AppSession mAppSession;
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    String id;
    String toUserId;
    WebSocket webSocket;
    MessageAdapter mRecentChatAdapter;
    String name;
    String userName;
    private String profilePicUri;
    final Handler pingHandler = new Handler();
    ProgressDialog mProgressDialog;
    String chatType;
    String mCurrentVideoPath;
    String filePathVideo;
    View sheetView;
    BottomSheetDialog mBottomSheetDialog;
    OkHttpClient okHttpClient;
    Runnable pingRunnable;
    private int overallXScrol = 0;
    @BindView(R.id.progressBar4)
    ProgressBar mProgressBar;

    LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    String page = "0";
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    boolean isCheck = false;
    private int overallXScroll = 0;
    int firstItemPos;
    private String filePath;
    /// private WebSocketConnection mConnection = new WebSocketConnection();

    Uri capturedUri = null;
    private ArrayList<ChatHolder> mChatHolders = new ArrayList<>();
    private ArrayList<ChatHolder> mChatHoldersStore = new ArrayList<>();
    int scroll = 0;

    @SuppressLint("ValidFragment")
    public ChatConversionFragment(String id, String userName, String toUserId) {
        this.id = id;
        this.userName = userName;
        this.toUserId = toUserId;
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chat_conversion, container, false);
        ButterKnife.bind(this, view);
        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        sheetView = getActivity().getLayoutInflater().inflate(R.layout.custome_bottom_dilog, null);

        mBottomSheetDialog.setContentView(sheetView);
        mRecentChatAdapter = new MessageAdapter(getActivity(), mChatHolders, name);
        list_chat_messages.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getContext());
        list_chat_messages.setLayoutManager(mLayoutManager);
        //    list_chat_messages.setLayoutManager(new LinearLayoutManager(getContext()));
        list_chat_messages.setItemAnimator(new DefaultItemAnimator());
        NotificationManager notificationManager = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        list_chat_messages.setAdapter(mRecentChatAdapter);
        firstItemPos = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        mAppSession = mAppSession.getInstance(getActivity());
        LinearLayout cameraIdLL = (LinearLayout) sheetView.findViewById(R.id.cameraIdLL);
        LinearLayout galleryid = (LinearLayout) sheetView.findViewById(R.id.galleryid);
        LinearLayout recordingid = (LinearLayout) sheetView.findViewById(R.id.recordingid);
        LinearLayout fileiconId = (LinearLayout) sheetView.findViewById(R.id.fileiconId);
        TextView tv_btn_remove_photo = (TextView) sheetView.findViewById(R.id.tv_btn_remove_photo);
        button_chat_attachment.setOnClickListener(this);

      //  loadFFMpegBinary();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        cameraIdLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                mBottomSheetDialog.dismiss();
            }
        });
        galleryid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                mBottomSheetDialog.dismiss();
            }
        });
        recordingid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakeVideoIntent();
                mBottomSheetDialog.dismiss();
            }
        });

        fileiconId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent4 = new Intent(getContext(), NormalFilePickActivity.class);
                intent4.putExtra(Constant.MAX_NUMBER, 1);
              //  intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf"});
                intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"pdf"});
                startActivityForResult(intent4, 4);
                mBottomSheetDialog.dismiss();
            }
        });

        tv_btn_remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scroll = 1;
                mAppSession.saveData("comeChatnoti", "0");
                scrollSocketData();
                pullToRefresh.setRefreshing(false);
            }
        });
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.loading));
        button_chat_send.setOnClickListener(this);
        Rltoolbar.setOnClickListener(this);
        userneameTv.setText(userName);
        Request request = new Request.Builder().url(Utils.CHAT_BASE_URL).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, listener);
        mAppSession.saveData("Chat", "1");
        seenUnSeen();
        mProgressDialog.show();

        pingRunnable = new Runnable() {
            @Override
            public void run() {


                if (webSocket == null) {
                    Request request = new Request.Builder().url(Utils.CHAT_BASE_URL).build();
                    EchoWebSocketListener listener = new EchoWebSocketListener();
                    okHttpClient = new OkHttpClient();
                    webSocket = okHttpClient.newWebSocket(request, listener);
                    okHttpClient.dispatcher().executorService().shutdown();
                }
                pingHandler.postDelayed(this, 4000);

                String aa = mAppSession.getData("comeChatnoti");
                Log.e("aaa", "" + aa);

                if (mAppSession.getData("comeChatnoti").equalsIgnoreCase("1")) {
                    page = "0";
                    scroll = 0;
                    mAppSession.saveData("comeChatnoti", "0");
                    sendMessageToServerFirst();
                    Log.e("show1", "sucesss");
                } else {
                    Log.e("show1", "fail");
                }
            }
        };
        pingHandler.postDelayed(pingRunnable, 4000);


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_chat_send:
                if (webSocket != null) {

                    if (edit_chat_message.getText().toString().trim().equalsIgnoreCase("")) {
                    } else {
                        chatType = "TEXT";
                        JSONObject jsonObj = new JSONObject();
                        JSONObject jsonObj_1 = new JSONObject();
                        try {
                            jsonObj.put("message", edit_chat_message.getText().toString().trim());
                            jsonObj.put("to_user", id);
                            jsonObj.put("type", "message");
                            jsonObj.put("message_type", chatType);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        try {
                            jsonObj_1.put("username", userName);
                            jsonObj_1.put("id", toUserId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            jsonObj.put("user", jsonObj_1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String jsonStr = jsonObj.toString();

                        Log.e("Register", "" + jsonStr);
                        webSocket.send(jsonStr);
                        edit_chat_message.setText("");
                        page = "0";
                        scroll = 0;
                        sendMessageToServerFirst();
                    }

                }

                break;

            case R.id.button_chat_attachment:
                mBottomSheetDialog.show();
                break;

            case R.id.Rltoolbar:
                if (getFragmentManager().getBackStackEntryCount() == 1) {
                    Utils.fragmentCall(new ChatRecentFragment(), getFragmentManager());
                    mAppSession.saveData("Chat", "0");
                    pingHandler.post(pingRunnable);
                }else{
                    Utils.fragmentCall(new ChatRecentFragment(), getFragmentManager());
                  //  getFragmentManager().popBackStackImmediate();
                    mAppSession.saveData("Chat", "0");
                    pingHandler.post(pingRunnable);
                }

//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStackImmediate();
//
//                    mAppSession.saveData("Chat", "0");
//                    pingHandler.post(pingRunnable);
//                }
                break;
        }

    }


    // WebSocket
    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {

            JSONObject jsonObj = new JSONObject();
            JSONObject jsonObj_1 = new JSONObject();
            try {
                jsonObj.put("to_user", id);
                jsonObj.put("type", "chathistory");
                jsonObj.put("page", "0");
            } catch (JSONException e) {
                e.printStackTrace();

            }
            try {
                jsonObj_1.put("username", userName);
                jsonObj_1.put("id", toUserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                jsonObj.put("user", jsonObj_1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String jsonStr = jsonObj.toString();
            webSocket.send(jsonStr);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.e("first", "" + 2);

            Log.d("firsttext", "" + text);
            if (scroll == 1) {
                outputScroll(text, "1");
            } else {
                output(text, "1");
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Rx bytes: " + bytes.hex(), "2");
            Log.e("first", "" + 3);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closed: " + code + " / " + reason, "3");
            Log.e("first", "" + 4);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error: " + t.getMessage(), "4");
            Log.e("first", "" + 5);
        }
    }

    private void output(final String txt, String no) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("websocketchat", txt);
                    if (no.equalsIgnoreCase("1")) {
                        msg(txt);
                        if (mChatHolders.size() > 0) {
                            mRecentChatAdapter.notifyDataSetChanged();
                            list_chat_messages.smoothScrollToPosition(mRecentChatAdapter.getItemCount() - 1);
                        }
                    }
                    mProgressDialog.dismiss();
                    mProgressBar.setVisibility(View.GONE);

                }
            });
        }
    }
    private void sendMessageToServerFirst() {
        Log.e("chathistory","123");
        JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj_1 = new JSONObject();
        try {
            jsonObj.put("to_user", id);
            jsonObj.put("type", "chathistory");
            jsonObj.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObj_1.put("username", userName);
            jsonObj_1.put("id", toUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObj.put("user", jsonObj_1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonStr = jsonObj.toString();
        webSocket.send(jsonStr);
        seenUnSeen();
        Log.e("First Time", "" + jsonStr);

    }
    private void sendMessageToServerFirstVideo() {
        JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj_1 = new JSONObject();
        try {
            jsonObj.put("to_user", id);
            jsonObj.put("type", "chathistory");
            jsonObj.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        try {
            jsonObj_1.put("username", userName);
            jsonObj_1.put("id", toUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObj.put("user", jsonObj_1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObj.toString();
        webSocket.send(jsonStr);
        seenUnSeen();


    }
    private void seenUnSeen() {
        JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj_1 = new JSONObject();
        try {
            jsonObj.put("to_user", id);
            jsonObj.put("type", "seencount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObj_1.put("username", userName);
            jsonObj_1.put("id", toUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObj.put("user", jsonObj_1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObj.toString();
        webSocket.send(jsonStr);
        Log.e("seen", "" + jsonStr);

    }
    private void msg(String msg) {
        try {
            JSONObject jObj = new JSONObject(msg);
            String type = jObj.getString("type");

            if (type.equals("chathistory")) {
                page = jObj.getString("page");
                JSONArray jsonArray = jObj.getJSONArray("message");
                Log.e("CHAT DATA", jsonArray.toString());
                mChatHolders.clear();

                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mChatHolders.add(new ChatHolder(jsonObject.getString("message"), jsonObject.getString("sender"), jsonObject.getString("receiver"), jsonObject.getString("message_type"), jsonObject.getString("thumb")));
                    }
                    mProgressDialog.dismiss();
                }
                mProgressDialog.dismiss();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }
    }
    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                uploadDoucment = mCurrentPhotoPath;
                chatType = "IMAGE";
                try {
                    Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(mCurrentPhotoPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File file = new File(uploadDoucment);
                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                uloadeDoc(file_size);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                uploadDoucment = picturePath;
                chatType = "IMAGE";
                File file = new File(uploadDoucment);
                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                uloadeDoc(file_size);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
            } else if (requestCode == 3) {
                chatType = "VIDEO";
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/CopOps/videos");
                if (f.mkdirs() || f.isDirectory()) {

                  //  executeCompressCommand(mCurrentVideoPath);
                    new VideoCompressAsyncTask(getActivity()).execute(mCurrentVideoPath, f.getPath());

//                    File file = new File(mCurrentVideoPath);
//                    int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
//
//                    if(file_size<10240) {
//
//                        new VideoCompressAsyncTask(getActivity()).execute(mCurrentVideoPath, f.getPath());
//                    }else{
//                        Utils.showAlert(getString(R.string.checkmb), getContext());
//                    }
                }else{


              //      executeCompressCommand(mCurrentVideoPath);
                    new VideoCompressAsyncTask(getActivity()).execute(mCurrentVideoPath, f.getPath());
//                    File file = new File(mCurrentVideoPath);
//                    int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
//
//                    if(file_size<10240) {
//
//                        new VideoCompressAsyncTask(getActivity()).execute(mCurrentVideoPath, f.getPath());
//                    }else{
//                        Utils.showAlert(getString(R.string.checkmb), getContext());
//                    }
                }
            }
            else if (requestCode == 4) {
                chatType = "PDF";
               // Uri filePath = data.getData();
                String path = null;

                ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                StringBuilder builder = new StringBuilder();

                for (NormalFile file : list) {
                     path = file.getPath();
                    builder.append(path + "\n");
                }
                uploadDoucment = path;
                 File file = new File(path);
                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                uloadeDoc(file_size);
                mBottomSheetDialog.dismiss();
            }
        }
    }
    public void uloadeDoc(int file_size) {
        if(file_size<10240) {
        try {
            if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                File uploadFile;
                MultipartBody.Part doucmentToUploadProfile = null;
                if (uploadDoucment != null) {
                    RequestBody mFile;
                    uploadFile = new File(uploadDoucment);
                    if (uploadFile.getAbsolutePath().contains(".jpg")) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mFile = RequestBody.create(MediaType.parse("image/*"), uploadFile);
                        mProgressBar.setVisibility(View.VISIBLE);
                    } else if (uploadFile.getAbsolutePath().contains(".png")) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mFile = RequestBody.create(MediaType.parse("image/*"), uploadFile);
                    } else if (uploadFile.getAbsolutePath().contains(".pdf")) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mFile = RequestBody.create(MediaType.parse("pdf/*"), uploadFile);
                    } else {
                        mFile = RequestBody.create(MediaType.parse("video/*"), uploadFile);
                    }
                    doucmentToUploadProfile = MultipartBody.Part.createFormData("upload_document", uploadFile.getName(), mFile);
                }
                Service uploadImage = ApiUtils.getAPIService();
                Call<CommanStatusPojo> fileUpload = uploadImage.uploadData(doucmentToUploadProfile);
                fileUpload.enqueue(new Callback<CommanStatusPojo>() {
                    @Override
                    public void onResponse(Call<CommanStatusPojo> call, retrofit2.Response<CommanStatusPojo> response) {
                        try {
                            if (response.body() != null) {
                                CommanStatusPojo registrationResponse = response.body();
                                if (registrationResponse.getStatus().equals("false")) {
                                    mProgressBar.setVisibility(View.GONE);
                                    Utils.showAlert(registrationResponse.getMessage(), getContext());
                                } else {
                                    if (webSocket != null) {
                                        uploadDoucment = null;
                                        JSONObject jsonObj = new JSONObject();
                                        JSONObject jsonObj_1 = new JSONObject();
                                        try {
                                            jsonObj.put("message", registrationResponse.getMessage());
                                            jsonObj.put("to_user", id);
                                            jsonObj.put("type", "message");
                                            jsonObj.put("message_type", chatType);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            jsonObj_1.put("username", userName);
                                            jsonObj_1.put("id", toUserId);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            jsonObj.put("user", jsonObj_1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        String jsonStr = jsonObj.toString();
                                        Log.e("Register", "" + jsonStr);
                                        scroll = 0;
                                        page = "0";
                                        webSocket.send(jsonStr);
                                        sendMessageToServerFirstVideo();
                                    }
                                }
                            } else {
                                mProgressBar.setVisibility(View.GONE);
                                Utils.showAlert(response.message(), getContext());
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            mProgressBar.setVisibility(View.GONE);
                            Utils.showAlert(e.getMessage(), getContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), Utils.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        } }else{
         //   uploadDoucment=null;
            mProgressBar.setVisibility(View.INVISIBLE);
            Utils.showAlert(getString(R.string.checkmb), getContext());
        }
    }


    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 90);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                capturedUri = FileProvider.getUriForFile(getActivity(), Utils.FILE_PROVIDER_AUTHORITY, createMediaFile(Utils.TYPE_VIDEO));
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);
                Log.d("LOG_TAG", "VideoUri: " + capturedUri.toString());
                startActivityForResult(takeVideoIntent, 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void dispatchTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createMediaFile(Utils.TYPE_IMAGE);
            } catch (IOException ex) {
                // Error occurred while creating the File
                // Log.i(TAG, "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri profileUri = FileProvider.getUriForFile(getActivity(),
                        Utils.FILE_PROVIDER_AUTHORITY,
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, profileUri);
                startActivityForResult(cameraIntent, 1);
            }
        }


    }


    private File createMediaFile(int type) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName;
        File file;
        File storageDir;
        if (type == Utils.TYPE_IMAGE) {
            fileName = "JPEG_" + timeStamp + "_";
        } else {
            fileName = "VID_" + timeStamp + "_";
        }
        if (type == 1) {
            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            file = File.createTempFile(fileName, ".jpg", storageDir      /* directory */);
            mCurrentPhotoPath = file.getAbsolutePath();
        } else {

            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

            file = File.createTempFile(fileName, ".mp4", storageDir      /* directory */);
            mCurrentVideoPath = file.getAbsolutePath();

        }

        return file;
    }



    /**
     * Command for compressing video
     */
    private void executeCompressCommand(String path) {


        mProgressBar.setVisibility(View.VISIBLE);
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES
        );

        String filePrefix = "compress_video";
        String fileExtn = ".mp4";
     //   String yourRealPath = getPath(getActivity(), selectedVideoUri);
        String yourRealPath = path;


        File dest = new File(moviesDir, filePrefix + fileExtn);
        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
        }

        Log.d("TAG", "startTrim: src: " +path );
        Log.d("TAG", "startTrim: dest: " + dest.getAbsolutePath());
        filePath = dest.getAbsolutePath();
        String[] complexCommand = {"-y", "-i", yourRealPath, "-s", "480x320", "-r", "25", "-vcodec", "mpeg4", "-b:v", "150k", "-b:a", "48000", "-ac", "2", "-ar", "22050", filePath};
        execFFmpegBinary(complexCommand);

    }

    /**
     * Executing ffmpeg binary
     */
    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d("TAG", "FAILED with output : " + s);




                }

                @Override
                public void onSuccess(String s) {

                    Log.d("TAG", "SUCCESS with output : " + s);
                    uploadDoucment = filePath;
                    File file = new File(uploadDoucment);
                    int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                    uloadeDoc(file_size);


                }

                @Override
                public void onProgress(String s) {
                    Log.d("TAG", "Started command : ffmpeg " + command);

                }

                @Override
                public void onStart() {
                    Log.d("TAG", "Started command : ffmpeg " + command);

                }

                @Override
                public void onFinish() {
                    Log.d("TAG", "Finished command : ffmpeg " + command);


                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    private String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri.
     */
    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {
        Context mContext;

        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // initDialog(getActivity().getString(R.string.compress_msg));
            // progressDialog.show();
            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... paths) {
            filePathVideo = null;
            try {

                filePathVideo = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);
                uploadDoucment = filePathVideo;
                File file = new File(uploadDoucment);
                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                uloadeDoc(file_size);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return filePathVideo;

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

        }
    }

    private void getMsg(RequestBody Data) {
        try {


            // p.show();
            Service acceptInterven = ApiUtils.getAPIService();
            Call<CommanStatusPojo> msesg = acceptInterven.getMsgCount(Data);

            msesg.enqueue(new Callback<CommanStatusPojo>() {
                @Override
                public void onResponse(Call<CommanStatusPojo> call, retrofit2.Response<CommanStatusPojo> response) {

                    if (response != null) {
                        try {
                            CommanStatusPojo assignmentListPojo_close = response.body();
                            Log.e("ddd", "" + assignmentListPojo_close);


                            if (assignmentListPojo_close.getStatus().equalsIgnoreCase("true")) {

                                if (assignmentListPojo_close.getMessage().equalsIgnoreCase(String.valueOf(mChatHolders.size()))) {

                                } else {
                                    sendMessageToServerFirst();
                                }

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("ddd", "" + response);
                }

                @Override
                public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
                    //  Utils.showAlert(t.getMessage(), getActivity());
                    Log.e("ddd", "" + "failure");
                }
            });
//            acceptInterasvenpCall.enqueue(new Callback<CommanStatusPojo>() {
//                @SuppressLint("ResourceAsColor")
//                @Override
//                public void onResponse(Call<CommanStatusPojo> call,Response<CommanStatusPojo> response) {
//                    try {
//                        if (response.body() != null) {
//                            CommanStatusPojo assignmentListPojo_close = response.body();
//                            if (assignmentListPojo_close.getStatus().equals("false")) {
//                                Utils.showAlert(assignmentListPojo_close.getMessage(), getActivity());
//                            } else {
//                                //  Utils.fragmentCall(new CloseIntervationReportFragment(assignmentListPojo_close), getFragmentManager());
//                            }
//                           // progressDialog.dismiss();
//
//                        } else {
//                            //  Utils.showAlert(getString(R.string.Notfound), getActivity());
//                            Utils.showAlert(getString(R.string.Notfound), getActivity());
//                        }
//
//                    } catch (Exception e) {
//                      //  progressDialog.dismiss();
//                        e.getMessage();
//                        Utils.showAlert(e.getMessage(), getActivity());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
//                    Log.d("TAG", "Error " + t.getMessage());
//                   // progressDialog.dismiss();
//                    Utils.showAlert(t.getMessage(), getActivity());
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class AddPhotoBottomDialogFragment extends BottomSheetDialogFragment {

        LinearLayout cameraIdLL;


        public static AddPhotoBottomDialogFragment newInstance() {
            return new AddPhotoBottomDialogFragment();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.custome_bottom_dilog, container,
                    false);


            // get the views and attach the listener

            return view;

        }
    }

    @Override
    public void onDestroyView() {
        if (getView() != null) {
            ViewGroup parent = (ViewGroup) getView().getParent();
            parent.removeAllViews();
        }
        super.onDestroyView();
    }
//Get Chat Histry
    public void scrollSocketData() {
        JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj_1 = new JSONObject();
        try {
            jsonObj.put("to_user", id);
            jsonObj.put("type", "chathistory");
            jsonObj.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();

        }


        try {
            jsonObj_1.put("username", userName);
            jsonObj_1.put("id", toUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObj.put("user", jsonObj_1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonStr = jsonObj.toString();
        webSocket.send(jsonStr);
        Log.e("First Time", "" + jsonStr);
    }
//for USE Page nation
    void scrollMsg(String txt) {

        try {

            JSONObject jObj = new JSONObject(txt);
            String type = jObj.getString("type");
            page = jObj.getString("page");

            Log.e("page", "" + page);

            if (type.equals("chathistory")) {
                JSONArray jsonArray = jObj.getJSONArray("message");
                Log.e("old", "" + mChatHolders.size());

                // mChatHoldersStore.addAll(mChatHolders);

                //   mChatHolders.clear();


                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mChatHolders.add(i, new ChatHolder(jsonObject.getString("message"), jsonObject.getString("sender"), jsonObject.getString("receiver"), jsonObject.getString("message_type"), jsonObject.getString("thumb")));
                        // mRecentChatAdapter.notifyItemInserted(i);
                    }
                    mRecentChatAdapter.notifyDataSetChanged();

                    mProgressDialog.dismiss();
                }
                mProgressDialog.dismiss();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }
    }
    private void outputScroll(final String txt, String no) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("websocketchat", txt);
                    if (no.equalsIgnoreCase("1")) {
                        scrollMsg(txt);

                    }
                    mProgressDialog.dismiss();

                }
            });
        }
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * Load FFmpeg binary
     */
    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                Log.d("TAG", "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(getActivity());
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showUnsupportedExceptionDialog();
                }

                @Override
                public void onSuccess() {
                    Log.d("TAG", "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showUnsupportedExceptionDialog();
        } catch (Exception e) {
            Log.d("TAG", "EXception no controlada : " + e);
        }
    }

    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Not Supported")
                .setMessage("Device Not Supported")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // MainActivity.this.finish();
                    }
                })
                .create()
                .show();

    }
}


