package com.example.myapplication.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DashBoardActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utility.SaveSharedPreference;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.AdvertInterface;
import com.example.myapplication.retrofit.FavoritesInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FavoritesFragment extends Fragment {


    private LinearLayout favoriteContainer;
    Fragment carDescription = new CarDescription();



    public FavoritesFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        favoriteContainer= view.findViewById(R.id.favoritescontainer);
        try {
            getFavorites();
        } catch (GeneralSecurityException |IOException e) {
            e.printStackTrace();
        }
        return view;
    }
    public void sendBundle(Fragment fragment, String keyword) {
        Bundle bundle = new Bundle();
        bundle.putString("idadvert", keyword);
        fragment.setArguments(bundle);
    }

    public void fillFavoriteContainer(int id, String modelstr ,String odometerstr,Bitmap image){
        View favorite= getLayoutInflater().inflate(R.layout.favorite_cards,favoriteContainer,false);
        ImageView imageView = favorite.findViewById(R.id.mainImage);
        TextView model = favorite.findViewById(R.id.title);
        TextView odometer = favorite.findViewById(R.id.odometer);
        imageView.setImageBitmap(image);
        model.setText(modelstr);
        odometer.setText(odometerstr);
        favorite.setId(id);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBundle(carDescription,String.valueOf(favorite.getId()));
                ((DashBoardActivity) requireActivity()).setFragment(carDescription);
            }
        });
        favoriteContainer.addView(favorite);
    }

    private void getFavorites() throws GeneralSecurityException, IOException {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        FavoritesInterface favoritesInterface = retrofit.create(FavoritesInterface.class);
        Call<Object> call = favoritesInterface.getUsersFavorite(SaveSharedPreference.getSessionId(getContext()));
        //shimmerFrameLayout.startShimmer();
        call.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                try {
                    if (response.code() == 200) {
                        //shimmerFrameLayout.stopShimmer();
                       // shimmerFrameLayout.setVisibility(View.GONE);
                        assert response.body() != null;
                        JSONObject jsobj;
                        JSONObject jsonAdvert;

                        JSONArray jsonArray;
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONArray favorites = new JSONArray(jsonObject.getString("data"));
                        for (int i = 0; i < favorites.length(); i++) {
                          //  jsonArray= (JSONArray) cars.getJSONObject(i).get("photos");;
                          //  byte[] backToBytes = Base64.getDecoder().decode(jsonArray.get(0).toString());
                          //  Bitmap bitmap = BitmapFactory.decodeByteArray(backToBytes, 0, backToBytes.length);
                           // jsonArray= (JSONArray) favorites.getJSONObject(i).get("photos");


                            byte[] backToBytes = Base64.getDecoder().decode(favorites.getJSONObject(i).getString("photos"));
                            Bitmap bitmap = BitmapFactory.decodeByteArray(backToBytes, 0, backToBytes.length);
                            fillFavoriteContainer(Integer.parseInt(favorites.getJSONObject(i).getString("idadvert")),
                                                  favorites.getJSONObject(i).getString("title"),
                                                  favorites.getJSONObject(i).getString("price"),
                                    bitmap
                            );
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.password_email_incorrect), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
            }
        });
    }





}