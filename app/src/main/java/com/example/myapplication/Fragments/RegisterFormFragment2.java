package com.example.myapplication.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.retrofit.RegisterInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterFormFragment2 extends Fragment {

    TextView birthDayPicker;
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    Button previous_form;
    Button confirmRegister;
    CheckBox isPofessionel;
    Bundle bundle;
    private DatePickerDialog datePickerDialog;

    public RegisterFormFragment2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
        setExitTransition(inflater.inflateTransition(R.transition.fade));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_form2, container, false);
        bundle = this.getArguments();
        initDatePicker();
        previous_form = view.findViewById(R.id.back_form);
        birthDayPicker = view.findViewById(R.id.birthDayInput);
        firstName = view.findViewById(R.id.firstnameInput);
        lastName = view.findViewById(R.id.lastnameInput);
        phoneNumber = view.findViewById(R.id.phoneInput);
        isPofessionel = view.findViewById(R.id.userStatusCheckbox);

        confirmRegister = view.findViewById(R.id.confirm_register);
        confirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPofessionel.isChecked()) {
                    nextFragment(new RegisterFormFragment3());

                } else {
                    UserModel userModel = new UserModel(firstName.getText().toString(),
                            lastName.getText().toString(),
                            birthDayPicker.getText().toString(),
                            phoneNumber.getText().toString(),
                            "0",
                            bundle.getString("email"),
                            bundle.getString("password")
                    );
                    sendNetworkRequest(userModel);
                }

            }
        });

        birthDayPicker.setText("30/12/2000");
        birthDayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDatePicker(view);
            }
        });
        previous_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        return view;
    }

    public void nextFragment(Fragment fragment) {
        ((RegisterActivity) this.requireActivity()).setFragment(fragment);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = day + "/" + month + "/" + year;
                birthDayPicker.setText(date);
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

    public void back() {
        ((RegisterActivity) this.getActivity()).back();
    }

    private void sendNetworkRequest(UserModel user) {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
        Log.e("TAG", (gson.toJson(user)));
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RegisterInterface register = retrofit.create(RegisterInterface.class);
        Call<Object> call = register.register(user);
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