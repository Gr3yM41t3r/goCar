package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.BlankFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Fragment fm = new BlankFragment();
        setFragment(fm);
        BottomNavigationView navigationView = findViewById(R.id.activity_main_bottom_navigation);
        navigationView.setOnItemSelectedListener(item -> {
            myClick(item);
            return true;
        });

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.flFragment, fragment);
        t.commit();
    }

    public void myClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homePage:
                Toast.makeText(HomePageActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                SaveSharedPreference.setUsersEmail(HomePageActivity.this, "");
                break;
            case R.id.favorite:
                break;
            case R.id.account:
                if (SaveSharedPreference.isLogedIn(HomePageActivity.this)) {
                    Toast.makeText(HomePageActivity.this, "Already Logged in", Toast.LENGTH_LONG).show();

                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }
}