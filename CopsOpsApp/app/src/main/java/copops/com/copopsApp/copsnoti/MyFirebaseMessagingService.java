package copops.com.copopsApp.copsnoti;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import copops.com.copopsApp.R;
import copops.com.copopsApp.activity.AssigenedIntervation;
import copops.com.copopsApp.activity.DashboardActivity;
import copops.com.copopsApp.fragment.OperatorFragment;
import copops.com.copopsApp.pojo.IncdentSetPojo;
import copops.com.copopsApp.pojo.OperatorShowAlInfo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

  //  private static final String TAG = "COPSFirebaseMsgService";

    private static NotificationChannel mChannel;
    private static NotificationManager notifManager;
    AppSession mAppSession;
    Map<String, String> data;
    private String strRemoteMessage;
    private String intervationId="";
    private String intervationDateTime="";
    private String intervationAddress="";
    private String intervationObject="";
    private String intervationDescp="";
    private String intervationOthDescp="";
    private String intervationStatus="";
    private String intervationRef="";
    private static final String TAG = "FCM Service";
    JSONObject object;



    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {




        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " +
                    remoteMessage.getNotification().getBody());
        }



        mAppSession= mAppSession.getInstance(getApplicationContext());


        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Remote Message " +remoteMessage.getData());
    //    Log.e(TAG, "Remote Message1 " +remoteMessage.getNotification().getBody());
       // strRemoteMessage=remoteMessage.getData();
           data = remoteMessage.getData();
        wakeUpScreen();

        Map<String, String> params = remoteMessage.getData();
       object = new JSONObject(params);
        Log.e("JSON_OBJECT", object.toString());
        if(mAppSession.getData("copsuser").equalsIgnoreCase("citizen")){
            return;
        }

        // Check if message contains a data payload.

        if(remoteMessage.getData() != null) {

            try {

                    if(mAppSession.getData("devicelanguage").equalsIgnoreCase("fr")) {

                        if(mAppSession.getData("Chat").equalsIgnoreCase("1")){
                            mAppSession.saveData("comeChatnoti","1");
                            Log.e("show1","come");

                        }else{

                            JSONObject jsonObject2 = new JSONObject(remoteMessage.getData());


                                JSONObject jsonObject = new JSONObject(remoteMessage.getData());

                                if(jsonObject.getString("type").equalsIgnoreCase("chat")){
                                    String message=jsonObject.getString("message");

                                    mAppSession.saveData("senderId",jsonObject.getString("sender_id"));
                                    mAppSession.saveData("reciverid",jsonObject.getString("receiver_id"));

                                    mAppSession.saveData("user",jsonObject.getString("user"));

                                    sendNotificationChat("A new intervention has been assigned to you",message, object.toString(), getApplicationContext());
                                    mAppSession.saveData("chat_noti","1");
                                }else{

                                    sendNotification("A new intervention has been assigned to you", jsonObject.getString("message"), jsonObject2.toString(), getApplicationContext());
                                }



                            Log.d(TAG, "From: 1" + remoteMessage.getFrom());
                            if (Utils.checkConnection(getApplicationContext())) {
                                IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                                incdentSetPojo.setUser_id(mAppSession.getData("id"));
                                incdentSetPojo.setDevice_id(Utils.getDeviceId(getApplicationContext()));
                                incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
                                incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
                                incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                                Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                getCopeStatus(mFile);
                            } else {
                                Utils.showAlert(getApplicationContext().getString(R.string.internet_conection), getApplicationContext());
                            }
                        }



                    }else{
                      Log.d("chat0",""+mAppSession.getData("Chat"));
                        if(mAppSession.getData("Chat").equalsIgnoreCase("1")){

                            mAppSession.saveData("comeChatnoti","1");
                            Log.e("show1","come");
                        }else{
                            JSONObject jsonObject2 = new JSONObject(remoteMessage.getData());



                                JSONObject jsonObject = new JSONObject(remoteMessage.getData());

                                if (jsonObject.getString("type").equalsIgnoreCase("chat")) {
                                    String message = jsonObject.getString("message");

                                    mAppSession.saveData("senderId", jsonObject.getString("sender_id"));
                                    mAppSession.saveData("reciverid", jsonObject.getString("receiver_id"));

                                    mAppSession.saveData("user", jsonObject.getString("user"));

                                    sendNotificationChat("A new intervention has been assigned to you", message, object.toString(), getApplicationContext());
                                    mAppSession.saveData("chat_noti", "1");
                                } else {
                                    sendNotification("A new intervention has been assigned to you", jsonObject.getString("message"), jsonObject2.toString(), getApplicationContext());
                                }
                        //    Log.d(TAG, "From: 1" + remoteMessage.getFrom());
                            if (Utils.checkConnection(getApplicationContext())) {
                                IncdentSetPojo incdentSetPojo = new IncdentSetPojo();
                                incdentSetPojo.setUser_id(mAppSession.getData("id"));
                                incdentSetPojo.setDevice_id(Utils.getDeviceId(getApplicationContext()));
                                incdentSetPojo.setIncident_lat(mAppSession.getData("latitude"));
                                incdentSetPojo.setIncident_lng(mAppSession.getData("longitude"));
                                incdentSetPojo.setdevice_language(mAppSession.getData("devicelanguage"));
                                Log.e("@@@@", EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incdentSetPojo)));
                                getCopeStatus(mFile);
                            } else {
                                Utils.showAlert(getApplicationContext().getString(R.string.internet_conection), getApplicationContext());
                            }
                        }
                    }
                    mAppSession.saveData("notification", "notify");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    @SuppressLint("WrongConstant")
    public static void sendNotificationChat(String title, String description,String remMsg, Context context) {

        Ringtone r = null;
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder;
            //  Intent intent = new Intent(context, DashboardActivity.class);
            Intent intent = new Intent(context, DashboardActivity.class);

            // intent.putExtra("Intervation", "assignedIntervation");
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notification","chat");
            intent.putExtra("remMsg",remMsg);

            intent.putExtra("","");
            PendingIntent pendingIntent;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel==null) {
                mChannel = new NotificationChannel("0", "Copops", importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setSound(null,null);
                notifManager.createNotificationChannel(mChannel);




            }
            builder = new NotificationCompat.Builder(context, "0");
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle("Copops")
                    .setSmallIcon(getNotificationIcon()) // required
                    .setContentText(description)  // required
                    .setAutoCancel(true)
//                    .setLargeIcon(BitmapFactory.decodeResource
//                            (context.getResources(), R.mipmap.logo_launcher))
                    .setBadgeIconType(R.mipmap.logo_launcher)
                    .setContentIntent(pendingIntent);

            Notification notification = builder.build();
            notifManager.notify(0, notification);
        } else {

            // Intent intent = new Intent(context, DashboardActivity.class);
            Intent intent = new Intent(context, DashboardActivity.class);
            //   intent.putExtra("Intervation", "assignedIntervation");
            // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("notification","notify");
            intent.putExtra("remMsg",remMsg);

            PendingIntent pendingIntent = null;

            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("Copops")
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(getNotificationIcon())
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("Copops").bigText(description));


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    @SuppressLint("WrongConstant")
    public static void sendNotification(String title, String description,String remMsg, Context context) {

        Ringtone r = null;
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder;
            //  Intent intent = new Intent(context, DashboardActivity.class);
            Intent intent = new Intent(context, AssigenedIntervation.class);

            // intent.putExtra("Intervation", "assignedIntervation");
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notification","notify");
            intent.putExtra("remMsg",remMsg);

            intent.putExtra("","");
            PendingIntent pendingIntent;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel==null) {
                mChannel = new NotificationChannel("0", "Copops", importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setSound(null,null);
                notifManager.createNotificationChannel(mChannel);




            }
            builder = new NotificationCompat.Builder(context, "0");
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle("Copops")
                    .setSmallIcon(getNotificationIcon()) // required
                    .setContentText(description)  // required
                    .setAutoCancel(true)
//                    .setLargeIcon(BitmapFactory.decodeResource
//                            (context.getResources(), R.mipmap.logo_launcher))
                    .setBadgeIconType(R.mipmap.logo_launcher).setPriority(1000)
                    .setContentIntent(pendingIntent).setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            Notification notification = builder.build();
            notifManager.notify(0, notification);
        } else {

            // Intent intent = new Intent(context, DashboardActivity.class);
            Intent intent = new Intent(context, AssigenedIntervation.class);
            //   intent.putExtra("Intervation", "assignedIntervation");
            // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("notification","notify");
            intent.putExtra("remMsg",remMsg);

            PendingIntent pendingIntent = null;

            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("Copops")
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(getNotificationIcon()).setPriority(1000)
                    .setContentIntent(pendingIntent).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).
                    setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("Copops").bigText(description));


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    private static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.logo_launcher : R.mipmap.logo_launcher;
    }



    private void getCopeStatus(RequestBody Data) {

     //   progressDialog.show();
        Service operator = ApiUtils.getAPIService();
        Call<OperatorShowAlInfo> getallLatLong = operator.getOperotor(Data);
        getallLatLong.enqueue(new Callback<OperatorShowAlInfo>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<OperatorShowAlInfo> call, Response<OperatorShowAlInfo> response)

            {
                try {
                    if (response.body() != null) {
                        OperatorShowAlInfo operatorShowAlInfo = response.body();

                        if (operatorShowAlInfo.getStatus().equals("false")) {
                            //  Utils.showAlert(registrationResponse.getMessage(), getActivity());
                        } else {


                             String new_reports =operatorShowAlInfo.getNew_reports();
                             mAppSession.saveData("new_reports",new_reports);

                            mAppSession.saveData("copstatus",operatorShowAlInfo.getAvailable());
                             mAppSession.saveData("notifica",operatorShowAlInfo.getTotalMessageCount());



                    }

                }} catch (Exception e) {

                    e.getMessage();

                }
            }

            @Override
            public void onFailure(Call<OperatorShowAlInfo> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());


            }
        });
    }




    public boolean isJSONValid(String testJson) {
        try {
            new JSONObject(testJson);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(testJson);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    /* when your phone is locked screen wakeup method*/
    private void wakeUpScreen() {
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        Log.e("screen on......", "" + isScreenOn);
        if (isScreenOn == false) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire(10000);
        }
    }
}
