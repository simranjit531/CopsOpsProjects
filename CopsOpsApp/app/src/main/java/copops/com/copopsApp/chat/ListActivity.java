package copops.com.copopsApp.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import copops.com.copopsApp.R;
import copops.com.copopsApp.chat.setting.SettingFragment;
import copops.com.copopsApp.shortcut.ShortcutViewService;
import copops.com.copopsApp.shortcut.ShortcutViewService_Citizen;


public class ListActivity extends AppCompatActivity {
    private Context mContext;


    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private BottomNavigationView navigation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_list);
            mContext=ListActivity.this;
            mainThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopService(new Intent(mContext, ShortcutViewService.class));

                    stopService(new Intent(mContext, ShortcutViewService_Citizen.class));
                }
            }, 1000);
            navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            handleNavigationGroupChannels();
        }catch (Exception e)
        {
            e.printStackTrace();}

    }


    private void handleNavigationGroupChannels() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new GroupChannelListFragment()).commit();
    }

    private void handleNavigationOpenChannels() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new OpenChannelListFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_open_channels:
                    handleNavigationOpenChannels();
                    return true;
                case R.id.navigation_group_channels:
                     handleNavigationGroupChannels();
                    return true;
                case R.id.navigation_user_list:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new UserListFragment()).commit();
                    return true;
                case R.id.navigation_settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingFragment()).commit();
                    return true;
            }
            return false;
        }
    };
}
