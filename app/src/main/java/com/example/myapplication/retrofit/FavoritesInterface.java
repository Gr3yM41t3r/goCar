package com.example.myapplication.retrofit;

import com.example.myapplication.model.Compte;
import com.example.myapplication.model.Favorites;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FavoritesInterface {

    @POST("favorites/add")
    Call<Object> addFavorites(@Body Favorites favorites);

    @POST("favorites/getusers")
    Call<Object> getUsersFavorite(@Body String userID);

    @POST("favorites/delete")
    Call<Object> deleteFavorites(@Body Favorites favorites);
}
