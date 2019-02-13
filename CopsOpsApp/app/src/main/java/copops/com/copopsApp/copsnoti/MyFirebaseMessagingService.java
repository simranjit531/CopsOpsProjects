package copops.com.copopsApp.copsnoti;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import android.os.Build;
import android.util.Log;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import copops.com.copopsApp.R;
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

    private static final String TAG = "COPSFirebaseMsgService";

    private static NotificationChannel mChannel;
    private static NotificationManager notifManager;

    AppSession mAppSession;
interface updateInterface{
   public void update();
}

    updateInterface mUpdateInterface;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


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
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        if(remoteMessage.getNotification() != null) {
            if (remoteMessage.getData().size() > 0) {

                try {
                    if (remoteMessage.getNotification().getBody() != null) {
                        Log.d(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());
                        //  Log.e(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());

                        if(remoteMessage.getNotification().getBody().equalsIgnoreCase("intervention assigned")) {
                            if(mAppSession.getData("devicelanguage").equalsIgnoreCase("fr")) {
                                sendNotification("COPOPS", "Interventions Assignées", getApplicationContext());

                            }else{
                                sendNotification("COPOPS", remoteMessage.getNotification().getBody(), getApplicationContext());
                            }
                                sendNotification("COPOPS", "Interventions Assignées", getApplicationContext());
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

                        }else{
                            sendNotification("COPOPS", remoteMessage.getNotification().getBody(), getApplicationContext());
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

                     //   String new_reports =remoteMessage.getData().get("new_reports");
                       // mAppSession.saveData("new_reports",new_reports);


                        //   sendNotification(remoteMessage.getNotification().getBody());
                        mAppSession.saveData("notification", "notify");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


            if(remoteMessage.getNotification().getBody().equalsIgnoreCase("intervention assigned")) {

                if(mAppSession.getData("devicelanguage").equalsIgnoreCase("fr")) {
                    sendNotification("COPOPS", "Interventions Assignées", getApplicationContext());

                    }else{
                    sendNotification("COPOPS", remoteMessage.getNotification().getBody(), getApplicationContext());
                }
                //  String new_reports =remoteMessage.getData().get("new_reports");
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
            }else{
                sendNotification("COPOPS", remoteMessage.getNotification().getBody(), getApplicationContext());
                //  String new_reports =remoteMessage.getData().get("new_reports");
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
      //      mAppSession.saveData("new_reports",new_reports);
          //  sendNotification(remoteMessage.getNotification().getBody());
            mAppSession.saveData("notification","notify");
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]





    @SuppressLint("WrongConstant")
    public static void sendNotification(String title, String description, Context context) {


        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder;
            Intent intent = new Intent(context, DashboardActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, "0");

//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("notification","notify");
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle(title)
                    .setSmallIcon(getNotificationIcon()) // required
                    .setContentText(description)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
//                    .setLargeIcon(BitmapFactory.decodeResource
//                            (context.getResources(), R.mipmap.logo_launcher))
                    .setBadgeIconType(R.mipmap.logo_launcher)
                    .setContentIntent(pendingIntent);
//                    .setSound(RingtoneManager.getDefaultUri
//                            (RingtoneManager.TYPE_NOTIFICATION));
            Notification notification = builder.build();
            notifManager.notify(0, notification);
        } else {

            Intent intent = new Intent(context, DashboardActivity.class);

//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notification","notify");
            PendingIntent pendingIntent = null;

            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSound(defaultSoundUri)
                    .setSmallIcon(getNotificationIcon())
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(description));

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }




    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //old image name for push is logon
        //new image name is app_logo_new

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo_launcher)
                .setContentTitle("COPOPS")
                .setContentText(messageBody).setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
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
}
