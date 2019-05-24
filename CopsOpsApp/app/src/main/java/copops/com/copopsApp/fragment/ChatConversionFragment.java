package copops.com.copopsApp.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
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


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.iceteck.silicompressorr.SiliCompressor;


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
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ChatConversionFragment extends Fragment implements View.OnClickListener {

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

    boolean isCheck=false;
    private int overallXScroll = 0;
    int firstItemPos;
    /// private WebSocketConnection mConnection = new WebSocketConnection();

    Uri capturedUri = null;
    private ArrayList<ChatHolder> mChatHolders = new ArrayList<>();
    private ArrayList<ChatHolder> mChatHoldersStore = new ArrayList<>();
  int scroll=0;
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
        ;

        mAppSession = mAppSession.getInstance(getActivity());
        LinearLayout cameraIdLL = (LinearLayout) sheetView.findViewById(R.id.cameraIdLL);
        LinearLayout galleryid = (LinearLayout) sheetView.findViewById(R.id.galleryid);
        LinearLayout recordingid = (LinearLayout) sheetView.findViewById(R.id.recordingid);
        LinearLayout fileiconId = (LinearLayout) sheetView.findViewById(R.id.fileiconId);
        TextView tv_btn_remove_photo = (TextView) sheetView.findViewById(R.id.tv_btn_remove_photo);
        button_chat_attachment.setOnClickListener(this);
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
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 4);
                mBottomSheetDialog.dismiss();
            }
        });

        tv_btn_remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sheetView.d
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
        // sendMessageToServerFirst();

        mProgressDialog.show();

        pingRunnable = new Runnable() {
            @Override
            public void run() {



                if(webSocket==null){
                    Request request = new Request.Builder().url(Utils.CHAT_BASE_URL).build();
                    EchoWebSocketListener listener = new EchoWebSocketListener();
                    okHttpClient = new OkHttpClient();

                    webSocket = okHttpClient.newWebSocket(request, listener);

                    okHttpClient.dispatcher().executorService().shutdown();
                }
                //sendMessageToServerFirst();
                pingHandler.postDelayed(this, 1000);
//                if(size.size()>mChatHolders.size()) {

                String aa = mAppSession.getData("comeChatnoti");
                Log.e("aaa", "" + aa);

                if (mAppSession.getData("comeChatnoti").equalsIgnoreCase("1")) {
                    //  seenUnSeen();
                    page="0";
                    scroll=0;
                    sendMessageToServerFirst();
                    Log.e("show1", "sucesss");
                //    mAppSession.saveData("comeChatnoti", "0");
                } else {
                    Log.e("show1", "fail");
                }
//
            }
        };
        pingHandler.postDelayed(pingRunnable, 1000);


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_chat_send:
                if (webSocket != null) {

                    if (edit_chat_message.getText().toString().trim().equalsIgnoreCase("")) {
                        //chatType="TEXT";
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
                        scroll=0;
                        // seenUnSeen();
                        sendMessageToServerFirst();
                    }

                    // pingHandler.postDelayed(this, 1000);
                }

                break;

            case R.id.button_chat_attachment:
                //selectImage();


                mBottomSheetDialog.show();


                break;

            case R.id.Rltoolbar:

//                pingHandler.post(pingRunnable);
//                Utils.fragmentCall(new ChatRecentFragment(), getFragmentManager());
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                    mAppSession.saveData("Chat", "0");
                    pingHandler.post(pingRunnable);
                    //    Utils.fragmentCall(new ChatRecentFragment(), getFragmentManager());
                }
                break;
            // webSocket.close(1010, "");

            //   break;
        }

    }


    // WebSocket
    private final class EchoWebSocketListener extends WebSocketListener {


//        private void run() {
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .readTimeout(0,  TimeUnit.MILLISECONDS)
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url("ws://82.165.253.201:8080")
//                    .build();
//            client.newWebSocket(request, this);
//
//            // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
//            client.dispatcher().executorService().shutdown();
//        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
//            webSocket.send("Hello, it's SSaurel !");
//            webSocket.send("What's up ?");
//            webSocket.send(ByteString.decodeHex("deadbeef"));

            //   webSocket=webSocket;
            //  webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
            Log.e("first", "" + 1);


            Log.i("Websocket", "Opened");


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
            // seenUnSeen();
            //   sendMessageToServerFirst();


        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            //  output("Rx: " + text);
            Log.e("first", "" + 2);

            Log.d("firsttext", "" + text);


if(scroll==1){
    outputScroll(text,"1");


}else{
    output(text, "1");
}


                //isCheck=true;



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
                       // mChatHolders.clear();
                        msg(txt);


                        if (mChatHolders.size() > 0) {

                            mRecentChatAdapter.notifyDataSetChanged();
                          //  mLayoutManager.setStackFromEnd(true);


                            list_chat_messages.smoothScrollToPosition(mRecentChatAdapter.getItemCount() - 1);

                           // if(chatType.equalsIgnoreCase("VIDEO"))


                        }

                     //
                    }
                    mProgressDialog.dismiss();

                }
            });
        }
    }


    private void sendMessageToServerFirst() {

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
      //
        seenUnSeen();
        //  pingHandler.postDelayed(this, 10000);
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
        //
        seenUnSeen();
        mProgressBar.setVisibility(View.GONE);
        //  pingHandler.postDelayed(this, 10000);
        Log.e("First Time", "" + jsonStr);

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
        //  pingHandler.postDelayed(this, 10000);
        Log.e("seen", "" + jsonStr);

    }


    private void msg(String msg) {
        try {
            JSONObject jObj = new JSONObject(msg);
            String type = jObj.getString("type");
            page = jObj.getString("page");

            Log.e("page", "" + page);

            if (type.equals("chathistory")) {
                JSONArray jsonArray = jObj.getJSONArray("message");
                Log.e("CHAT DATA", jsonArray.toString());
                mChatHolders.clear();

                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mChatHolders.add(new ChatHolder(jsonObject.getString("message"), jsonObject.getString("sender"), jsonObject.getString("receiver"), jsonObject.getString("message_type"), jsonObject.getString("thumb")));
                    }


               //     mChatHoldersStore.addAll(mChatHolders);


                    //   size=mChatHolders;

                    mProgressDialog.dismiss();
                }
                mProgressDialog.dismiss();

            }

        } catch (JSONException e) {
            e.printStackTrace();
            mProgressDialog.dismiss();
        }
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Record Video", "File", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    dispatchTakePictureIntent();

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Record Video")) {
                    dispatchTakeVideoIntent();
                } else if (options[item].equals("File")) {

                    Intent intent = new Intent();
                    intent.setType("application/pdf");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 4);


                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private String saveFileFromUri(Uri pdfUri) {
        try {
            InputStream is =
                    getContext().getContentResolver().openInputStream(pdfUri);
            byte[] bytesArray = new byte[is.available()];
            int read = is.read(bytesArray);
            //write to sdcard

            File dir = new File(Environment.getExternalStorageDirectory(), "/CopopsPdf");
            boolean mkdirs = dir.mkdirs();
            File myPdf = new
                    File(Environment.getExternalStorageDirectory(), "/CopopsPdf/myPdf.pdf");
            if (read == -1 && mkdirs) {

            }
            FileOutputStream fos = new FileOutputStream(myPdf.getPath());
            fos.write(bytesArray);
            fos.close();
            //            System.out.println(fileString);
            return myPdf.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

                uloadeDoc();

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                uploadDoucment = picturePath;
                chatType = "IMAGE";
                uloadeDoc();
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
                // viewImage.setImageBitmap(thumbnail);
            } else if (requestCode == 3) {
                //  Uri uri=data.getData();

                chatType = "VIDEO";


                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/CopOps/videos");
                if (f.mkdirs() || f.isDirectory())

                    new VideoCompressAsyncTask(getActivity()).execute(mCurrentVideoPath, f.getPath());

            } else if (requestCode == 4) {


                chatType = "PDF";

                Uri filePath = data.getData();


                uploadDoucment = saveFileFromUri(filePath);


                uloadeDoc();


            }
        }
    }

    public void uloadeDoc() {
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
                // RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(registationPjoSetData)));
                //  Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(registationPjoSetData)));
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

                                              scroll=0;
                                             page="0";
                                        webSocket.send(jsonStr);
                                        sendMessageToServerFirstVideo();

                                        //     edit_chat_message.setText("");

                                        //  getActivity().startService(new Intent(getActivity(), TrackingServices.class));

                                    }
                                }
                                //   progressDialog.dismiss();

                            } else {
                                //   progressDialog.dismiss();
                                mProgressBar.setVisibility(View.GONE);
                                Utils.showAlert(response.message(), getContext());
                            }
                        } catch (Exception e) {
                            //  progressDialog.dismiss();
                            e.getMessage();
                            mProgressBar.setVisibility(View.GONE);
                            Utils.showAlert(e.getMessage(), getContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommanStatusPojo> call, Throwable t) {
                        //   Log.d(TAG, "Error " + t.getMessage());
                        //  progressDialog.dismiss();

                        mProgressBar.setVisibility(View.GONE);
                        //    Utils.showAlert(t.getMessage(), getContext());
                    }
                });
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), Utils.READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
         //   Utils.showAlert(e.getMessage(), getContext());
        }
    }


    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {

                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1800);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                capturedUri = FileProvider.getUriForFile(getActivity(),
                        Utils.FILE_PROVIDER_AUTHORITY, createMediaFile(Utils.TYPE_VIDEO));

                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);
                Log.d("LOG_TAG", "VideoUri: " + capturedUri.toString());
                startActivityForResult(takeVideoIntent, 3);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private void dispatchTakePictureIntent() {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");*/
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

                uloadeDoc();

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

    void scrollMsg(String txt) {

        try {

            JSONObject jObj = new JSONObject(txt);
            String type = jObj.getString("type");
            page = jObj.getString("page");

            Log.e("page", "" + page);

            if (type.equals("chathistory")) {
                JSONArray jsonArray = jObj.getJSONArray("message");
                Log.e("old",""+ mChatHolders.size());

               // mChatHoldersStore.addAll(mChatHolders);

             //   mChatHolders.clear();


                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mChatHolders.add(i,new ChatHolder(jsonObject.getString("message"), jsonObject.getString("sender"), jsonObject.getString("receiver"), jsonObject.getString("message_type"), jsonObject.getString("thumb")));
                       // mRecentChatAdapter.notifyItemInserted(i);
                    }

                      mRecentChatAdapter.notifyDataSetChanged();

                    mProgressDialog.dismiss();
                }
//                else{
//                    mAppSession.saveData("comeChatnoti", "1");
//                }

              //  list_chat_messages.setItemAnimator(null);
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






}


