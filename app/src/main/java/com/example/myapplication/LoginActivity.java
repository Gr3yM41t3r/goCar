package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.constant.Constants;
import com.example.myapplication.model.Compte;
import com.example.myapplication.retrofit.LoginInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText email = findViewById(R.id.emailInput);
        EditText password = findViewById(R.id.passwordInput);
        TextView signUp = findViewById(R.id.signUp);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                if (TextUtils.isEmpty(emailValue) || TextUtils.isEmpty(passwordValue)) {
                    email.setError("requis");
                    password.setError("requis");
                } else {
                    if (isEmailValid(emailValue)) {
                        Compte compte = new Compte(emailValue, passwordValue);
                        sendNetworkRequest(compte);
                    } else {
                        email.setError("email invalide");
                    }
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendNetworkRequest(Compte compte) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        LoginInterface login = retrofit.create(LoginInterface.class);
        Call<Object> call = login.login(compte);
        ProgressDialog progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMessage("please wait");
        progressDoalog.setTitle("connection en cours");
        progressDoalog.show();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        String s = response.body().toString();
                        JSONObject jsonObject = new JSONObject(s);
                        jsonObject = new JSONObject(String.valueOf(jsonObject));
                        Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_LONG).show();
                        SaveSharedPreference.setUsersEmail(LoginActivity.this, new JSONObject(jsonObject.getString("data")).getString("email"));
                        progressDoalog.dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error Repeat Again", Toast.LENGTH_LONG).show();
                        progressDoalog.dismiss();

                    }
                } catch (JSONException | IOException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Service Indisponible", Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
            }
        });
    }
}
