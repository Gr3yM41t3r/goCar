package com.example.myapplication.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.myapplication.DashBoardActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utility.SaveSharedPreference;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.LoginInterface;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserProfileFragment extends Fragment {

    private Button changeSubscription;
    private TextView fname;
    private TextView lname;
    private TextView birthday;
    private TextView phonenumber;
    private TextView accounttype;
    private TextView creationdate;
    private TextView entreprisename;
    private TextView adresse;
    private TextView city;
    private TextView zipcode;
    private TextView expiredate;
    private TextView photosnumber;
    private TextView subscribtiontype;
    private LinearLayout professionalview;


    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        TextView email = view.findViewById(R.id.email);
        changeSubscription = view.findViewById(R.id.changesubscription);
        fname = view.findViewById(R.id.fname);
        lname = view.findViewById(R.id.lname);
        birthday = view.findViewById(R.id.birthDay);
        phonenumber = view.findViewById(R.id.phonenumber);
        accounttype = view.findViewById(R.id.accounttype);
        creationdate = view.findViewById(R.id.creationdate);
        professionalview = view.findViewById(R.id.professionalview);
        entreprisename = view.findViewById(R.id.entreprisename);
        adresse = view.findViewById(R.id.adresse);
        city = view.findViewById(R.id.city);
        zipcode = view.findViewById(R.id.zipcode);
        subscribtiontype = view.findViewById(R.id.subscribtiontype);
        expiredate = view.findViewById(R.id.expiredate);
        photosnumber = view.findViewById(R.id.imagenumber);
        professionalview.setVisibility(View.GONE);
        try {
            getPersonnalInfo();
        } catch (JSONException | IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        try {
            email.setText(SaveSharedPreference.getEmail(getContext()));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        changeSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((DashBoardActivity) requireActivity()).setFragment(new SubscriptionFragment());
            }
        });
        return view;
    }

    public String getaccountType(String accounttype) {
        if (accounttype.equals("0")) {
            return "Particulier";
        }
        return "Professionel";
    }

    public String getAuthorizedPictures(String typeAbo) {
        switch (typeAbo) {
            case "0":
                return "2";
            case "1":
                return "4";
            case "2":
                return "6";
        }
        return "";
    }

    public String getSubName(String typeAbo) {
        switch (typeAbo) {
            case "0":
                return "Basic";
            case "1":
                return "Essentiel";
            case "2":
                return "Premium";
        }
        return "NAN";
    }

    private void getPersonnalInfo() throws JSONException, GeneralSecurityException, IOException {
        JSONObject paramObject = new JSONObject();
        paramObject.put("sessionid", SaveSharedPreference.getSessionId(getContext()));
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/user/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        LoginInterface loginInterface = retrofit.create(LoginInterface.class);
        Call<Object> call = loginInterface.getUserData(paramObject.toString());
        call.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        JSONObject jsoncar;
                        JSONObject jsonObject;

                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        jsoncar = jsonObject.getJSONObject("data");
                        fname.setText(jsoncar.getString("first_name"));
                        lname.setText(jsoncar.getString("last_name"));
                        birthday.setText(jsoncar.getString("birth_day"));
                        phonenumber.setText(jsoncar.getString("phone_number"));
                        accounttype.setText(getaccountType(jsoncar.getString("account_type")));
                        creationdate.setText(jsoncar.getString("creation_date"));
                        if (jsoncar.getString("account_type").equals("1")) {
                            professionalview.setVisibility(View.VISIBLE);
                            entreprisename.setText(jsoncar.getString("entreprisename"));
                            zipcode.setText(jsoncar.getString("zipcode"));
                            city.setText(jsoncar.getString("city"));
                            adresse.setText(jsoncar.getString("adresse"));
                            subscribtiontype.setText(getSubName(jsoncar.getString("subtype")));
                            expiredate.setText(jsoncar.getString("expiredate"));
                            photosnumber.setText(getAuthorizedPictures(jsoncar.getString("subtype")));
                        }

                        Log.e("kkkkkkkkkkkkkkkkk", jsoncar.getString("account_type"));
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
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