package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.model.Organisation;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.retrofit.RegisterInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterFormFragment3 extends Fragment {

    Bundle bundle;
    Button confirmRegister;



    public RegisterFormFragment3() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
        setExitTransition(inflater.inflateTransition(R.transition.fade));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_form3, container, false);
        bundle = this.getArguments();
        Button previous_form= view.findViewById(R.id.back_form);
        confirmRegister = view.findViewById(R.id.confirm_register);
        previous_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        confirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Organisation organisation = new Organisation(
                        bundle.getString("first_name"),
                        bundle.getString("last_name"),
                        bundle.getString("birth_day"),
                        bundle.getString("phone_number"),
                        bundle.getString("account_type"),
                        bundle.getString("email"),
                        bundle.getString("password"),)
                );
                sendNetworkRequest(organisation);

            }
        });

        return  view;
    }
    public void back(){
        ((RegisterActivity) this.getActivity()).back();
    }

    private void sendNetworkRequest(Organisation organisation) {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
        Log.e("TAG", (gson.toJson(organisation)));
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RegisterInterface register = retrofit.create(RegisterInterface.class);
        Call<Object> call = register.registerPro(organisation);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "nice", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "an Erroe has occuured", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "Service Indisponible", Toast.LENGTH_LONG).show();
            }
        });
    }
}