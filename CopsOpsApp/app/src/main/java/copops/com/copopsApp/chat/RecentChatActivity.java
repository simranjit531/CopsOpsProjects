package copops.com.copopsApp.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;







import com.chatcamp.uikit.channel.ChannelAdapter;
import com.chatcamp.uikit.channel.ChannelList;
import com.chatcamp.uikit.customview.LoadingView;
import com.chatcamp.uikit.messages.RecyclerScrollMoreListener;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import copops.com.copopsApp.shortcut.ShortcutViewService;
import copops.com.copopsApp.shortcut.ShortcutViewService_Citizen;
import copops.com.copopsApp.utils.AppSession;
import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.User;
import io.chatcamp.sdk.UserListQuery;

public class RecentChatActivity extends AppCompatActivity implements UserListAdapter.UserClickListener,  RecyclerScrollMoreListener.OnLoadMoreListener {

    private      Timer    timer       = new Timer();
    private UserListQuery    query;
    private UserListAdapter  contactAdapter;

    private      boolean  isFirstTime = true;
    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private Context mContext;
    @BindView(R.id.tv_place_holder)
    TextView tvPlaceText;
    @BindView(R.id.iv_recent_back)
    ImageView ivRecentBack;
    @BindView(R.id.iv_person_add)
    ImageView ivPersonAdd;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.channel_list)
    ChannelList channelList;
    @BindView(R.id.loading_view)
    LoadingView loadingView;
    @BindView(R.id.et_search_recent)
    EditText recentSearch;
    @BindView(R.id.recycler_view)
    RecyclerView contactList;
    private GroupChannelListQuery.ParticipantState groupFilter;
    AppSession mAppSession;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chat);
        ButterKnife.bind(this);
        mContext=RecentChatActivity.this;

        mAppSession = mAppSession.getInstance(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (decor!=null) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);
            }
            //mAppSession.getData()
        }
        initId();
        channelList.setLoadingView(loadingView);




        channelList.setChannelClickListener(new ChannelAdapter.ChannelClickedListener() {
            @Override
            public void onClick(BaseChannel baseChannel) {

                mAppSession.saveData("countNoti","0");
              //  mAppSession.saveData("countNoti","0");
                Intent intent = new Intent(mContext, ConversationActivity.class);
                intent.putExtra("channelType", "group");
                intent.putExtra("DISPLY_NAME","");
                intent.putExtra("participantState", groupFilter.name());
                String aaaa =   baseChannel.getName();
                intent.putExtra("channelId", baseChannel.getId());
                startActivity(intent);
            }
        });
        channelList.setOnChannelsLoadedListener(new ChannelList.OnChannelsLoadedListener() {
            @Override
            public void onChannelsLoaded() {
                progressBar.setVisibility(View.GONE);
                if(channelList.getAdapter().getItemCount() == 0) {

                    tvPlaceText.setVisibility(View.VISIBLE);
                } else {
                    tvPlaceText.setVisibility(View.GONE);
                }
            }
        });
        query = ChatCamp.createUserListQuery();
        contactAdapter = new UserListAdapter(mContext);
        contactAdapter.setUserClickListener(this);
        @SuppressLint("WrongConstant")
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        contactList.setLayoutManager(manager);
        RecyclerScrollMoreListener recyclerScrollMoreListener = new RecyclerScrollMoreListener(manager, this);
        contactList.addOnScrollListener(recyclerScrollMoreListener);
        contactList.setAdapter(contactAdapter);
        loadUsers("", false);
        recentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strTxt=editable.toString();
                if (!strTxt.equals(""))
                {
                    channelList.setVisibility(View.GONE);
                    contactList.setVisibility(View.VISIBLE);
                    loadUsers(editable.toString(), true);
                }else
                {
                    channelList.setVisibility(View.VISIBLE);
                    contactList.setVisibility(View.GONE);
                    if(groupFilter == null) {
                        groupFilter = GroupChannelListQuery.ParticipantState.ALL;
                    }
                    channelList.setChannelType(BaseChannel.ChannelType.GROUP, groupFilter);
                }

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if(groupFilter == null) {
            groupFilter = GroupChannelListQuery.ParticipantState.ALL;
        }

      channelList.setChannelType(BaseChannel.ChannelType.GROUP, groupFilter);
    }
    public void initId()
    {

        ivRecentBack.setOnClickListener(onClickRecentBack);
        ivPersonAdd.setOnClickListener(onClickPesonAdd);
        mainThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopService(new Intent(mContext, ShortcutViewService.class));

                stopService(new Intent(mContext, ShortcutViewService_Citizen.class));
            }
        }, 1000);

    }
    protected View.OnClickListener onClickRecentBack=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAppSession.saveData("screenShow","abc");
            finish();
        }
    };
    protected View.OnClickListener onClickPesonAdd=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          startActivity(new Intent(mContext,SelectContactActivity.class));
        }
    };


    private void loadUsers(String searchText, boolean hasTextChanged) {
        if (!TextUtils.isEmpty(searchText) || hasTextChanged) {
            query = ChatCamp.createUserListQuery();
            query.setDisplayNameSearch(searchText);
            isFirstTime = true;
        }
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                query.load(20, new UserListQuery.ResultHandler() {
                    @Override
                    public void onResult(List<User> userList, ChatCampException e) {
                        if (isFirstTime) {
                            isFirstTime = false;
                            progressBar.setVisibility(View.GONE);
                            contactAdapter.clear();
                            if (userList.size() == 0) {
                                tvPlaceText.setVisibility(View.VISIBLE);
                            } else {
                                tvPlaceText.setVisibility(View.GONE);
                            }
                        }
                        for (User user : userList) {
                            if (user.getId().equals(ChatCamp.getCurrentUser().getId())) {
                                userList.remove(user);
                                break;
                            }
                        }
                        loadingView.setVisibility(View.GONE);
                        contactAdapter.addAll(userList);

                    }
                });
            }
        }, 500);

    }
    @Override
    public void onUserClicked(User user) {
        //  String displayName=user.getDisplayName();
        GroupChannel.create("OneToOne", new String[]{ChatCamp.getCurrentUser().getId(), user.getId()}, true, new BaseChannel.CreateListener() {
            @Override
            public void onResult(BaseChannel groupChannel, ChatCampException e) {
                Intent intent = new Intent(mContext, ConversationActivity.class);
                intent.putExtra("channelType", "group");
                intent.putExtra("DISPLY_NAME",user.getDisplayName());
                intent.putExtra("participantState", GroupChannel.ChannelType.GROUP.name());

                intent.putExtra("channelId", groupChannel.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoadMore(int page, int total) {
        loadingView.setVisibility(View.VISIBLE);
        loadUsers("", false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



        mAppSession.saveData("screenShow","abc");
        finish();
    }


}
