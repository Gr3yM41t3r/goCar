package com.example.myapplication.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DashBoardActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.SaveSharedPreference;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.model.Compte;
import com.example.myapplication.retrofit.CarInterface;
import com.example.myapplication.retrofit.LoginInterface;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Objects;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    LinearLayout horizontal;
    ShimmerFrameLayout shimmerFrameLayout;
    LayoutInflater inflater2 ;
    ImageView hamburger;
    DrawerLayout sideBar;
    TextView all;
    TextView bb;
    TextView berline;
    TextView citadine;
    TextView sport;
    CardView searchbar;


    public HomePageFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_left));
        setExitTransition(inflater.inflateTransition(R.transition.slide_right));
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home_page, container, false);
        inflater2 = getLayoutInflater();
        horizontal =view.findViewById(R.id.horizontal);
        shimmerFrameLayout =view.findViewById(R.id.shimmer);
        hamburger =view.findViewById(R.id.side_bar_hamburger);
        sideBar =view.findViewById(R.id.homePage);
        all =view.findViewById(R.id.all);
        bb =view.findViewById(R.id.bb);
        berline =view.findViewById(R.id.berline);
        citadine =view.findViewById(R.id.citadine);
        sport =view.findViewById(R.id.sport);
        searchbar =view.findViewById(R.id.search_bar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),sideBar,R.string.an_error_occurred_please_login_again_later,R.string.an_error_occurred_please_login_again_later);
        sideBar.addDrawerListener(toggle);
        toggle.syncState();
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sideBar.openDrawer(GravityCompat.START);
            }
        });

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment searchFragment = new SearchFragment();
                ((DashBoardActivity) requireActivity()).setFragment(searchFragment);

            }
        });

        getCars();
        return view;
    }

    public void fillCarList(String tp, String mdl, String odo, String fl, String prdyr, Bitmap bm){
        View cardview= inflater2.inflate(R.layout.big_cardview_car,horizontal,false);
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

        horizontal.addView(cardview);
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
                            fillCarList(jsobj.getString("type"),
                                    jsobj.getString("brand")+" "+jsobj.getString("model"),
                                    jsobj.getString("odometer"),
                                    jsobj.getString("fuel"),
                                    jsobj.getString("productionyear"),
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