package com.example.myapplication.retrofit;

import com.example.myapplication.model.Compte;

import org.intellij.lang.annotations.JdkConstants;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface LoginInterface {
    @POST("login")
    Call<Object> login(@Body Compte compte);


    @POST("getuserdata")
    Call<Object> getUserData(@Body String token);
}
