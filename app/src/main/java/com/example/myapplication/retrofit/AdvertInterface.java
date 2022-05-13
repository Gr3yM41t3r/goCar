package com.example.myapplication.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AdvertInterface {


    @GET("annonce/getalladvert")
    Call<Object> getAdverts();

    @POST("annonce/getsearchadvert")
    Call<Object> getadvertbysearch(@Body String keyword);

    @POST("annonce/getsinleadvert")
    Call<Object> getSingleCarDescription(@Body String keyword);


}
