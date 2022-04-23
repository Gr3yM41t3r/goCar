package com.example.myapplication.retrofit;

import com.example.myapplication.model.Compte;
import com.example.myapplication.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterInterface {
    @POST("register")
    Call<Object> register(@Body UserModel user);
}
