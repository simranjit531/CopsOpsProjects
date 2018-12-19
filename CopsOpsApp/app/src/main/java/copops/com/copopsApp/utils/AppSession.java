package copops.com.copopsApp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSession {
    private static AppSession yourPreference;
    private SharedPreferences sharedPreferences;

    public static AppSession getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new AppSession(context);
        }
        return yourPreference;
    }

    private AppSession(Context context) {
        sharedPreferences = context.getSharedPreferences("copops.com.copopsApp",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
