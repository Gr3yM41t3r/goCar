package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utility.SaveSharedPreference;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.AdvertInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyAdvertDetails extends Fragment {

    private CardView deleteButton;
    private CardView boost;
    private ImageView mainImage;
    private TextView title;
    private TextView numberOfViews;
    private TableLayout simpleTableLayout;
    private TextView odometer;
    private Bundle previousFragBundle;
    private ImageView boostImage;
    private Boolean ispro =false;
    private Boolean isboosted =false;
    private int idadvert;
    public MyAdvertDetails() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_advert_details, container, false);
        previousFragBundle= this.getArguments();

        deleteButton = view.findViewById(R.id.deleteadvert);
        mainImage = view.findViewById(R.id.mainImage);
        title = view.findViewById(R.id.title);
        numberOfViews = view.findViewById(R.id.numberOfViews);
        odometer = view.findViewById(R.id.odometer);
        boost = view.findViewById(R.id.boost);
        boostImage = view.findViewById(R.id.boostImage);
        simpleTableLayout = view.findViewById(R.id.simpleTableLayout);
        try {
            getAdvert();
        } catch (GeneralSecurityException | IOException | JSONException e) {
            e.printStackTrace();
        }
        Vibrator vibe = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                vibe.vibrate(80);


            }
        });
        boost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);

                if (ispro){
                    if (isboosted){
                        try {
                            boostAdvert("0",String.valueOf(idadvert));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isboosted=false;
                        boostImage.setImageResource(R.drawable.lightning_bolt);

                    }else {
                        try {
                            boostAdvert("1",String.valueOf(idadvert));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isboosted=true;
                        boostImage.setImageResource(R.drawable.lightning_bolt_yellow);

                    }
                }else{
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
                }

            }
        });
        return view;
    }


    public void getAdvert()throws GeneralSecurityException, IOException, JSONException{

            JSONObject paramObject = new JSONObject();
            paramObject.put("userid", SaveSharedPreference.getSessionId(getContext()));
            paramObject.put("idadvert", previousFragBundle.getString("idadvert"));
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(Constants.URL + "api/goCar/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            AdvertInterface car = retrofit.create(AdvertInterface.class);
            Call<Object> call = car.getuserAdvert(paramObject.toString());
            call.enqueue(new Callback<Object>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    try {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            JSONObject jsobj;
                            JSONObject jsonAdvert;
                            JSONArray jsonArray;
                            JSONObject jsonArray2;
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            JSONArray cars = new JSONArray(jsonObject.getString("data"));
                            Log.e("fsdfsdfsdfs",cars.toString());
                            for (int i = 0; i < cars.length(); i++) {
                                jsobj=new JSONObject(cars.getJSONObject(i).getString("car"));
                                ispro = cars.getJSONObject(i).getBoolean("ispro");
                                jsonAdvert=new JSONObject(cars.getJSONObject(i).getString("advert"));
                                jsonArray= (JSONArray) cars.getJSONObject(i).get("photos");;
                                byte[] backToBytes = Base64.getDecoder().decode(jsonArray.get(0).toString());
                                Bitmap bitmap = BitmapFactory.decodeByteArray(backToBytes, 0, backToBytes.length);
                                mainImage.setImageBitmap(bitmap);
                                idadvert=jsonAdvert.getInt("id");
                                title.setText(jsonAdvert.getString("title"));
                                isboosted = jsonAdvert.getInt("boost")==1;
                                if (jsonAdvert.getInt("boost")==1){
                                    isboosted =true;
                                    Log.e("mkjlmlkddddjm", String.valueOf(jsonAdvert.getInt("boost")));
                                    Log.e("mkjlmlkddddjm", String.valueOf(jsonAdvert.getInt("boost")==1));
                                    boostImage.setImageResource(R.drawable.lightning_bolt_yellow);
                                }
                                jsonArray2= (JSONObject) cars.getJSONObject(i).get("views");
                                odometer.setText(jsobj.getString("brand")+" "+jsobj.getString("model"));
                                Log.e("mlkmlk",String.valueOf(jsonArray2.length()));
                                Iterator<String> keys= jsonArray2.keys();
                                int counter =0;
                                while (keys.hasNext()){
                                    String keyValue = (String)keys.next();
                                    Log.e("mlkmlk",keyValue);
                                    counter+=jsonArray2.getInt(keyValue);
                                    View row= getLayoutInflater().inflate(R.layout.table_row_views,simpleTableLayout,false);
                                    TextView city = row.findViewById(R.id.city);
                                    TextView numberOfViews = row.findViewById(R.id.numberOfViews);
                                    city.setText(keyValue);
                                    numberOfViews.setText(String.valueOf(jsonArray2.getInt(keyValue)));
                                    simpleTableLayout.addView(row);
                                }
                                numberOfViews.setText(String.valueOf(counter));

                            }
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
    public void boostAdvert(String boosttype,String idadvert) throws JSONException {
        JSONObject paramObject = new JSONObject();
        paramObject.put("boosttype", boosttype);
        paramObject.put("idadvert",idadvert );
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AdvertInterface car = retrofit.create(AdvertInterface.class);
        Call<Object> call = car.boost(paramObject.toString());
        call.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void editAdvert(){

    }
    public void deleteAdvert(){

    }

}