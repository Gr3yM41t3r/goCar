package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFormFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFormFragment1 extends Fragment {



    public RegisterFormFragment1() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegisterFormFragment1 newInstance(String param1, String param2) {
        RegisterFormFragment1 fragment = new RegisterFormFragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    public void nextFragment(Fragment fragment){
        ((RegisterActivity) this.getActivity()).setFragment(fragment);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_register_form1, container, false);
        Button next_form= view.findViewById(R.id.next_form);
        next_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_form= new RegisterFormFragment2();
                nextFragment(next_form);

            }
        });
        return view;
    }
}