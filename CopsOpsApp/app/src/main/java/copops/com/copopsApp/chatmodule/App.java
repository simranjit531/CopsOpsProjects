package copops.com.copopsApp.chatmodule;



import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.ActivityLifecycle;

import java.io.IOException;

import copops.com.copopsApp.chatmodule.models.SampleConfigs;
import copops.com.copopsApp.chatmodule.utils.Consts;
import copops.com.copopsApp.chatmodule.utils.configs.ConfigUtils;
//import io.fabric.sdk.android.Fabric;

public class App extends CoreApp {
    private static final String TAG = App.class.getSimpleName();
    private static SampleConfigs sampleConfigs;

    @Override
    public void onCreate() {
        super.onCreate();
     //   initFabric();
        ActivityLifecycle.init(this);
        initSampleConfigs();
    }

    private void initSampleConfigs() {
        try {
            sampleConfigs = ConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SampleConfigs getSampleConfigs() {
        return sampleConfigs;
    }

    private void initFabric() {
//        if (!BuildConfig.DEBUG) {
//            Fabric.with(this, new Crashlytics());
//        }
    }
}