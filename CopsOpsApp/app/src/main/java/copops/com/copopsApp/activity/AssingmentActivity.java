package copops.com.copopsApp.activity;

import androidx.appcompat.app.AppCompatActivity;
import copops.com.copopsApp.R;
import copops.com.copopsApp.fragment.AssignmentTableFragment;
import copops.com.copopsApp.utils.Utils;

import android.os.Bundle;

public class AssingmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assingment);
        Utils.fragmentCall(new AssignmentTableFragment(), getSupportFragmentManager());
    }
}
