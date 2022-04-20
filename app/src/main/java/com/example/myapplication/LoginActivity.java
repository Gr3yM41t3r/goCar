package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.model.Compte;
import com.example.myapplication.retrofit.LoginInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void sendNetworkRequest(Compte compte){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://localhost:3000/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        LoginInterface login = retrofit.create(LoginInterface.class);
        login.login(compte);

    }
}