package com.dc024.ptsgenap_11rpl1_absen16;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity {
    private EditText et_username, et_password;
    private boolean isFormFilled = false;
    private SharedPreferences preferences;
    private CircularProgressButton btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        et_password = findViewById(R.id.etPassword);
        et_username = findViewById(R.id.etEmail);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            private void doNothing() {
            }

            @Override
            public void onClick(View v) {
                isFormFilled = true;
                final String hp = et_username.getText().toString();
                final String password = et_password.getText().toString();

                if (hp.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Harap lengkapi isian yang tersedia", Toast.LENGTH_SHORT).show();
                    isFormFilled = false;
                }
                if (isFormFilled) {
                    btnLogin.startAnimation();
                    HashMap<String, String> body = new HashMap<>();
                    body.put("username", hp);
                    body.put("password", password);
                    AndroidNetworking.post("http://192.168.43.153/TugasPTS/api/auth.php")
                            .addBodyParameter(body)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("GZS", "respon : " + response);

                                    String status = response.optString("STATUS");
                                    String message = response.optString("MESSAGE");
                                    if (status.equalsIgnoreCase("SUCCESS")) {
                                        JSONObject payload = response.optJSONObject("PAYLOAD");
                                        String U_ID = payload.optString("LOGIN_ID");
                                        String U_NAME = payload.optString("LOGIN_NAME");


                                        preferences = getSharedPreferences("Tugas PTS", Context.MODE_PRIVATE);
                                        preferences.edit()
                                                .putString("id", U_ID)
                                                .putString("name", U_NAME)
                                                .apply();

                                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                        finish();
                                        finishAffinity();
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }

                                    btnLogin.revertAnimation();
                                }

                                @Override
                                public void onError(ANError anError) {
                                    btnLogin.revertAnimation();
                                    Toast.makeText(LoginActivity.this, "ERROR LUR", Toast.LENGTH_SHORT).show();
                                    Log.d("GZS", "onError: " + anError.getErrorBody(    ));
                                    Log.d("GZS", "onError: " + anError.getLocalizedMessage());
                                    Log.d("GZS", "onError: " + anError.getErrorDetail());
                                    Log.d("GZS", "onError: " + anError.getResponse());
                                    Log.d("GZS  ", "onError: " + anError.getErrorCode());
                                }
                            });
                }
            }
        });
    }
}
