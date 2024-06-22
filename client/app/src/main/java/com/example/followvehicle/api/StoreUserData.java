package com.example.followvehicle.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StoreUserData {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_COOKIE = "cookie";
    private static final String KEY_DEVICE_CODE = "device_code";
    private static final String KEY_ACCOUNT_NAME = "account_name";

    private static final String KEY_NOTIFICATIONS = "notifications";

    private SharedPreferences sharedPreferences;
    private static StoreUserData instance;

    private Gson gson;
    private StoreUserData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
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

    public void setNotifications(List<String> notifications) {
        String json = gson.toJson(notifications);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NOTIFICATIONS, json);
        editor.apply();
    }

    public List<String> getNotifications() {
        String json = sharedPreferences.getString(KEY_NOTIFICATIONS, null);
        if (json != null) {
            Type type = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    public void addNotification(String notification) {
        List<String> notifications = getNotifications();
        notifications.add(notification);
        setNotifications(notifications);
    }
}
