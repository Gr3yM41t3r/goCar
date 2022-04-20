package com.example.myapplication.retrofit;

import com.example.myapplication.model.Compte;

import org.intellij.lang.annotations.JdkConstants;

import retrofit2.Call;
import retrofit2.http.POST;


public interface LoginInterface {
    @POST("login")
    Call<Compte> login(Compte compte);
}
