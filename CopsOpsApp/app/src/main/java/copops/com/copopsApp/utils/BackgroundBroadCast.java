package copops.com.copopsApp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.quickblox.chat.QBChatService;
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
import copops.com.copopsApp.fragment.LoginFragment;

public class BackgroundBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent background = new Intent(context, BackgroundService.class);
        context.startService(background);
    }







}
