package copops.com.copopsApp.chatmodule.gcm;


import com.quickblox.sample.core.gcm.CoreGcmPushListenerService;
import com.quickblox.sample.core.utils.NotificationUtils;
import com.quickblox.sample.core.utils.ResourceUtils;

import copops.com.copopsApp.R;
import copops.com.copopsApp.chatmodule.ui.activity.SplashActivity;

public class GcmPushListenerService extends CoreGcmPushListenerService {
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void showNotification(String message) {
        NotificationUtils.showNotification(this, SplashActivity.class,
                ResourceUtils.getString(R.string.notification_title), message,
                R.mipmap.ic_notification, NOTIFICATION_ID);
    }
}