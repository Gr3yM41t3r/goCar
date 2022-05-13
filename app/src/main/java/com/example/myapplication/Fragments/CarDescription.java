package com.example.myapplication.Fragments;

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
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.AdvertInterface;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView numberofdoors;
    private TextView fiscalPower;
    private TextView dinPower;
    private TextView description;
    private ImageView mainImage;
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
        numberofdoors= view.findViewById(R.id.numberofdoors);
        fiscalPower= view.findViewById(R.id.fiscalPower);
        dinPower= view.findViewById(R.id.dinPower);
        description= view.findViewById(R.id.description);
        mainImage= view.findViewById(R.id.mainImage);

         new LongRunninTask().execute();

        // Inflate the layout for this fragment
        return view;
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
                getCarDescription(previousFragBundle.getString("idadvert"));
            } catch (JSONException e) {
                e.printStackTrace();
            };
            return null;
        }
    }




    private void getCarDescription(String keyword) throws JSONException {
        JSONObject paramObject = new JSONObject();
        paramObject.put("idadvert", keyword);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AdvertInterface car = retrofit.create(AdvertInterface.class);
        Call<Object> call = car.getSingleCarDescription(paramObject.toString());
        shimmerFrameLayout.startShimmer();
        call.enqueue(new Callback<Object>() {
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
                        jsoncar2= new JSONObject(jsoncar.getJSONObject("advert").toString());
                        jsonArray= (JSONArray) jsoncar.getJSONArray("photos");
                        byte[] backToBytes = Base64.getDecoder().decode(jsonArray.get(0).toString());
                        Bitmap bitmap = BitmapFactory.decodeByteArray(backToBytes, 0, backToBytes.length);
                        adverttitle.setText(jsoncar2.getString("title"));
                        owner.setText(jsoncar2.getString("title"));
                        brand.setText(jsoncar3.getString("brand"));
                        model.setText(jsoncar3.getString("model"));
                        yearmode.setText(jsoncar3.getString("productionyear"));
                        odometer.setText(jsoncar3.getString("odometer"));
                        fuel.setText(jsoncar3.getString("fuel"));
                        gearbox.setText(jsoncar3.getString("gearbox"));
                        type.setText(jsoncar3.getString("type"));
                        color.setText(jsoncar3.getString("color"));
                        numberofdoors.setText(jsoncar2.getString("title"));
                        fiscalPower.setText(jsoncar3.getString("fiscalpower"));
                        dinPower.setText(jsoncar3.getString("dinpower"));
                        description.setText(jsoncar2.getString("description"));
                        mainImage.setImageBitmap(bitmap);

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

