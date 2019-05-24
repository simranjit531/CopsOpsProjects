package copops.com.copopsApp.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.common.base.CaseFormat;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.adapter.AssignmentInsidentListAdapter;
import copops.com.copopsApp.adapter.RecentChatAdapter;
import copops.com.copopsApp.pojo.RecentChatHolder;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.ChatHolder;
import copops.com.copopsApp.utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatRecentFragment extends Fragment implements View.OnClickListener, RecentChatAdapter.ChatItmeInterface {

    @BindView(R.id.recent_recycler_view)
    RecyclerView recent_recycler_view;

    RecentChatAdapter.ChatItmeInterface mChatItmeInterface;

    @BindView(R.id.iv_person_add)
    ImageView iv_person_add;

    @BindView(R.id.iv_recent_back)
    ImageView iv_recent_back;
    WebSocketClient  mWebSocketClient;
   ArrayList<RecentChatHolder> recentChatHolders = new ArrayList<>();
    RecentChatAdapter mRecentChatAdapter;
    AppSession mAppSession;
    ProgressDialog mProgressDialog;
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    WebSocket webSocket;
    final Handler pingHandler = new Handler();
    public ChatRecentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_chat_recent, container, false);
        ButterKnife.bind(this,view);
        mChatItmeInterface=this;
        mAppSession=mAppSession.getInstance(getActivity());
        initView();
     //   connectWebSocket();
        Request request = new Request.Builder().url(Utils.CHAT_BASE_URL).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        OkHttpClient okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.loading));
        mAppSession.saveData("Chat","0");

        mProgressDialog.show();
        mProgressDialog.dismiss();
        Runnable pingRunnable = new Runnable() {
            @Override public void run() {

                sendMessageToServerFirst();
                pingHandler.postDelayed(this, 3000);
            }
        };
        pingHandler.postDelayed(pingRunnable, 3000);
        return view;
    }




    // WebSocket
    private final class EchoWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {

            JSONObject jsonObj = new JSONObject();
            JSONObject jsonObj_1 = new JSONObject();
            try {
                //  jsonObj.put("to_user", id);
                jsonObj.put("type", "register");
            } catch (JSONException e) {
                e.printStackTrace();

            }


            try {
                jsonObj_1.put("username",mAppSession.getData("name") );
                jsonObj_1.put("id", mAppSession.getData("id"));
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
            sendMessageToServerFirst();
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            //  output("Rx: " + text);
            output(text,"1");
            Log.e("json",""+text);

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Rx bytes: " + bytes.hex(),"2");
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closed: " + code + " / " + reason,"3");
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error: " + t.getMessage(),"4");
        }
    }

    private void initView() {

        iv_person_add.setOnClickListener(this);
        iv_recent_back.setOnClickListener(this);

        mRecentChatAdapter = new RecentChatAdapter(getContext(),mChatItmeInterface,recentChatHolders);
        recent_recycler_view.setHasFixedSize(true);
        recent_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recent_recycler_view.setItemAnimator(new DefaultItemAnimator());
        recent_recycler_view.setAdapter(mRecentChatAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case R.id.iv_person_add:
            Utils.fragmentCall(new NewUserFragment(), getFragmentManager());
               break;
            case R.id.iv_recent_back:
              //  if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();

                Utils.fragmentCall(new OperatorFragment(), getFragmentManager());
             //   }

                break;
        }
    }


    private void sendMessageToServerFirst() {

        JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj_1 = new JSONObject();
        try {
          //  jsonObj.put("to_user", id);
            jsonObj.put("type", "recentchats");
        } catch (JSONException e) {
            e.printStackTrace();

        }


        try {
            jsonObj_1.put("username", mAppSession.getData("name"));
            jsonObj_1.put("id", mAppSession.getData("id"));
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
        Log.e("First Time", "" + jsonStr);

    }

    private void output(final String txt,String no) {


        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("websocketchat", txt);
                    if (no.equalsIgnoreCase("1")) {
                        msg(txt);

                        mRecentChatAdapter.notifyDataSetChanged();
                        if (recentChatHolders.size() > 0) {
                            recent_recycler_view.smoothScrollToPosition(mRecentChatAdapter.getItemCount() - 1);
                        }

                    }

                    mProgressDialog.dismiss();

                }
            });
        }
    }



    private void msg(String msg){
        try {
            JSONObject jObj = new JSONObject(msg);
            String type=jObj.getString("type");

            if(type.equals("recentchats")){
                JSONArray jsonArray =jObj.getJSONArray("recentchat");
                Log.e("CHAT DATA",jsonArray.toString());
                recentChatHolders.clear();

                if(jsonArray.length()>0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        recentChatHolders.add(new RecentChatHolder(jsonObject.getString("id"), jsonObject.getString("sender_id"), jsonObject.getString("receiver_id"),jsonObject.getString("message"),jsonObject.getString("message_type"),jsonObject.getString("is_read"),jsonObject.getString("is_deleted"),jsonObject.getString("created_at"),jsonObject.getString("updated_at"),jsonObject.getString("user_id"),jsonObject.getString("user"),jsonObject.getString("time"),jsonObject.getString("unread")));
                    }
                    //   size=mChatHolders;

                    mProgressDialog.dismiss();
                }
                mProgressDialog.dismiss();

            }

        } catch (JSONException e) {
            e.printStackTrace();
       //     mProgressDialog.dismiss();
        }
    }

    @Override
    public void clickPosition(int pos) {
        String aaa = mAppSession.getData("id");

        if(mAppSession.getData("id").equalsIgnoreCase((recentChatHolders.get(pos).getReceiver_id()))){
            Utils.fragmentCall(new ChatConversionFragment(recentChatHolders.get(pos).getSender_id(),recentChatHolders.get(pos).getUser(),recentChatHolders.get(pos).getReceiver_id()), getFragmentManager());
        }else{

            Utils.fragmentCall(new ChatConversionFragment(recentChatHolders.get(pos).getReceiver_id(),recentChatHolders.get(pos).getUser(),recentChatHolders.get(pos).getSender_id()), getFragmentManager());
        }


    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://82.165.253.201:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");


                JSONObject jsonObj = new JSONObject();
                JSONObject jsonObj_1 = new JSONObject();
                try {
                  //  jsonObj.put("to_user", id);
                    jsonObj.put("type", "register");
                } catch (JSONException e) {
                    e.printStackTrace();

                }


                try {
                    jsonObj_1.put("username",mAppSession.getData("name") );
                    jsonObj_1.put("id", mAppSession.getData("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    jsonObj.put("user", jsonObj_1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String jsonStr = jsonObj.toString();
                mWebSocketClient.send(jsonStr + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                final String message1 = s;

                output(s,"1");
                Log.e("json",""+s);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        TextView textView = (TextView)findViewById(R.id.messages);
////                        textView.setText(textView.getText() + "\n" + message);
//                    }
//                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }


}
