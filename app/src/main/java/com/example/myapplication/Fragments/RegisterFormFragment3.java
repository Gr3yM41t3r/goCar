package com.example.myapplication.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.model.Organisation;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.retrofit.RegisterInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterFormFragment3 extends Fragment {



    private Button confirmRegister;
    private Button previous_form;
    private Bundle bundle;
    private EditText orgranisation;
    private EditText adresse;
    private EditText zipCode;
    private TextView city;

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
        previous_form= view.findViewById(R.id.back_form);
        confirmRegister= view.findViewById(R.id.confirm_register);
        orgranisation= view.findViewById(R.id.organisationInput);
        adresse= view.findViewById(R.id.adresseInput);
        zipCode= view.findViewById(R.id.zipCodeInput);
        city= view.findViewById(R.id.cityInput);

        previous_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        confirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String organisationValue =orgranisation.getText().toString();
                String adresseValue =adresse.getText().toString();
                String zipcodeValue =zipCode.getText().toString();
                String cityValue =city.getText().toString();
                if(TextUtils.isEmpty(organisationValue) || TextUtils.isEmpty(adresseValue) || TextUtils.isEmpty(zipcodeValue)|| TextUtils.isEmpty(cityValue)) {
                    if(TextUtils.isEmpty(organisationValue)){
                        orgranisation.setError("requis");
                    }
                    if(TextUtils.isEmpty(organisationValue)){
                        adresse.setError("requis");
                    }
                    if(TextUtils.isEmpty(organisationValue)){
                        zipCode.setError("requis");
                    }
                    if(TextUtils.isEmpty(organisationValue)){
                        city.setError("requis");
                    }
                }else {
                    Organisation organisation = new Organisation(bundle.get("firstname").toString(),
                            bundle.get("lastname").toString(),
                            bundle.get("birthday").toString(),
                            bundle.get("phoneNumber").toString(),
                            bundle.get("accountType").toString(),
                            bundle.get("email").toString(),
                            bundle.get("password").toString(),
                            organisationValue,
                            adresseValue,
                            zipcodeValue,
                            cityValue);
                    register(organisation);
                }
            }
        });
        zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String zipcodeValue =zipCode.getText().toString();
                if (zipcodeValue.length()==5){
                    try {
                        getCityName(zipcodeValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return  view;
    }
    public void back(){
        ((RegisterActivity) this.getActivity()).back();
    }

    public void goToLogin(){
        ((RegisterActivity) this.getActivity()).finish();
    }


    private void register(Organisation organisation) {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RegisterInterface register = retrofit.create(RegisterInterface.class);
        Call<Object> call = register.registerProfessionel(organisation);
        ProgressDialog progressDoalog = new ProgressDialog(this.getActivity());
        progressDoalog.setMessage(getString(R.string.please_wait));
        progressDoalog.setTitle(getString(R.string.connection___));
        progressDoalog.show();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
                if (response.code() == 200) {
                    progressDoalog.dismiss();
                    goToLogin();
                } else {
                    progressDoalog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
            }
        });
    }

    private void getCityName(String emailtxt) throws JSONException {
        JSONObject paramObject = new JSONObject();
        paramObject.put("zipcode", emailtxt);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RegisterInterface register = retrofit.create(RegisterInterface.class);
        Call<Object> call = register.getCity(paramObject.toString());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
                try {
                if (response.code() == 200) {
                    assert response.body() != null;
                    String s = response.body().toString();
                    JSONObject jsonObject = new JSONObject(s);
                    city.setText(jsonObject.getString("data").replace("_"," "));
                }  else {
                    Toast.makeText(getContext(), getString(R.string.city_not_found), Toast.LENGTH_LONG).show();
                }
                } catch (JSONException  e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.error_getting_city), Toast.LENGTH_LONG).show();
            }
        });
    }
}