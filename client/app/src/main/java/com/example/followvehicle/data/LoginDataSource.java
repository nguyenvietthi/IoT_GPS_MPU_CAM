package com.example.followvehicle.data;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.followvehicle.MainActivity;
import com.example.followvehicle.api.StoreUserData;
import com.example.followvehicle.api.api;
import com.example.followvehicle.data.model.LoggedInUser;
import com.example.followvehicle.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    LoggedInUser fakeUser;

    int check;

    public Result<LoggedInUser> login(String username, String password) {



        try {
            api.login(username, password, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("MainActivity", "Login failed", e);
                    check = 0;
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("MainActivity", "Unexpected code " + response);
                    } else {
                        String responseData = response.body().string();
                        String cookie = "";
                        String status = "";
                        String account_name = "";
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            cookie = jsonObject.getString("cookie");
                            status = jsonObject.getString("message");
                            account_name = jsonObject.getString("name");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (status.equals("OK")) {
                            Log.i("MainActivity", "Login successful: " + cookie);
                             check = 2;
                             fakeUser = new LoggedInUser(
                                username,
                                username,
                                cookie,
                                account_name
                             );
                        } else {
                            check = 1;
                        }
                    }
                }
            });

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }

        switch (check){
            case 0: {
                return new Result.Error(new IOException("Check Your Internet Connection", new Exception()));
            }
            case 1: {
                return new Result.Error(new IOException("Wrong Info", new Exception()));
            }
            case 2: {
                return new Result.Success<>(fakeUser);
            }
            default:
                return null;
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}