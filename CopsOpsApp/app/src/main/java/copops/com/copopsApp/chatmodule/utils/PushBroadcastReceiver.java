package copops.com.copopsApp.chatmodule.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.quickblox.sample.core.utils.constant.GcmConsts;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import copops.com.copopsApp.R;
import copops.com.copopsApp.chatmodule.ui.activity.DialogsActivity;

public class PushBroadcastReceiver {

    private static NotificationChannel mChannel;
    private static NotificationManager notifManager;

    /*extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {Ø
        String message = intent.getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE);
        Log.v("Check", "Received broadcast " + intent.getAction() + " with data: " + message);
        displayCustomNotificationForOrders("heelo","hii",context);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    public static void displayCustomNotificationForOrders(String title, String description, Context context) {
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try{
            NotificationCompat.Builder builder;
            Intent intent = new Intent(context, DialogsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", title, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setSound(null, null);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, "0");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);


            builder.setContentTitle(title)
                    .setSmallIcon(getNotificationIcon()) // required
                    .setContentText(description)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource
                            (context.getResources(), R.mipmap.logo_launcher))
                    .setBadgeIconType(R.mipmap.logo_launcher).setSound( Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.notification))
                    .setContentIntent(pendingIntent);
//                    .setSound(RingtoneManager.getDefaultUri
//                            (RingtoneManager.TYPE_NOTIFICATION));
            Notification notification = builder.build();
            notifManager.notify(0, notification);
        }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try {

                int importance = NotificationManager.IMPORTANCE_HIGH;


                Intent intent = new Intent(context, DialogsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = null;
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);

                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                if (mChannel == null) {
                    mChannel = new NotificationChannel
                            ("0", title, importance);
                    mChannel.setDescription(description);
                    mChannel.enableVibration(true);
                    mChannel.setSound(null, null);
                    notifManager.createNotificationChannel(mChannel);
                }
              Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                notificationBuilder.setContentTitle(title)
                        .setContentText(description)
                        .setAutoCancel(true)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(getNotificationIcon())
                        .setContentIntent(pendingIntent).setSound(Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + R.raw.notification))
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(description));

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.logo_launcher : R.mipmap.logo_launcher;
    }
}
