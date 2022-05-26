package com.example.myapplication.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Vibrator;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utility.SaveSharedPreference;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.model.Favorites;
import com.example.myapplication.retrofit.AdvertInterface;
import com.example.myapplication.retrofit.FavoritesInterface;
import com.facebook.shimmer.ShimmerFrameLayout;
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


public class CarDescription extends Fragment {


    private TextView adverttitle;
    private TextView owner;
    private TextView brand;
    private TextView model;
    private TextView yearmode;
    private TextView odometer;
    private TextView fuel;
    private TextView gearbox;
    private TextView type;
    private TextView color;
    private TextView startservice;
    private TextView fiscalPower;
    private TextView dinPower;
    private TextView date;
    private TextView description;
    private ImageView mainImage;
    private ImageView favorite;
    private Bundle previousFragBundle;
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout relativeLayout;


    public CarDescription() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_car_description, container, false);
        previousFragBundle= this.getArguments();
        adverttitle= view.findViewById(R.id.adverttitle);
        shimmerFrameLayout =view.findViewById(R.id.shimmer);
        relativeLayout =view.findViewById(R.id.relativeLayout);
        relativeLayout.setVisibility(View.INVISIBLE);
        owner= view.findViewById(R.id.owner);
        brand= view.findViewById(R.id.brand);
        model= view.findViewById(R.id.model);
        yearmode= view.findViewById(R.id.yearmode);
        odometer= view.findViewById(R.id.odometer);
        fuel= view.findViewById(R.id.fuel);
        type= view.findViewById(R.id.type);
        gearbox= view.findViewById(R.id.gearbox);
        color= view.findViewById(R.id.color);
        startservice= view.findViewById(R.id.startservice);
        fiscalPower= view.findViewById(R.id.fiscalPower);
        dinPower= view.findViewById(R.id.dinPower);
        date= view.findViewById(R.id.date);
        description= view.findViewById(R.id.description);
        mainImage= view.findViewById(R.id.mainImage);
        favorite= view.findViewById(R.id.favoritebutton);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibe = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
                try {
                    if(!SaveSharedPreference.isLogedIn(getContext())){

                        new AlertDialog.Builder(getContext())
                                .setTitle("bloqué")
                                .setMessage("you need to login to use this function")
                                .setPositiveButton(getString(R.string.se_connecter), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                // .setIcon(android.R.drawable.)
                                .show();
                        vibe.vibrate(80);
                        return;
                    }
                    if (!favorite.isSelected()){
                        vibe.vibrate(80);
                        favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_24_red));
                        favorite.setSelected(true);
                        try {
                            Favorites favorites = new Favorites(String.valueOf(previousFragBundle.getString("idadvert")),SaveSharedPreference.getSessionId(getContext()));
                            new CarDescription.addFavorites().doInBackground(favorites);
                        } catch (GeneralSecurityException | IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_border_52));
                        vibe.vibrate(80);
                        favorite.setSelected(false);
                        Favorites favorites = null;
                        try {
                           favorites = new Favorites(String.valueOf(previousFragBundle.getString("idadvert")), SaveSharedPreference.getSessionId(getContext()));
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new CarDescription.deleteFavorite().doInBackground(favorites);
                    }
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

         new LongRunninTask().execute();

        // Inflate the layout for this fragment
        return view;
    }

    private class addFavorites extends AsyncTask<Favorites,Void,Void> {

        @Override
        protected Void doInBackground(Favorites... favorites) {
            addFavorite(favorites[0]);
            return null;

        }
    }

    private class deleteFavorite extends AsyncTask<Favorites,Void,Void> {

        @Override
        protected Void doInBackground(Favorites... favorites) {
            deleteFavorite(favorites[0]);
            return null;

        }
    }

    private void addFavorite(Favorites favorites){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        FavoritesInterface login = retrofit.create(FavoritesInterface.class);
        Call<Object> call = login.addFavorites(favorites);
        ProgressDialog progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage(getString(R.string.please_wait));
        progressDoalog.setTitle(getString(R.string.connection___));
        progressDoalog.show();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.code() == 200) {
                    progressDoalog.dismiss();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                    progressDoalog.dismiss();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
            }
        });

    }

    private void deleteFavorite(Favorites favorites){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        FavoritesInterface login = retrofit.create(FavoritesInterface.class);
        Call<Object> call = login.deleteFavorites(favorites);
        ProgressDialog progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage(getString(R.string.please_wait));
        progressDoalog.setTitle(getString(R.string.connection___));
        progressDoalog.show();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.code() == 200) {
                    progressDoalog.dismiss();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                    progressDoalog.dismiss();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
            }
        });

    }


    @Override
    public void setExitTransition(@Nullable Object transition) {
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        super.setExitTransition(inflater.inflateTransition(R.transition.slide_left));
    }


    private class LongRunninTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                getCarDescription(previousFragBundle.getString("idadvert"),previousFragBundle.getString("longitude"),previousFragBundle.getString("latitude"));
            } catch (JSONException | GeneralSecurityException | IOException e) {
                e.printStackTrace();
            };
            return null;
        }
    }




    private void getCarDescription(String keyword,String longitude,String latitude) throws JSONException, GeneralSecurityException, IOException {
        JSONObject paramObject = new JSONObject();
        paramObject.put("idadvert", keyword);
        paramObject.put("longitude", longitude);
        paramObject.put("latitude", latitude);
        paramObject.put("userid", SaveSharedPreference.getSessionId(getContext()));
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AdvertInterface car = retrofit.create(AdvertInterface.class);
        Call<Object> call = car.getSingleCarDescription(paramObject.toString());
        shimmerFrameLayout.startShimmer();
        call.enqueue(new Callback<Object>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                try {
                    if (response.code() == 200) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        assert response.body() != null;
                        JSONObject jsoncar;
                        JSONObject jsoncar3;
                        JSONObject jsoncar2;
                        JSONArray jsonArray;
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        jsoncar=jsonObject.getJSONObject("data");
                        jsoncar3= jsoncar.getJSONObject("car");
                        boolean isfavorite = Boolean.parseBoolean(jsoncar.getString("isfavorite"));

                        jsoncar2= new JSONObject(jsoncar.getJSONObject("advert").toString());
                        jsonArray= (JSONArray) jsoncar.getJSONArray("photos");
                        byte[] backToBytes = Base64.getDecoder().decode(jsonArray.get(0).toString());
                        Bitmap bitmap = BitmapFactory.decodeByteArray(backToBytes, 0, backToBytes.length);

                        adverttitle.setText(jsoncar2.getString("title"));
                        owner.setText(jsoncar2.getString("title"));
                        date.setText(jsoncar2.getString("publishdate"));
                        brand.setText(brand.getText()+" : " +jsoncar3.getString("brand"));
                        model.setText(model.getText()+" : "+jsoncar3.getString("model"));
                        yearmode.setText(yearmode.getText()+" : "+jsoncar3.getString("productionyear"));
                        odometer.setText(odometer.getText()+" : "+jsoncar3.getString("odometer"));
                        fuel.setText(fuel.getText()+" : "+jsoncar3.getString("fuel"));
                        gearbox.setText(gearbox.getText()+" : "+jsoncar3.getString("gearbox"));
                        type.setText(type.getText()+" : "+jsoncar3.getString("type"));
                        color.setText(color.getText()+" : "+jsoncar3.getString("color"));
                        startservice.setText(startservice.getText()+" : "+jsoncar3.getString("startservice"));
                        fiscalPower.setText(fiscalPower.getText()+" : "+jsoncar3.getString("fiscalpower"));
                        dinPower.setText(dinPower.getText()+" : "+jsoncar3.getString("dinpower"));
                        description.setText(jsoncar2.getString("description"));
                        mainImage.setImageBitmap(bitmap);
                        if (isfavorite){
                            favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_24_red));
                            favorite.setSelected(true);
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

