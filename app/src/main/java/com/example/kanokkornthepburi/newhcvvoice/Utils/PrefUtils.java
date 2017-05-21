package com.example.kanokkornthepburi.newhcvvoice.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
    private SharedPreferences mSharePreferences;
    private Context mContext;
    private static PrefUtils mInstance;
    private static final String PREF_USER = "PREF_USER";
    private static final String PREF_MIC = "PREF_MIC";
    private static final String PREF_HOME = "PREF_HOME";

    private PrefUtils(Context context) {
        this.mContext = context;
        mSharePreferences = mContext.getSharedPreferences("HCVVOICE_DATA", Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        mInstance = new PrefUtils(context);
    }

    public static PrefUtils getInstance() {
        if (mInstance != null) {
            return mInstance;
        } else {
            throw new NullPointerException();
        }
    }

    public Boolean isLogin() {
        return mSharePreferences.contains(PREF_USER);
    }

    public void setUsername(String username) {
        mSharePreferences.edit().putString(PREF_USER, username).apply();
    }

    public String getUsername() {
        return mSharePreferences.getString(PREF_USER, "");
    }

    public void setLogout() {
        mSharePreferences.edit().remove(PREF_USER).apply();
    }

    public void setMic(boolean status) {
        mSharePreferences.edit().putBoolean(PREF_MIC, status).apply();
    }

    public boolean isOpenMic() {
        return mSharePreferences.getBoolean(PREF_MIC, false);
    }

    public String getHome() {
        return mSharePreferences.getString(PREF_HOME, "");
    }

    public void setHome(String home) {
        mSharePreferences.edit().putString(PREF_HOME, home).apply();
    }
}
