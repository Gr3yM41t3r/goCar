package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.HomePageFragment;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.CarInterface;
import com.example.myapplication.retrofit.RegisterInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Log.e("ljlkjlkjkljlkj1",String.valueOf(fragmentManager.getBackStackEntryCount()));

        fragmentManager.beginTransaction().replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit();
        Log.e("ljlkjlkjkljlkj2",String.valueOf(fragmentManager.getBackStackEntryCount()));


    }
    @Override
    public void onBackPressed() {
        Log.e("ljlkjlkjkljlkj",String.valueOf(fragmentManager.getBackStackEntryCount()));
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
            Log.e("ljlkjlkjkljlkj",String.valueOf(fragmentManager.getBackStackEntryCount()));

        }else {
            finish();
        }
    }






    @SuppressLint("NonConstantResourceId")
    public void myClick(MenuItem item) throws GeneralSecurityException, IOException {
        switch (item.getItemId()) {
            case R.id.homePage:
                SaveSharedPreference.setUsersEmail(DashBoardActivity.this, "");
                break;
            case R.id.favorite:
                break;
            case R.id.account:
                if (SaveSharedPreference.isLogedIn(DashBoardActivity.this)) {
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

}