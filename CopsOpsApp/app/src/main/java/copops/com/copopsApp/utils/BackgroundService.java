package copops.com.copopsApp.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import copops.com.copopsApp.R;
import copops.com.copopsApp.chatmodule.App;
import copops.com.copopsApp.chatmodule.utils.PushBroadcastReceiver;
import copops.com.copopsApp.chatmodule.utils.chat.ChatHelper;
import copops.com.copopsApp.chatmodule.utils.qb.QbChatDialogMessageListenerImp;

public class BackgroundService extends Service {

    private boolean isRunning;
    private Context context;
    Thread backgroundThread;

    AppSession mAppSession;

    QBIncomingMessagesManager incomingMessagesManager;
    private QBUser currentUser;
    ArrayList<QBUser> list;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);

        mAppSession=mAppSession.getInstance(context);
    }





    private void buildUsersList() {

        List<String> tags = new ArrayList<>();
        tags.add(App.getSampleConfigs().getUsersTag());

        QBUsers.getUsersByTags(tags, null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> result, Bundle params) {

                list = result;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getLogin().equalsIgnoreCase(mAppSession.getData("user_id"))) {
                        QBUser user = list.get(i);
                        user.setPassword(App.getSampleConfigs().getUsersPassword());
                        //user.setPassword(mAppSession.getData("user_id"));
                        login(user);
                        break;
                    }
                }
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void login(final QBUser user) {
    //    ProgressDialogFragment.show(getActivity().getSupportFragmentManager(), R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);
               // ProgressDialogFragment.hide().getSupportFragmentManager());
                currentUser = ChatHelper.getCurrentUser();

                incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();

                incomingMessagesManager.addDialogMessageListener(new AllDialogsMessageListener());
            }

            @Override
            public void onError(QBResponseException e) {
             //   ProgressDialogFragment.hide(getActivity().getSupportFragmentManager());
            }
        });
    }



    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            Log.d("RanjanCheck", "processMessage");


            Log.d("RanjanCheck", "processMessage");
            QBUser user=null;
            int sender= qbChatMessage.getSenderId();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(sender)) {
                    user = list.get(i);
                    break;
                }
            }

//            if (qbChatMessage.getAttachments() != null) {
//                PushBroadcastReceiver.displayCustomNotificationForOrders(result.getName(), " " + "Attachment" + "  " + "(" + count + " message)", getActivity());
//            } else{
//                PushBroadcastReceiver.displayCustomNotificationForOrders(result.getName(), " " + qbChatMessage.getBody() + "  " + "(" + count + " message)", getActivity());
//
//            }
           // PushBroadcastReceiver.displayCustomNotificationForOrders(user.getFullName(), " "+qbChatMessage.getBody(), context);
            //   PushBroadcastReceiver.displayCustomNotificationForOrders("COPOPS", " "+qbChatMessage.getBody(), getActivity());
        }
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            // Do something here
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
            Log.e("in timer", "Ranjan  ");
            buildUsersList();
     //   }


        return START_STICKY;
    }

}