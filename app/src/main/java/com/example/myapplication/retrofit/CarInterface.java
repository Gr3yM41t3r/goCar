package com.example.myapplication.retrofit;

import com.example.myapplication.model.Compte;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CarInterface {

    @GET("annonce/getall")
    Call<Object> getCars();

    @GET("annonce/image")
    Call<Object> getImage();
}
