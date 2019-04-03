package copops.com.copopsApp.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import copops.com.copopsApp.R;


public class TestConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_conversation);

        TestConversationFragment fragment = new TestConversationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channelType", getIntent().getStringExtra("channelType"));
        bundle.putString("channelId", getIntent().getStringExtra("channelId"));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
