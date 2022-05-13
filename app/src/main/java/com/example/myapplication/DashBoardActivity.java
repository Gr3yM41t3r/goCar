package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.HomePageFragment;
import com.example.myapplication.Fragments.UserProfileFragment;
import com.example.myapplication.Utility.SaveSharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.io.IOException;
import java.security.GeneralSecurityException;

public class DashBoardActivity extends AppCompatActivity {

    ImageView imageView ;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Fragment fm = new HomePageFragment();
        setFragment(fm);
        BottomNavigationView navigationView = findViewById(R.id.activity_main_bottom_navigation);
        navigationView.setOnItemSelectedListener(item -> {
            try {
                myClick(item);
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
            return true;
        });


    }

    public void setFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_right,R.anim.slide_out_left)
                .replace(R.id.flFragment, fragment).setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        //    super.onBackPressed();
        }else {
            this.finish();
        }
    }

    @Override
    public void finish() {
        super.finish();

    }




    @SuppressLint("NonConstantResourceId")
    public void myClick(MenuItem item) throws GeneralSecurityException, IOException {
        switch (item.getItemId()) {
            case R.id.homePage:
                break;
            case R.id.favorite:
                break;
            case R.id.account:
                if (SaveSharedPreference.isLogedIn(DashBoardActivity.this)) {
                    Fragment userFragment = new UserProfileFragment();
                    setFragment(userFragment);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

}