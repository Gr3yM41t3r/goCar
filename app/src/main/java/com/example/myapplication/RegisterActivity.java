package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.RegisterFormFragment1;

public class RegisterActivity extends AppCompatActivity {
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fm ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fm = new RegisterFormFragment1();
        setFragment(fm);
    }

    public void setFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.registerFragment, fragment).setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void back() {
        fragmentManager.popBackStack();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}