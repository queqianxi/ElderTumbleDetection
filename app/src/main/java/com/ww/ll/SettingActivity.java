package com.ww.ll;


import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Window;

/**
 * 设置页面
 * @author Ww
 * @date 2018/5/21
 */

public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    public static final String KEY_NAME = "pre_key_name";
    public static final String KEY_SEX = "pre_key_sex";
    public static final String KEY_AGE = "pre_key_age";
    public static final String KEY_ALERT = "pre_key_alert";
    public static final String KEY_VIBRATE = "pre_key_vibrate";
    public static final String KEY_PHONE = "pre_key_phone";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences);
        ActionBar actionBar = getActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

//        if(key.equals(KEY_AGE)){
//            Preference agePre = findPreference(key);
//            agePre.setSummary(sharedPreferences.getString(key, "请输入年龄"));
//        }
        switch (key){
            case KEY_NAME:
                Preference namePre = findPreference(key);
                namePre.setSummary(sharedPreferences.getString(key, ""));
                break;
            case KEY_SEX:
                Preference sexPre = findPreference(key);
                sexPre.setSummary(sharedPreferences.getString(key, ""));
                break;
            case KEY_AGE:
                Preference agePre = findPreference(key);
                agePre.setSummary(sharedPreferences.getString(key, ""));
                break;
            case KEY_ALERT:
                Preference alertPre = findPreference(key);
                alertPre.setSummary(sharedPreferences.getString(key, ""));
                break;
            case KEY_VIBRATE:
                break;
            case KEY_PHONE:
                Preference phonePre = findPreference(key);
                phonePre.setSummary(sharedPreferences.getString(key, ""));
                break;
                default:
                    break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
