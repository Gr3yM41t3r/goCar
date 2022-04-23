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
 * Use the {@link RegisterFormFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFormFragment2 extends Fragment {


    public RegisterFormFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFormFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFormFragment2 newInstance(String param1, String param2) {
        RegisterFormFragment2 fragment = new RegisterFormFragment2();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_register_form2, container, false);
        Button previous_form= view.findViewById(R.id.back_form);
        previous_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment next_form= new RegisterFormFragment2();
                back();

            }
        });
        return view;
    }

    public void back(){
        ((RegisterActivity) this.getActivity()).back();
    }
}