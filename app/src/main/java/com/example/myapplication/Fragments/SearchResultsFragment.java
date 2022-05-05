package com.example.myapplication.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ImageResizer;
import com.example.myapplication.R;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.CarInterface;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchResultsFragment extends Fragment {

    ShimmerFrameLayout shimmerFrameLayout;
    GridLayout gridLayout;
    LayoutInflater inflater2;
    public SearchResultsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        gridLayout= view.findViewById(R.id.mainGrid);
        shimmerFrameLayout =view.findViewById(R.id.shimmer);
        inflater2 = getLayoutInflater();
        getCars();


        return view;
    }

    public void fillCarList(String tp, String mdl, String odo, String fl, String prdyr, Bitmap bm){
        View cardview= inflater2.inflate(R.layout.small_cardview_car,gridLayout,false);
        TextView type = cardview.findViewById(R.id.title);
        TextView model = cardview.findViewById(R.id.model);
        TextView odometer = cardview.findViewById(R.id.odometer);
        TextView fuel = cardview.findViewById(R.id.fuel);
        TextView productionyear = cardview.findViewById(R.id.productionyear);
        ImageView mainImage = cardview.findViewById(R.id.mainImage);
        type.setText(tp);
        model.setText(mdl);
        odometer.setText(odo);
        fuel.setText(fl);
        productionyear.setText(prdyr);
        mainImage.setImageBitmap(bm);
        gridLayout.addView(cardview);
    }

    private void getCars() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        CarInterface car = retrofit.create(CarInterface.class);
        Call<Object> call = car.getCars();
        shimmerFrameLayout.startShimmer();
        call.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                try {
                    if (response.code() == 200) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        assert response.body() != null;
                        JSONObject jsobj;
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONArray cars = new JSONArray(jsonObject.getString("data"));
                        for (int i = 0; i < cars.length(); i++) {
                            jsobj=new JSONObject(cars.getJSONObject(i).getString("car"));
                            byte[] backToBytes = Base64.getDecoder().decode(cars.getJSONObject(i).getString("photo"));
                            Bitmap bitmap = BitmapFactory.decodeByteArray(backToBytes, 0, backToBytes.length);
                            Bitmap reduce = ImageResizer.reduceBitmapSize(bitmap,146000);


                            fillCarList(jsobj.getString("type"),
                                    jsobj.getString("brand")+" "+jsobj.getString("model"),
                                    jsobj.getString("odometer"),
                                    jsobj.getString("fuel"),
                                    jsobj.getString("productionyear"),
                                    reduce
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