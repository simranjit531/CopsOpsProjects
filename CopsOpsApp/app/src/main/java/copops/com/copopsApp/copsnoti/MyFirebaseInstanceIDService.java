package copops.com.copopsApp.copsnoti;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import copops.com.copopsApp.utils.AppSession;

/**
 * Created by Ranjan Gupta
 */
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    AppSession mAppSession;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken1", s);

        mAppSession= mAppSession.getInstance(getApplicationContext());
        mAppSession.saveData("fcm_token",s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("dsadasdsad","dsdssasadas");
        super.onMessageReceived(remoteMessage);
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }
}