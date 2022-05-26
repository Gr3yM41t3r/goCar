package com.example.myapplication.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AdvertInterface {


    @POST("annonce/getalladvert")
    Call<Object> getAdverts(@Body String usertoken);

    @POST("annonce/getsearchadvert")
    Call<Object> getadvertbysearch(@Body String keyword);

    @POST("annonce/getadvertbytype")
    Call<Object> getadvertbytype(@Body String keyword);

    @POST("annonce/getadvertbyfilter")
    Call<Object> getadvertbyfilter(@Body String keyword);

    @POST("annonce/getsinleadvert")
    Call<Object> getSingleCarDescription(@Body String keyword);

    @POST("annonce/addadvert")
    Call<Object> addAdvert(@Body String keyword);

    @POST("annonce/getusersAdverts")
    Call<Object> getusersAdverts(@Body String keyword);

    @POST("annonce/getusersAdvert")
    Call<Object> getuserAdvert(@Body String keyword);

    @POST("annonce/boost")
    Call<Object> boost(@Body String keyword);


}
