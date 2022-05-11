package com.example.myapplication.Fragments;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.Patterns;
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
                    if ((TextUtils.isEmpty(emailValue))) {
                        email.setError(getString(R.string.email_required));
                    }
                    if ((TextUtils.isEmpty(passwordValue))) {
                        password.setError(getString(R.string.password_required));
                    }
                    if ((TextUtils.isEmpty(confirmPasswordValue))) {
                        confirmPassword.setError(getString(R.string.password_required));
                    }


                } else {
                    if (isEmailValid(emailValue)) {
                        if (passwordValue.length()>=8) {

                            if (passwordValue.equals(confirmPasswordValue)) {
                                try {
                                    checkIfEmailExists(emailValue);
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
                        email.setError(getString(R.string.invalid_email));
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
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void checkIfEmailExists(String emailtxt) throws JSONException {

        JSONObject paramObject = new JSONObject();
        paramObject.put("email", emailtxt);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RegisterInterface register = retrofit.create(RegisterInterface.class);
        Call<Object> call = register.process(paramObject.toString());
        ProgressDialog progressDoalog = new ProgressDialog(this.getActivity());
        progressDoalog.setMessage(getString(R.string.please_wait));
        progressDoalog.setTitle(getString(R.string.connection___));
        progressDoalog.show();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
                if (response.code() == 200) {
                    progressDoalog.dismiss();
                    nextFragment(next_form_frame);
                    sendBundle(next_form_frame, emailValue, passwordValue);
                } else if (response.code() == 400) {
                    progressDoalog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                } else {
                    progressDoalog.dismiss();
                    email.setError(getString(R.string.email_already_exist));
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(),  getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
            }
        });
    }

}