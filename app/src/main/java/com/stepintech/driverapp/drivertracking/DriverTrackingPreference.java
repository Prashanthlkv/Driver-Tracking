package com.stepintech.driverapp.drivertracking;

import android.content.Context;
import android.content.SharedPreferences;

public class DriverTrackingPreference {

    private static final String TAG = "AlarmHandler";
    private static DriverTrackingPreference sSharedPrefs;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private static final String PREFERENCE_NAME = "driver_tracking_preference";

    /**
     * Class for keeping all the keys used for shared preferences in one place.
     */
    static class Key {
        static final String IS_TRACKING_ON = "is_tracking_on";
    }

    private DriverTrackingPreference(){
        //Empty constructor.
    }

    private DriverTrackingPreference(Context context) {
        mPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    static DriverTrackingPreference getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new DriverTrackingPreference(context.getApplicationContext());
        }
        return sSharedPrefs;
    }

    public void put(String key, String val) {
        doEdit();
        mEditor.putString(key, val);
        doCommit();
    }

    public void put(String key, boolean val) {
        doEdit();
        mEditor.putBoolean(key, val);
        doCommit();
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    private void doEdit() {
        if (mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (mEditor != null) {
            mEditor.apply();
            mEditor = null;
        }
    }


}
