package copops.com.copopsApp.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chatcamp.uikit.customview.LoadingView;
import com.chatcamp.uikit.messages.RecyclerScrollMoreListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import copops.com.copopsApp.R;
import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.User;
import io.chatcamp.sdk.UserListQuery;

public class SelectContactActivity extends AppCompatActivity  implements UserListAdapter.UserClickListener,  RecyclerScrollMoreListener.OnLoadMoreListener{
    private      Timer    timer       = new Timer();
    private      boolean  isFirstTime = true;
    private      Context  mContext;

    private UserListQuery    query;
    private UserListAdapter  contactAdapter;

    @BindView(R.id.iv_recent_back_arrow)
    ImageView ivRecentBack;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_search_user)
    EditText etSearcContact;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.loading_view)
    LoadingView loadingView;
    @BindView(R.id.tv_place_holder)
    TextView tvPlaceText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        mContext=SelectContactActivity.this;
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
        }
        ivRecentBack.setOnClickListener(onClickBack);
        query = ChatCamp.createUserListQuery();
        contactAdapter = new UserListAdapter(mContext);
        contactAdapter.setUserClickListener(this);
        @SuppressLint("WrongConstant")
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerScrollMoreListener recyclerScrollMoreListener = new RecyclerScrollMoreListener(manager, this);
        recyclerView.addOnScrollListener(recyclerScrollMoreListener);
        recyclerView.setAdapter(contactAdapter);
        loadUsers("", false);
        etSearcContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loadUsers(editable.toString(), true);
            }
        });
    }
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
    protected View.OnClickListener onClickBack=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         finish();
        }
    };
}
