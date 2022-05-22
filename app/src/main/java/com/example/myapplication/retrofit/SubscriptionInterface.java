package com.example.myapplication.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SubscriptionInterface {

    @POST("updatesubscription")
    Call<Object> updateSubscription(@Body String type);
}
