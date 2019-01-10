package copops.com.copopsApp.chatmodule.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.messages.services.QBPushManager;
import com.quickblox.messages.services.SubscribeService;

import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.async.BaseAsyncTask;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.ErrorUtils;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.StringRes;
import androidx.appcompat.view.ActionMode;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.activity.DashboardActivity;
import copops.com.copopsApp.chat.ChatCopsActivity;
import copops.com.copopsApp.chatmodule.App;
import copops.com.copopsApp.chatmodule.managers.DialogsManager;
import copops.com.copopsApp.chatmodule.ui.adapter.DialogsAdapter;
import copops.com.copopsApp.chatmodule.utils.chat.ChatHelper;
import copops.com.copopsApp.chatmodule.utils.qb.QbChatDialogMessageListenerImp;
import copops.com.copopsApp.chatmodule.utils.qb.QbDialogHolder;
import copops.com.copopsApp.chatmodule.utils.qb.callback.QBPushSubscribeListenerImpl;
import copops.com.copopsApp.chatmodule.utils.qb.callback.QbEntityCallbackImpl;
import copops.com.copopsApp.utils.AppSession;

public class DialogsActivity extends BaseActivity implements DialogsManager.ManagingDialogsCallbacks {
    private static final String TAG = DialogsActivity.class.getSimpleName();
    private static final int REQUEST_SELECT_PEOPLE = 174;
    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;
    ArrayList<QBUser> list;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private ActionMode currentActionMode;
    private SwipyRefreshLayout setOnRefreshListener;
    private QBRequestGetBuilder requestBuilder;
    private Menu menu;
    private int skipRecords = 0;
    private boolean isProcessingResultInProgress;
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private BroadcastReceiver pushBroadcastReceiver;
    private GooglePlayServicesHelper googlePlayServicesHelper;
    private DialogsAdapter dialogsAdapter;
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;
    private QBUser currentUser;
    ArrayList<QBChatDialog> qbChatDialogArrayList;
    AppSession mAppSession;
    ListView dialogsListView;
    @BindView(R.id.chatAdd)
    ImageView chatAdd;

    @BindView(R.id.IVback)
    ImageView IVback;


    @BindView(R.id.userSearch)
    EditText userSearch;
    private static final int SPLASH_DELAY = 1500;
    public static void start(Context context) {
        Intent intent = new Intent(context, DialogsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);

        if (checkConfigsWithSnackebarError()) {
            proceedToTheNextActivityWithDelay();
        }

        ButterKnife.bind(this);

        mAppSession=mAppSession.getInstance(this);

        googlePlayServicesHelper = new GooglePlayServicesHelper();

        pushBroadcastReceiver = new PushBroadcastReceiver();

        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();

        dialogsManager = new DialogsManager();

        currentUser = ChatHelper.getCurrentUser();

        initUi();

        userSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

      //  setActionBarTitle(getString(R.string.dialogs_logged_in_as, currentUser.getFullName()));

      //  registerQbChatListeners();
        if (QbDialogHolder.getInstance().getDialogs().size() > 0) {
            loadDialogsFromQb(true, true);
        } else {
            loadDialogsFromQb(false, true);
        }
    }


    private void filter(String text) {
        //new array list that will hold the filtered data
      //  filterdNames = new ArrayList<>();


        //looping through existing elements
        for (QBChatDialog s : qbChatDialogArrayList) {
            //if the existing elements contains the search input
            if (s.getName().equalsIgnoreCase(text)) {
                //adding the element to filtered list
             //   filterdNames.add(s);
                updateDialogsAdapter();
            }

        }


        //calling a method of the adapter class and passing the filtered list

    }

    @Override
    protected void onResume() {
        super.onResume();
        googlePlayServicesHelper.checkPlayServicesAvailable(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(pushBroadcastReceiver,
                new IntentFilter(GcmConsts.ACTION_NEW_GCM_EVENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pushBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterQbChatListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dialogs, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isProcessingResultInProgress) {
            return super.onOptionsItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.menu_dialogs_action_logout:
                userLogout();
                item.setEnabled(false);
                invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            isProcessingResultInProgress = true;
            if (requestCode == REQUEST_SELECT_PEOPLE) {
                ArrayList<QBUser> selectedUsers = (ArrayList<QBUser>) data
                        .getSerializableExtra(SelectUsersActivity.EXTRA_QB_USERS);

                if (isPrivateDialogExist(selectedUsers)) {
                    selectedUsers.remove(ChatHelper.getCurrentUser());
                    QBChatDialog existingPrivateDialog = QbDialogHolder.getInstance().getPrivateDialogWithUser(selectedUsers.get(0));
                    isProcessingResultInProgress = false;
                    ChatActivity.startForResult(DialogsActivity.this, REQUEST_DIALOG_ID_FOR_UPDATE, existingPrivateDialog);
                } else {
                    ProgressDialogFragment.show(getSupportFragmentManager(), R.string.create_chat);
                    createDialog(selectedUsers);
                }
            } else if (requestCode == REQUEST_DIALOG_ID_FOR_UPDATE) {
                if (data != null) {
                    String dialogId = data.getStringExtra(ChatActivity.EXTRA_DIALOG_ID);
                    loadUpdatedDialog(dialogId);
                } else {
                    isProcessingResultInProgress = false;
                    updateDialogsList();
                }
            }
        } else {
            updateDialogsAdapter();
        }
    }

    private boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.addAll(allSelectedUsers);
        selectedUsers.remove(ChatHelper.getCurrentUser());
        return selectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(selectedUsers.get(0));
    }

    private void loadUpdatedDialog(String dialogId) {
        ChatHelper.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle bundle) {
                isProcessingResultInProgress = false;
                QbDialogHolder.getInstance().addDialog(result);
                updateDialogsAdapter();
            }

            @Override
            public void onError(QBResponseException e) {
                isProcessingResultInProgress = false;
            }
        });
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.layout_root);
    }

    @Override
    public ActionMode startSupportActionMode(ActionMode.Callback callback) {
        currentActionMode = super.startSupportActionMode(callback);
        return currentActionMode;
    }

    private void userLogout() {
        ChatHelper.getInstance().destroy();
        logout();
        SharedPrefsHelper.getInstance().removeQbUser();

      //  finish();
      //  LoginActivity.start(DialogsActivity.this);
  //   Intent mIntent = new Intent(DialogsActivity.this,DashboardActivity.class);
  //   startActivity(mIntent);
        QbDialogHolder.getInstance().clear();
      //  ProgressDialogFragment.hide(getSupportFragmentManager());
        finish();
    }

    private void logout() {
        if (QBPushManager.getInstance().isSubscribedToPushes()) {
            QBPushManager.getInstance().addListener(new QBPushSubscribeListenerImpl() {
                @Override
                public void onSubscriptionDeleted(boolean success) {
                    logoutREST();
                    QBPushManager.getInstance().removeListener(this);
                }
            });
            SubscribeService.unSubscribeFromPushes(DialogsActivity.this);
        } else {
            logoutREST();
        }
    }

    private void logoutREST() {
        QBUsers.signOut().performAsync(null);
    }

    private void updateDialogsList() {
        requestBuilder.setSkip(skipRecords = 0);
        loadDialogsFromQb(true, true);
    }

    public void onStartNewChatClick(View view) {
        SelectUsersActivity.startForResult(this, REQUEST_SELECT_PEOPLE);
    }

    private void initUi() {


        LinearLayout emptyHintLayout = _findViewById(R.id.layout_chat_empty);
         dialogsListView = _findViewById(R.id.list_dialogs_chats);
        progressBar = _findViewById(R.id.progress_dialogs);
        fab = _findViewById(R.id.fab_dialogs_new_chat);
        setOnRefreshListener = _findViewById(R.id.swipy_refresh_layout);
          qbChatDialogArrayList= new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values());

     //   qbChatDialogArrayList.add((QBChatDialog) QbDialogHolder.getInstance().getDialogs().values());

        dialogsAdapter = new DialogsAdapter(this,qbChatDialogArrayList);

        TextView listHeader = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.include_list_hint_header, dialogsListView, false);
        listHeader.setText(R.string.dialogs_list_hint);
        dialogsListView.setEmptyView(emptyHintLayout);
      //  dialogsListView.addHeaderView(listHeader, null, false);

        dialogsListView.setAdapter(dialogsAdapter);


        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartNewChatClick(v);
            }
        });

        dialogsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog selectedDialog = (QBChatDialog) parent.getItemAtPosition(position);
                if (currentActionMode == null) {
                    ChatActivity.startForResult(DialogsActivity.this, REQUEST_DIALOG_ID_FOR_UPDATE, selectedDialog);
                } else {
                    dialogsAdapter.toggleSelection(selectedDialog);
                }
            }
        });
        dialogsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog selectedDialog = (QBChatDialog) parent.getItemAtPosition(position);
                startSupportActionMode(new DeleteActionModeCallback());
                dialogsAdapter.selectItem(selectedDialog);
                return true;
            }
        });
        requestBuilder = new QBRequestGetBuilder();

        setOnRefreshListener.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                requestBuilder.setSkip(skipRecords += ChatHelper.DIALOG_ITEMS_PER_PAGE);
                loadDialogsFromQb(true, false);
            }
        });
    }

    private void registerQbChatListeners() {

        incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

        if (incomingMessagesManager != null) {
            incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                    ? allDialogsMessagesListener : new AllDialogsMessageListener());
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                    ? systemMessagesListener : new SystemMessagesListener());
        }

        dialogsManager.addManagingDialogsCallbackListener(this);
    }

    private void unregisterQbChatListeners() {
        if (incomingMessagesManager != null) {
            incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
        }

        dialogsManager.removeManagingDialogsCallbackListener(this);
    }

    private void createDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        isProcessingResultInProgress = false;
                        dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                        ChatActivity.startForResult(DialogsActivity.this, REQUEST_DIALOG_ID_FOR_UPDATE, dialog);
                        ProgressDialogFragment.hide(getSupportFragmentManager());
                    }
                    @Override
                    public void onError(QBResponseException e) {
                        isProcessingResultInProgress = false;
                        ProgressDialogFragment.hide(getSupportFragmentManager());
                        showErrorSnackbar(R.string.dialogs_creation_error, null, null);
                    }
                }
        );
    }
    private void loadDialogsFromQb(final boolean silentUpdate, final boolean clearDialogHolder) {
        isProcessingResultInProgress = true;
        if (!silentUpdate) {
            progressBar.setVisibility(View.VISIBLE);
        }

        ChatHelper.getInstance().getDialogs(requestBuilder, new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> dialogs, Bundle bundle) {
                DialogJoinerAsyncTask dialogJoinerAsyncTask = new DialogJoinerAsyncTask(DialogsActivity.this, dialogs, clearDialogHolder);
                dialogJoinerAsyncTask.execute();
            }

            @Override
            public void onError(QBResponseException e) {
                disableProgress();
                Toast.makeText(DialogsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableProgress() {
        isProcessingResultInProgress = false;
        progressBar.setVisibility(View.GONE);
        setOnRefreshListener.setRefreshing(false);
    }

    private void updateDialogsAdapter() {



        dialogsAdapter.updateList(new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values()));

       Log.e("ffff",""+qbChatDialogArrayList.size());
    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onDialogUpdated(String chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {
        updateDialogsAdapter();
    }

    private class DeleteActionModeCallback implements ActionMode.Callback {

        DeleteActionModeCallback() {
            fab.hide();
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode_dialogs, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_dialogs_action_delete:
                    deleteSelectedDialogs();
                    if (currentActionMode != null) {
                        currentActionMode.finish();
                    }
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            currentActionMode = null;
            dialogsAdapter.clearSelection();
            fab.show();
        }

        private void deleteSelectedDialogs() {
            final Collection<QBChatDialog> selectedDialogs = dialogsAdapter.getSelectedItems();
            ChatHelper.getInstance().deleteDialogs(selectedDialogs, new QBEntityCallback<ArrayList<String>>() {
                @Override
                public void onSuccess(ArrayList<String> dialogsIds, Bundle bundle) {
                    QbDialogHolder.getInstance().deleteDialogs(dialogsIds);
                    updateDialogsAdapter();
                }

                @Override
                public void onError(QBResponseException e) {
                    showErrorSnackbar(R.string.dialogs_deletion_error, e,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteSelectedDialogs();
                                }
                            });
                }
            });
        }
    }

    private class PushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE);
            Log.v(TAG, "Received broadcast " + intent.getAction() + " with data: " + message);
            requestBuilder.setSkip(skipRecords = 0);
            loadDialogsFromQb(true, true);
        }
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {

        }
    }

    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            if (!senderId.equals(ChatHelper.getCurrentUser().getId())) {
                dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
            }
        }
    }

    private static class DialogJoinerAsyncTask extends BaseAsyncTask<Void, Void, Void> {
        private WeakReference<DialogsActivity> activityRef;
        private ArrayList<QBChatDialog> dialogs;
        private boolean clearDialogHolder;

        DialogJoinerAsyncTask(DialogsActivity dialogsActivity, ArrayList<QBChatDialog> dialogs, boolean clearDialogHolder) {
            activityRef = new WeakReference<>(dialogsActivity);
            this.dialogs = dialogs;
            this.clearDialogHolder = clearDialogHolder;
        }

        @Override
        public Void performInBackground(Void... voids) throws Exception {
            ChatHelper.getInstance().join(dialogs);
            return null;
        }

        @Override
        public void onResult(Void aVoid) {
            if (activityRef.get() != null) {
                activityRef.get().disableProgress();
                if (clearDialogHolder) {
                    QbDialogHolder.getInstance().clear();
                }
                QbDialogHolder.getInstance().addDialogs(dialogs);
                activityRef.get().updateDialogsAdapter();
            }
        }

        @Override
        public void onException(Exception e) {
            super.onException(e);
            Toaster.shortToast("Error: " + e.getMessage());
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
          //  DialogsActivity.start(this);
        //    finish();
        } else {
            QBUser currentUser = getUserFromSession();


            if (currentUser == null) {

                //  LoginActivity.start(this);

                buildUsersList();
            } else {
               // buildUsersList();
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
              //  DialogsActivity.start(DialogsActivity.this);
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

        ProgressDialogFragment.show(getSupportFragmentManager());
        List<String> tags = new ArrayList<>();

        tags.add(App.getSampleConfigs().getUsersTag());

        QBUsers.getUsersByTags(tags, null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> result, Bundle params) {

             //   progressDialog.dismiss();

                list =result;
               // QBUser user = new QBUser();
              //  final QBUser user = list;

                for(int i=0;i<list.size();i++){
                    if(list.get(i).getLogin().equalsIgnoreCase(mAppSession.getData("user_id"))){

                        QBUser user = list.get(i);
                       user.setPassword(App.getSampleConfigs().getUsersPassword());
                        //user.setPassword(mAppSession.getData("user_id"));
                        login(user);
                        break;
                    }
                }

                // We use hardcoded password for all users for test purposes
                // Of course you shouldn't do that in your app

              //  user.setLogin(user.getLogin());
              //  user.setPassword(App.getSampleConfigs().getUsersPassword());

              //  login(user);
                //  login()
//                ChatAAdapter adapter = new ChatAAdapter(ChatCopsActivity.this, result,mChatInterFcee);
//                chatlist.setHasFixedSize(true);
//                chatlist.setLayoutManager(new LinearLayoutManager(ChatCopsActivity.this));
//                chatlist.setItemAnimator(new DefaultItemAnimator());
//                chatlist.setAdapter(adapter);
            }

            @Override
            public void onError(QBResponseException e) {
                ErrorUtils.showSnackbar(dialogsListView, R.string.login_cant_obtain_users, e,
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

//                ChatAAdapter adapter = new ChatAAdapter(ChatCopsActivity.this, result,mChatInterFcee);
//                chatlist.setHasFixedSize(true);
//                chatlist.setLayoutManager(new LinearLayoutManager(ChatCopsActivity.this));
//                chatlist.setItemAnimator(new DefaultItemAnimator());
//                chatlist.setAdapter(adapter);
             //   DialogsActivity.start(DialogsActivity.this);
              //  finish();

                ProgressDialogFragment.hide(getSupportFragmentManager());
            }

            @Override
            public void onError(QBResponseException e) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
                ErrorUtils.showSnackbar(dialogsListView, R.string.login_chat_login_error, e,
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
    public void onBackPressed() {
        super.onBackPressed();

       // userLogout();
    }
}