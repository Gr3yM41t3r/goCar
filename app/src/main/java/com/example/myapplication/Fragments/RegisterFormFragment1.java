package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.model.Compte;

import java.util.Objects;


public class RegisterFormFragment1 extends Fragment {


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
        View view =inflater.inflate(R.layout.fragment_register_form1, container, false);
        Button next_form= view.findViewById(R.id.next_form);
        EditText email= view.findViewById(R.id.emailInput);
        EditText password= view.findViewById(R.id.passwordInput);
        EditText confirmPassword= view.findViewById(R.id.confirmPasswordInput);
        next_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordValue=password.getText().toString();
                String confirmPasswordValue=confirmPassword.getText().toString();
                String emailValue =email.getText().toString();
                if (TextUtils.isEmpty(emailValue)||TextUtils.isEmpty(passwordValue)||TextUtils.isEmpty(confirmPasswordValue)){
                    email.setError("requis");
                    password.setError("requis");
                    confirmPassword.setError("requis");

                }else {
                    if (passwordValue.equals(confirmPasswordValue)){
                        Fragment next_form= new RegisterFormFragment2();
                        sendBundle(next_form,emailValue,passwordValue);
                        nextFragment(next_form);
                    }else {
                        confirmPassword.setError("les mots de passe ne se ressemblent pas");
                    }
                }


            }
        });
        return view;
    }

    public void nextFragment(Fragment fragment){
        ((RegisterActivity) this.requireActivity()).setFragment(fragment);
    }
    public void sendBundle(Fragment fragment,String email, String password){
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("password",password);
        fragment.setArguments(bundle);
    }

}