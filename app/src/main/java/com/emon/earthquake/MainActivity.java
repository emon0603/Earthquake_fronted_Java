package com.emon.earthquake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import com.emon.earthquake.Framgent.EmergencyFragment;
import com.emon.earthquake.Framgent.HomeFragment;
import com.emon.earthquake.Framgent.MoreFragment;
import com.emon.earthquake.Framgent.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);

        // Default fragment
        loadFragment(new HomeFragment());

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

            } else if (id == R.id.bottom_weather) {
                loadFragment(new WeatherFragment());
                return true;

            } else if (id == R.id.bottom_emergency) {
                loadFragment(new EmergencyFragment());
                return true;

            } else if (id == R.id.bottom_more) {
                loadFragment(new MoreFragment());
                return true;
            }

            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
