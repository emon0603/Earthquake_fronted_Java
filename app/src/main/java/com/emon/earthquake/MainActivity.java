package com.emon.earthquake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import com.emon.earthquake.Framgent.EarthQuakeFragment;
import com.emon.earthquake.Framgent.EmergencyFragment;
import com.emon.earthquake.Framgent.HomeFragment;
import com.emon.earthquake.Framgent.MoreFragment;
import com.emon.earthquake.Framgent.WeatherFragment;
import com.emon.earthquake.Worker.CheckApiWorker;
import com.emon.earthquake.Worker.WorkerHelper;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.BottomAppBar);




        loadBottomNav();
        BatteryOptimization();
        WorkerHelper.startMyWorker(this);



    }

    private void loadBottomNav(){

        // Default fragment
        loadFragment(new HomeFragment());
        bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);

        // --- FAB --> HOME FRAGMENT ---
        fab.setOnClickListener(v -> {
            loadFragment(new HomeFragment());
            bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
        });

        // --- Bottom Navigation Listener ---
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.bottom_home) {
                loadFragment(new HomeFragment());
                return true;

            } else if (id == R.id.bottom_emergency) {
                loadFragment(new EmergencyFragment());
                return true;

            }else if (id == R.id.bottom_earthquake) {
                loadFragment(new EarthQuakeFragment());
                return true;

            }

            return false;
        });
    }

    private void loadFragment(Fragment fragment) {

        bottomAppBar.setBackgroundResource(R.drawable.bottom_nav_background);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }


    private void BatteryOptimization(){
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);

        if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

    }
}
