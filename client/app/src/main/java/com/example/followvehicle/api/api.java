package com.example.followvehicle.api;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.*;
import java.io.IOException;
public class api {
    private static final String BASE_URL = "http://20.2.67.40:1234/";
    private static final OkHttpClient client = new OkHttpClient();

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void login(String email, String password, Callback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "login")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void check_cookie(String email, String cookie, Callback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("cookie", cookie);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "check_cookie")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void get_current_location(String DEVICE_ID, String cookie, String email, Callback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("cookie", cookie);
            jsonBody.put("device_id", DEVICE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "get_current_location")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void get_current_mpu(String DEVICE_ID, String cookie, String email, Callback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("cookie", cookie);
            jsonBody.put("device_id", DEVICE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "get_current_mpu")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(callback);
    }


    public static void logout(String cookie, String email,  Callback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("cookie", cookie);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "logout")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(callback);
    }
    public static void getLocationHistoryList(String cookie, String email, String startTime, String finishTime, String deviceId, Callback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("cookie", cookie);
            jsonBody.put("startTime", startTime);
            jsonBody.put("finishTime", finishTime);
            jsonBody.put("deviceId", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "get_location_history_list")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(callback);
    }

}
