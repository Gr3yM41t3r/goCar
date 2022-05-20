package com.example.myapplication.Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Utility.SaveSharedPreference;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.AdvertInterface;
import com.example.myapplication.retrofit.RegisterInterface;
import com.github.drjacky.imagepicker.ImagePicker;
import com.yalantis.ucrop.view.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddAdvertFragment extends Fragment {
    private CardView addphotobutton;
    LayoutInflater inflater2 ;
    LinearLayout horizontal;
    private EditText title;
    private EditText brand;
    private EditText model;
    private EditText odometer;
    private Spinner fuel;
    private EditText color;
    private TextView startservice;
    private EditText yearmodel;
    private EditText puifis;
    private EditText puidin;
    private EditText price;
    private EditText zipCode;
    private EditText description;
    private Spinner gearbox;
    private Spinner vehiculetype;
    private Spinner advertype;
    private Button confirm;
    private DatePickerDialog datePickerDialog;

    private ArrayList<EditText> allInputs = new ArrayList<>();
    ActivityResultLauncher<Intent> activityResultLauncher;
    public AddAdvertFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_advert, container, false);
        addphotobutton=view.findViewById(R.id.addPhotoButton);
        inflater2 = getLayoutInflater();
        horizontal =view.findViewById(R.id.horizontal);
        title =view.findViewById(R.id.title);
        brand =view.findViewById(R.id.brand);
        model =view.findViewById(R.id.model);
        odometer =view.findViewById(R.id.odometer);
        fuel =view.findViewById(R.id.fuel);
        zipCode =view.findViewById(R.id.codePostal);
        color =view.findViewById(R.id.color);
        startservice =view.findViewById(R.id.startservice);
        yearmodel =view.findViewById(R.id.yearmode);
        puifis =view.findViewById(R.id.fiscalPower);
        puidin =view.findViewById(R.id.dinPower);
        price =view.findViewById(R.id.price);
        description =view.findViewById(R.id.description);
        gearbox =view.findViewById(R.id.gearbox);
        vehiculetype =view.findViewById(R.id.vehiculetype);
        advertype =view.findViewById(R.id.adverttype);
        confirm =view.findViewById(R.id.confirm_button);
        initDatePicker();
        startservice.setText("30-12-2000");
        startservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(view);
            }
        });

        allInputs.add(title);
        allInputs.add(brand);
        allInputs.add(model);
        allInputs.add(odometer);
        allInputs.add(color);
        allInputs.add(yearmodel);
        allInputs.add(puifis);
        allInputs.add(zipCode);
        allInputs.add(puidin);
        allInputs.add(price);
        allInputs.add(description);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createAnnonce();
                } catch (JSONException | GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
            }
        });





        ActivityResultLauncher<Intent> launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri uri = result.getData().getData();
                        View cardview= inflater2.inflate(R.layout.photo_cardview,horizontal,false);
                        CropImageView main = cardview.findViewById(R.id.cropImageView);
                        ImageView remove = cardview.findViewById(R.id.removephoto);
                        main.setImageURI(uri);

                        remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                horizontal.removeView(cardview);
                            }
                        });
                        horizontal.addView(cardview);
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                    }
                });

        addphotobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] colors = {"Camera", "Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("choisir la source");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            Intent intent= ImagePicker.with(requireActivity())
                                    .cameraOnly()		//User can only capture image using Camera
                                    .createIntent();
                            launcher.launch(intent);
                        }else if(which==1){
                            Intent intent= ImagePicker.with(requireActivity())
                                    .galleryOnly()		//User can only capture image using Camera
                                    .createIntent();
                            launcher.launch(intent);
                        }
                    }
                });
                builder.show();

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
                        zipCode.setText(jsonObject.getString("data").replace("_"," "));
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

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = day + "-" + month + "-" + year;
                startservice.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
    public Boolean isEverythinchecked(){
        boolean allchecked= true;
        for (int i = 0; i < allInputs.size(); i++) {
            if (TextUtils.isEmpty(allInputs.get(i).getText())){
                allInputs.get(i).setError("required");
                allchecked=false;
            }
        }
        if (vehiculetype.getSelectedItem().toString().equals("type de voiture")){
            ( (TextView)vehiculetype.getSelectedView()).setError("required");
            allchecked=false;
        }
        if (advertype.getSelectedItem().toString().equals("type d'annonce")){
            ( (TextView)advertype.getSelectedView()).setError("required");
            allchecked=false;
        }
        if (gearbox.getSelectedItem().toString().equals("type d'annonce")){
            ( (TextView)gearbox.getSelectedView()).setError("required");
            allchecked=false;
        }
        if (fuel.getSelectedItem().toString().equals("type d'annonce")){
            ( (TextView)fuel.getSelectedView()).setError("required");
            allchecked=false;
        }

        return allchecked;

    }
    public StringBuilder getEncodedPicutes() {
        StringBuilder encodedPictures = new StringBuilder();
        for (int i = 0; i < horizontal.getChildCount(); i++) {
            Log.e("jdkljqsldkj", String.valueOf(horizontal.getChildCount()));
            View cardview = horizontal.getChildAt(i);
            CropImageView imageView = cardview.findViewById(R.id.cropImageView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedPictures.append(Base64.getEncoder().encodeToString(byteArray)+";");

            }
        }
        return encodedPictures;
    }

    public void createAnnonce() throws JSONException, GeneralSecurityException, IOException {
        if(isEverythinchecked()) {

            JSONObject paramObject = new JSONObject();
            paramObject.put("idUser", SaveSharedPreference.getSessionId(getContext()));
            paramObject.put("title", title.getText().toString());
            paramObject.put("brand", brand.getText().toString());
            paramObject.put("model", model.getText().toString());
            paramObject.put("odometer", odometer.getText().toString());
            paramObject.put("fuel", fuel.getSelectedItem().toString());
            paramObject.put("startservice", startservice.getText().toString());
            paramObject.put("color", color.getText().toString());
            paramObject.put("gearbox", gearbox.getSelectedItem().toString());
            paramObject.put("yearmodel", yearmodel.getText().toString());
            paramObject.put("puifis", puifis.getText().toString());
            paramObject.put("puidin", puidin.getText().toString());
            paramObject.put("price", price.getText().toString());
            paramObject.put("city", zipCode.getText().toString());
            paramObject.put("description", description.getText().toString());
            paramObject.put("vehiculetype", vehiculetype.getSelectedItem().toString());
            paramObject.put("advertype", advertype.getSelectedItem().toString());
            paramObject.put("photos", getEncodedPicutes());
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(Constants.URL + "api/goCar/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            AdvertInterface advertInterface = retrofit.create(AdvertInterface.class);
            Call<Object> call = advertInterface.addAdvert(paramObject.toString());
            ProgressDialog progressDoalog = new ProgressDialog(this.getActivity());
            progressDoalog.setMessage(getString(R.string.please_wait));
            progressDoalog.setTitle(getString(R.string.connection___));
            progressDoalog.show();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
                    if (response.code() == 200) {
                        progressDoalog.dismiss();

                    } else if (response.code() == 400) {
                        progressDoalog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                    } else {
                        progressDoalog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Toast.makeText(getContext(),  getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                }
            });

        }else {
            return;
        }

    }




}