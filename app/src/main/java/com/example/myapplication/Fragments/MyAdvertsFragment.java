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
import android.widget.EditText;
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


public class MyAdvertsFragment extends Fragment {


    private LinearLayout myadvertcontainer;
    private TextView notfound;



    public MyAdvertsFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_my_adverts, container, false);
        myadvertcontainer= view.findViewById(R.id.myadvertcontainer);
        notfound= view.findViewById(R.id.notfound);
        try {
            getCarsByType();
        } catch (GeneralSecurityException | IOException | JSONException e) {
            e.printStackTrace();
        }
        notfound.setVisibility(View.GONE);
        return view;


    }

    public void sendBundle(Fragment fragment, String idadvert) {
        Bundle bundle = new Bundle();
        bundle.putString("idadvert", idadvert);
        fragment.setArguments(bundle);
    }

    public void fillFavoriteContainer(int id, String modelstr , String odometerstr, Bitmap image){
        View favorite= getLayoutInflater().inflate(R.layout.my_advert_card,myadvertcontainer,false);
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
                Fragment advertDeatail = new MyAdvertDetails();
                sendBundle(advertDeatail,String.valueOf(favorite.getId()));
                ((DashBoardActivity) requireActivity()).setFragment(advertDeatail);

            }
        });
        myadvertcontainer.addView(favorite);
    }


    private void getCarsByType() throws GeneralSecurityException, IOException, JSONException {
        JSONObject paramObject = new JSONObject();
        paramObject.put("userid", SaveSharedPreference.getSessionId(getContext()));
        Log.e("lùlùm",paramObject.toString());
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AdvertInterface car = retrofit.create(AdvertInterface.class);
        Call<Object> call = car.getusersAdverts(paramObject.toString());

        call.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.e("lùlùm",String.valueOf(response.code()));

                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        JSONObject jsobj;
                        JSONObject jsonAdvert;
                        JSONArray jsonArray;
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONArray cars = new JSONArray(jsonObject.getString("data"));
                        Log.e("fsdfsdfsdfs",cars.toString());
                        for (int i = 0; i < cars.length(); i++) {
                            Log.e("fsdfsdfsdfs",String.valueOf(cars.length()));
                            jsobj=new JSONObject(cars.getJSONObject(i).getString("car"));
                            jsonAdvert=new JSONObject(cars.getJSONObject(i).getString("advert"));
                            jsonArray= (JSONArray) cars.getJSONObject(i).get("photos");;
                            byte[] backToBytes = Base64.getDecoder().decode(jsonArray.get(0).toString());
                            Bitmap bitmap = BitmapFactory.decodeByteArray(backToBytes, 0, backToBytes.length);
                            fillFavoriteContainer(jsonAdvert.getInt("id"),
                                    jsonAdvert.getString("title"),
                                    jsobj.getString("brand")+" "+jsobj.getString("model"),
                                    bitmap
                            );
                        }
                    } else {
                        notfound.setVisibility(View.VISIBLE);
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