package copops.com.copopsApp.chat.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chatcamp.uikit.setting.SettingView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import copops.com.copopsApp.R;
import copops.com.copopsApp.activity.DashboardActivity;
import copops.com.copopsApp.chat.LocalStorage;
import copops.com.copopsApp.chat.MainActivity;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.User;

public class SettingFragment extends Fragment {


    private SettingView settingView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        getActivity().setTitle("Setting");
        settingView = view.findViewById(R.id.setting_view);
        settingView.init(this);
        settingView.setUploadAvatarListener(new SettingView.UploadListener() {
            @Override
            public void onUploadProgress(int progress) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUploadSuccess(User user) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onUploadFailed(ChatCampException error) {
                progressBar.setVisibility(View.GONE);
            }
        });
        settingView.setLogoutClickListener(new SettingView.OnLogoutClickListener() {
            @Override
            public void onLogoutClicked() {
                LocalStorage.getInstance().setUsername("");
                LocalStorage.getInstance().setUserId("");
                Intent logoutIntent = new Intent(getActivity(), DashboardActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logoutIntent);
                getActivity().finish();
            }
        });

        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        settingView.onActivityResult(requestCode, resultCode, data);
    }
}
