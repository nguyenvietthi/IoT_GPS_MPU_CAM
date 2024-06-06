package com.example.followvehicle.api;

import android.content.Context;
import android.content.SharedPreferences;
public class StoreUserData {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_COOKIE = "cookie";
    private static final String KEY_DEVICE_CODE = "device_code";
    private static final String KEY_ACCOUNT_NAME = "account_name";
    private SharedPreferences sharedPreferences;
    private static StoreUserData instance;
    private StoreUserData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized StoreUserData getInstance(Context context) {
        if (instance == null) {
            instance = new StoreUserData(context.getApplicationContext());
        }
        return instance;
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public void setCookie(String cookie) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_COOKIE, cookie);
        editor.apply();
    }

    public String getCookie() {
        return sharedPreferences.getString(KEY_COOKIE, null);
    }
    public void setKeyDeviceCode(String cookie) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_COOKIE , cookie);
        editor.apply();
    }

    public String getKeyDeviceCode() {
        return sharedPreferences.getString(KEY_DEVICE_CODE, null);
    }

    public void setKeyAccountName(String accountName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCOUNT_NAME , accountName);
        editor.apply();
    }

    public String getKeyAccountName() {
        return sharedPreferences.getString(KEY_ACCOUNT_NAME, null);
    }
}
