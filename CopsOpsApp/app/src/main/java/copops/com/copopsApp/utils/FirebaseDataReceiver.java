package copops.com.copopsApp.utils;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.legacy.content.WakefulBroadcastReceiver;

/**
 * Created by Ranjan Gupta
 */
public class FirebaseDataReceiver extends WakefulBroadcastReceiver{

    private AppSession mAppSession;

    public void onReceive(Context context, Intent intent) {
        mAppSession=mAppSession.getInstance(context);
        if(mAppSession.getData("copsuser").equalsIgnoreCase("citizen")){
            return;
        }
        if(mAppSession.getData("Chat").equalsIgnoreCase("1")){

        }else{
            playNotificationSound(context);
        }
    }
    public void playNotificationSound(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}