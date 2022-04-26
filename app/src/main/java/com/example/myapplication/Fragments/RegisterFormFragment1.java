package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.RegisterInterface;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterFormFragment1 extends Fragment {

    Fragment next_form_frame;
    String emailValue;
    String confirmPasswordValue;
    String passwordValue;
    EditText email;

    public RegisterFormFragment1() {
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
        View view = inflater.inflate(R.layout.fragment_register_form1, container, false);
        next_form_frame = new RegisterFormFragment2();
        Button next_form_button = view.findViewById(R.id.next_form);
        email = view.findViewById(R.id.emailInput);
        EditText password = view.findViewById(R.id.passwordInput);
        EditText confirmPassword = view.findViewById(R.id.confirmPasswordInput);
        next_form_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordValue = password.getText().toString();
                confirmPasswordValue = confirmPassword.getText().toString();
                emailValue = email.getText().toString();
                if (TextUtils.isEmpty(emailValue) || TextUtils.isEmpty(passwordValue) || TextUtils.isEmpty(confirmPasswordValue)) {
                    email.setError("requis");
                    password.setError("requis");
                    confirmPassword.setError("requis");

                } else {
                    if (isEmailValid(emailValue)) {
                        if (passwordValue.length()>=8) {

                            if (passwordValue.equals(confirmPasswordValue)) {
                                try {
                                    sendNetworkRequest(emailValue);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                confirmPassword.setError("les mots de passe ne se ressemblent pas");
                            }

                        }else {
                            password.setError("password should be at least 8 caracters");
                        }
                    } else {
                        email.setError("email format not correct");
                    }
                }
            }
        });
        return view;
    }

    public void nextFragment(Fragment fragment) {
        ((RegisterActivity) this.requireActivity()).setFragment(fragment);
    }

    public void sendBundle(Fragment fragment, String email, String password) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("password", password);
        fragment.setArguments(bundle);
    }


    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void sendNetworkRequest(String emailtxt) throws JSONException {

        JSONObject paramObject = new JSONObject();
        paramObject.put("email", emailtxt);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RegisterInterface register = retrofit.create(RegisterInterface.class);
        Call<Object> call = register.process(paramObject.toString());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
                if (response.code() == 200) {
                    nextFragment(next_form_frame);
                    sendBundle(next_form_frame, emailValue, passwordValue);
                } else if (response.code() == 400) {
                    Toast.makeText(getContext(), "an error has occured", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "email Already Exists", Toast.LENGTH_LONG).show();
                    email.setError("email Existant");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "an error has occured ", Toast.LENGTH_LONG).show();
            }
        });
    }

}