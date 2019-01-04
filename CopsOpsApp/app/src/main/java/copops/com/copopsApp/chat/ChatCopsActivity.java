package copops.com.copopsApp.chat;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.adapter.ChatAAdapter;
import copops.com.copopsApp.chatmodule.App;
import copops.com.copopsApp.chatmodule.ui.activity.DialogsActivity;
import copops.com.copopsApp.chatmodule.utils.chat.ChatHelper;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.ui.activity.CoreBaseActivity;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.ErrorUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

public class ChatCopsActivity extends CoreBaseActivity implements ChatAAdapter.ChatInterFcee,View.OnClickListener {
    private static final int SPLASH_DELAY = 1500;

    ChatAAdapter.ChatInterFcee mChatInterFcee;

    @BindView(R.id.chatlist)
    RecyclerView chatlist;

    @BindView(R.id.filterId)
    ImageView filterId;

    ProgressDialog progressDialog;

    ArrayList<QBUser> list;
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);
        mChatInterFcee=this;
        progressDialog = new ProgressDialog(ChatCopsActivity.this);
        progressDialog.setMessage("loading...");

        filterId.setOnClickListener(this);
        progressDialog.show();
        if (checkConfigsWithSnackebarError()) {
            proceedToTheNextActivityWithDelay();
        }
    }


    protected boolean checkConfigsWithSnackebarError() {
        if (!sampleConfigIsCorrect()) {
            showSnackbarErrorParsingConfigs();
            return false;
        }

        return true;
    }



    protected boolean sampleConfigIsCorrect() {
        return CoreApp.getInstance().getQbConfigs() != null;
    }

    protected void proceedToTheNextActivityWithDelay() {
        mainThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                proceedToTheNextActivity();
            }
        }, SPLASH_DELAY);
    }

    protected void proceedToTheNextActivity() {
        if (checkSignIn()) {
            restoreChatSession();

          //  buildUsersList();
        } else {


         //   new OnUserLoginItemClickListener();

            buildUsersList();
           // LoginActivity.start(this);
            //finish();
        }
    }
    private void restoreChatSession() {
        if (ChatHelper.getInstance().isLogged()) {
            DialogsActivity.start(this);
            finish();
        } else {
            QBUser currentUser = getUserFromSession();
            if (currentUser == null) {

              //  LoginActivity.start(this);

                buildUsersList();
            } else {
                loginToChat(currentUser);
            }
        }
    }


    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        QBSessionManager qbSessionManager = QBSessionManager.getInstance();
        if (qbSessionManager.getSessionParameters() == null) {
            ChatHelper.getInstance().destroy();
            return null;
        }
        Integer userId = qbSessionManager.getSessionParameters().getUserId();
        user.setId(userId);
        return user;
    }



    private void loginToChat(final QBUser user) {
        ProgressDialogFragment.show(getSupportFragmentManager(), R.string.dlg_restoring_chat_session);

        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Log.v("TAG", "Chat login onSuccess()");

                ProgressDialogFragment.hide(getSupportFragmentManager());
                DialogsActivity.start(ChatCopsActivity.this);
               // finish();
            }

            @Override
            public void onError(QBResponseException e) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
              //  Log.w(TAG, "Chat login onError(): " + e);
                showSnackbarError(findViewById(R.id.layout_root), R.string.error_recreate_session, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginToChat(user);
                            }
                        });
            }
        });
    }

    @Override
    protected void showSnackbarError(View rootLayout, @StringRes int resId, QBResponseException e, View.OnClickListener clickListener) {
        rootLayout = findViewById(com.quickblox.sample.core.R.id.layout_root);
        ErrorUtils.showSnackbar(rootLayout, resId, e, com.quickblox.sample.core.R.string.dlg_retry, clickListener);
    }



    protected void showSnackbarErrorParsingConfigs() {
        ErrorUtils.showSnackbar(findViewById(com.quickblox.sample.core.R.id.layout_root), com.quickblox.sample.core.R.string.error_parsing_configs, com.quickblox.sample.core.R.string.dlg_ok, null);
    }

    protected boolean checkSignIn() {
        return QBSessionManager.getInstance().getSessionParameters() != null;
    }


    protected String getAppName() {
        return getString(R.string.splash_app_title);
    }



    private void buildUsersList() {
        List<String> tags = new ArrayList<>();

        tags.add(App.getSampleConfigs().getUsersTag());

        QBUsers.getUsersByTags(tags, null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> result, Bundle params) {

                progressDialog.dismiss();

                list =result;
//                final QBUser user = list.get(1);
//                // We use hardcoded password for all users for test purposes
//                // Of course you shouldn't do that in your app
//                user.setPassword(App.getSampleConfigs().getUsersPassword());
//
//                login(user);
              //  login()
                ChatAAdapter adapter = new ChatAAdapter(ChatCopsActivity.this, result,mChatInterFcee);
                chatlist.setHasFixedSize(true);
                chatlist.setLayoutManager(new LinearLayoutManager(ChatCopsActivity.this));
                chatlist.setItemAnimator(new DefaultItemAnimator());
                chatlist.setAdapter(adapter);
            }

            @Override
            public void onError(QBResponseException e) {
                ErrorUtils.showSnackbar(chatlist, R.string.login_cant_obtain_users, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                buildUsersList();
                            }
                        });
            }
        });
    }
    private void login(final QBUser user) {
        ProgressDialogFragment.show(getSupportFragmentManager(), R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);


                DialogsActivity.start(ChatCopsActivity.this);
              //  finish();

                ProgressDialogFragment.hide(getSupportFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
                ErrorUtils.showSnackbar(chatlist, R.string.login_chat_login_error, e,
                        R.string.dlg_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                login(user);
                            }
                        });
            }
        });
    }

    @Override
    public void onClick(int Pos) {
        new OnUserLoginItemClickListener();


        final QBUser user = list.get(Pos);
        // We use hardcoded password for all users for test purposes
        // Of course you shouldn't do that in your app
        user.setPassword(App.getSampleConfigs().getUsersPassword());

        login(user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.filterId:
                finish();
                break;

        }
    }


    private class OnUserLoginItemClickListener implements AdapterView.OnItemClickListener {

        public static final int LIST_HEADER_POSITION = 0;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == LIST_HEADER_POSITION) {
                return;
            }


            final QBUser user = (QBUser) parent.getItemAtPosition(position);
            // We use hardcoded password for all users for test purposes
            // Of course you shouldn't do that in your app
            user.setPassword(App.getSampleConfigs().getUsersPassword());

            login(user);
        }

    }
}
