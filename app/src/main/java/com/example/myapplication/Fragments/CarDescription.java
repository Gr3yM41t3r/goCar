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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.AdvertInterface;
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


    TextView carmodel;
    private  Handler handler = new Handler();
    Bundle previousFragBundle;

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
        carmodel= view.findViewById(R.id.carfullmodel);
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
     //   shimmerFrameLayout.startShimmer();
        call.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                try {
                    if (response.code() == 200) {
                      //  shimmerFrameLayout.stopShimmer();
                        //shimmerFrameLayout.setVisibility(View.GONE);
                        assert response.body() != null;
                        JSONObject jsoncar;
                        JSONObject jsoncar2;
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        jsoncar=jsonObject.getJSONObject("data");
                        jsoncar.getJSONObject("car");
                        jsoncar2= new JSONObject(jsoncar.getJSONObject("car").toString());
                       carmodel.setText(jsoncar2.getString("brand")+" "+jsoncar2.getString("model"));

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

