package com.example.myapplication.retrofit;

import com.example.myapplication.model.Compte;
import com.example.myapplication.model.Organisation;
import com.example.myapplication.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterInterface {
    @POST("register/particulier")
    Call<Object> registerParticulier(@Body UserModel user);

    @POST("register/professionel")
    Call<Object> registerProfessionel(@Body Organisation organisation);

    @POST("process")
    Call<Object> process(@Body String email);

    @POST("register/professionel/getcity")
    Call<Object> getCity(@Body String zipcode);
}
