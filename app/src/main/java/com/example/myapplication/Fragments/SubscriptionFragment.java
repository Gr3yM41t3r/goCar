package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Utility.SaveSharedPreference;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.SearchInterface;
import com.example.myapplication.retrofit.SubscriptionInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SubscriptionFragment extends Fragment {

    private CardView basic;
    private CardView essentiel;
    private CardView premium;


    public SubscriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_subscription, container, false);
        basic = view.findViewById(R.id.basic);
        essentiel = view.findViewById(R.id.Essentiel);
        premium = view.findViewById(R.id.Premium);


        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateSubscription("0");
                }  catch (JSONException | IOException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        essentiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateSubscription("1");
                }  catch (JSONException | IOException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateSubscription("2");
                } catch (JSONException | IOException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void updateSubscription(String type) throws JSONException, GeneralSecurityException, IOException {

        JSONObject paramObject = new JSONObject();
        paramObject.put("session", SaveSharedPreference.getSessionId(requireContext()));
        paramObject.put("type", type);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        SubscriptionInterface subscriptionInterface = retrofit.create(SubscriptionInterface.class);
        Call<Object> call = subscriptionInterface.updateSubscription(paramObject.toString());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
                Log.e("jhlkjl",String.valueOf(response.code()));
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "abonnement changé avec succé", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(),  getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
            }
        });
    }
}