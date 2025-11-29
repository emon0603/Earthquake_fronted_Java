package com.emon.earthquake;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.emon.earthquake.Framgent.EarthQuakeFragment;
import com.emon.earthquake.Framgent.EmergencyFragment;
import com.emon.earthquake.Framgent.HomeFragment;
import com.emon.earthquake.Framgent.MoreFragment;
import com.emon.earthquake.Framgent.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    boolean Exit = false;
    boolean Exit2 = true;
    boolean isHome = true;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        LoadBottomNavigation();
    }



    private void LoadBottomNavigation() {


        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_weather) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new WeatherFragment()).commit();
                    return true;
                }  else if (itemId == R.id.bottom_emergency) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new EmergencyFragment()).commit();
                    return true;
                } else if (itemId == R.id.bottom_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
                    return true;
                }
                else if (itemId == R.id.bottom_earthquake) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new EarthQuakeFragment()).commit();
                    return true;
                } else if (itemId == R.id.bottom_more) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MoreFragment()).commit();
                    return true;
                }


                return false;
            }
        });
    }


    public void onBackPressed() {

        if (isHome && Exit2) {
            finishAffinity();
        }

    }

}