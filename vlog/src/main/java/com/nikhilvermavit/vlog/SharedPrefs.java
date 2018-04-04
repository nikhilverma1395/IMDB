package com.nikhilvermavit.vlog;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class SharedPrefs {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {
        preferences = context.getSharedPreferences(Config.prefNAME, Context.MODE_PRIVATE);
    }

    public void storeValue(String key, String value) {
        editor = preferences.edit();
        editor.putString(key, value.trim()).apply();
    }

    public void storeBoolValue(String key, boolean value) {
        editor = preferences.edit();
        editor.putBoolean(key, value).apply();
    }

    public String getValue(String key, String value) {
        return preferences.getString(key, value);
    }

    public int getIntValue(String prefix, int err) {
        return preferences.getInt(prefix, err);
    }


    public boolean getBoolValue(String prefix) {
        return preferences.getBoolean(prefix, false);
    }

    public boolean getBoolValue(String prefix, boolean or) {
        return preferences.getBoolean(prefix, or);
    }

    public void storeIntValue(String dateReneWPREF, int i) {
        editor = preferences.edit();
        editor.putInt(dateReneWPREF, i).apply();
    }
}
