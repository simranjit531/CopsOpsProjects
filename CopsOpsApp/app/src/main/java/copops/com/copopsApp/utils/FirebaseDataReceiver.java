package copops.com.copopsApp.utils;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.legacy.content.WakefulBroadcastReceiver;
import copops.com.copopsApp.R;





public class FirebaseDataReceiver extends WakefulBroadcastReceiver  {

    private final String TAG = "FirebaseDataReceiver";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onReceive(Context context, Intent intent) {
     //   Log.d("abcccc", "I'm in!!!");
     //   Bundle dataBundle = intent.getBundleExtra("data");

       // PushBroadcastReceiver.displayCustomNotificationForOrders("Copops", " " + "Attachment" +" message)", context);

     //   Log.d(TAG, dataBundle.toString());

        playNotificationSound(context);


    }



    public void playNotificationSound(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();

//            Intent intent = new Intent(context, DashboardActivity.class);
//
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("notification","notify");
//            PendingIntent pendingIntent = null;
//
//            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
//                    .setContentTitle("Copops")
//                    .setContentText("HELLO")
//                    .setAutoCancel(true)
//                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
//                    .setSmallIcon(getNotificationIcon())
//                    .setContentIntent(pendingIntent)
//                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("Copops").bigText("HELLO"));
//
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.logo_launcher : R.mipmap.logo_launcher;
    }
}