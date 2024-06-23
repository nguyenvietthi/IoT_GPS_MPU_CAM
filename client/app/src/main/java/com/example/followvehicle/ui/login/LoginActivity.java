package com.example.followvehicle.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.followvehicle.MainActivity;
import com.example.followvehicle.R;
import com.example.followvehicle.RegisterActivity;
import com.example.followvehicle.api.StoreUserData;
import com.example.followvehicle.api.api;
import com.example.followvehicle.databinding.ActivityLoginBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String cookie = StoreUserData.getInstance(this).getCookie();
        String email = StoreUserData.getInstance(this).getEmail();

        api.check_cookie(email, cookie, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("MainActivity", "Unexpected code " + response);
                } else {
                    String responseData = response.body().string();
                    String status = "";
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        status = jsonObject.getString("message");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (status.equals("OK")) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("cookie", cookie);
                        intent.putExtra("account_name", StoreUserData.getInstance(LoginActivity.this).getKeyAccountName());
                        startActivity(intent);
                    }
                }
            }
        });


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView registerTextView = binding.register;
        registerTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        registerTextView.setTypeface(registerTextView.getTypeface(), Typeface.NORMAL);
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                api.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingProgressBar.setVisibility(View.GONE);
                            }
                        });

                        Log.e("MainActivity", "Login failed", e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingProgressBar.setVisibility(View.GONE);
                            }
                        });
                        if (!response.isSuccessful()) {
                            Log.e("MainActivity", "Unexpected code " + response);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });
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
                                StoreUserData.getInstance(LoginActivity.this).setCookie(cookie);
                                StoreUserData.getInstance(LoginActivity.this).setKeyAccountName(account_name);
                                StoreUserData.getInstance(LoginActivity.this).setEmail(usernameEditText.getText().toString());

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                intent.putExtra("email", usernameEditText.getText().toString());
                                intent.putExtra("cookie", cookie);
                                intent.putExtra("account_name", account_name);

                                startActivity(intent);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Wrong Password or Email", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });


            }
        });

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}