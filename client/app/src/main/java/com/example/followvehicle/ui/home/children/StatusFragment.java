package com.example.followvehicle.ui.home.children;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.followvehicle.MainActivity;
import com.example.followvehicle.R;
import com.example.followvehicle.api.StoreUserData;
import com.example.followvehicle.api.api;
import com.example.followvehicle.model.MPU;
import com.example.followvehicle.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StatusFragment extends Fragment {
    private MPU mpuInfo;
    private TextView textViewstatusLabel;
    private TextView textViewsaccxValue;
    private TextView textViewsaccyValue;
    private TextView textViewsacczValue;
    private TextView textViewsgyroxValue;
    private TextView textViewsgyroyValue;
    private TextView textViewsgyrozValue;

    private TextView textViewtemperatureValue;

    private Handler handler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);;

        textViewstatusLabel = view.findViewById(R.id.statusLabel);
        textViewsaccxValue = view.findViewById(R.id.accxValue);
        textViewsaccyValue = view.findViewById(R.id.accyValue);
        textViewsacczValue = view.findViewById(R.id.acczValue);
        textViewsgyroxValue = view.findViewById(R.id.gyroxValue);
        textViewsgyroyValue = view.findViewById(R.id.gyroyValue);
        textViewsgyrozValue = view.findViewById(R.id.gyrozValue);
        textViewtemperatureValue = view.findViewById(R.id.temperatureValue);

        String DEVICE_ID = StoreUserData.getInstance(requireContext()).getKeyDeviceCode();
        String email = StoreUserData.getInstance(requireContext()).getEmail();
        String cookie = StoreUserData.getInstance(requireContext()).getCookie();

        UpdateMPU(DEVICE_ID, cookie, email);
        handler = new Handler(Looper.getMainLooper());
        startUpdateMPU(DEVICE_ID, email, cookie);

        return view;

    }
    private void startUpdateMPU(String DEVICE_ID, String cookie, String email) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UpdateMPU(DEVICE_ID, cookie, email);
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    private void UpdateMPU(String DEVICE_ID, String cookie, String email) {
        api.get_current_mpu(DEVICE_ID, cookie, email, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewstatusLabel.setText("LOST CONNECTION!!!");
                            textViewstatusLabel.setTextColor(Color.parseColor("#FF0000"));
                        }
                    });
                } else {
                    String responseData = response.body().string();
                    double accx = 0;
                    double accy = 0;
                    double accz = 0;
                    double gyrox = 0;
                    double gyroy = 0;
                    double gyroz = 0;
                    double temperature = 0;
                    String status = "";

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        accx = jsonObject.getDouble("accx");
                        accy = jsonObject.getDouble("accy");
                        accz = jsonObject.getDouble("accz");
                        gyrox = jsonObject.getDouble("gyrox");
                        gyroy = jsonObject.getDouble("gyroy");
                        gyroz = jsonObject.getDouble("gyroz");
                        temperature = jsonObject.getDouble("temperature");
                        status = jsonObject.getString("message");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (status.equals("OK")) {
                        mpuInfo = new MPU(accx, accy, accz, gyrox, gyroy, gyroz, temperature);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                textView.setText(mpuInfo.toString());
                                textViewstatusLabel.setText("STATUS: OK");
                                textViewsaccxValue.setText(String.valueOf(mpuInfo.getAccx()));
                                textViewsaccyValue.setText(String.valueOf(mpuInfo.getAccy()));
                                textViewsacczValue.setText(String.valueOf(mpuInfo.getAccz()));
                                textViewsgyroxValue.setText(String.valueOf(mpuInfo.getGyrox()));
                                textViewsgyroyValue.setText(String.valueOf(mpuInfo.getGyroy()));
                                textViewsgyrozValue.setText(String.valueOf(mpuInfo.getGyroz()));
                                textViewtemperatureValue.setText(String.valueOf(mpuInfo.getTemperature()));

                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textViewstatusLabel.setText("LOST CONNECTION!!!");
                                textViewstatusLabel.setTextColor(Color.parseColor("#FF0000"));
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    public void onStart() {
        super.onStart();
    }


    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}