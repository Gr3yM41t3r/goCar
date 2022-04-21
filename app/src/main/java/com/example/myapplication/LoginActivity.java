package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.constant.Constants;
import com.example.myapplication.model.Compte;
import com.example.myapplication.retrofit.LoginInterface;

import org.json.JSONException;
import org.json.JSONObject;
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
        Button login = findViewById(R.id.login);
        EditText email = findViewById(R.id.emailInput);
        EditText password = findViewById(R.id.passwordInput);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                if (TextUtils.isEmpty(emailValue)||TextUtils.isEmpty(passwordValue)){
                    email.setError("requis");
                    password.setError("requis");
                }else {
                    Compte compte =new Compte(emailValue,passwordValue);
                    sendNetworkRequest(compte);
                }

            }
        });
    }

    private void sendNetworkRequest(Compte compte){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL+"api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        LoginInterface login = retrofit.create(LoginInterface.class);
        Call<Object> call =login.login(compte);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
               // assert response.body() != null;
                try {
                    if(response.code() ==200){
                        String s = response.body().toString();
                        JSONObject jsonObject = new JSONObject(s);
                        jsonObject = new JSONObject(String.valueOf(jsonObject));
                        Toast.makeText(LoginActivity.this,"Logged In",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(LoginActivity.this,"Error Repeat Again",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"failure",Toast.LENGTH_LONG).show();
            }
        });

    }
}
