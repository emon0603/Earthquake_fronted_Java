package com.emon.earthquake.Framgent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.emon.earthquake.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class EarthQuakeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ProgressBar progressBar;

    private static EarthQuakeFragment instance;
    private boolean mapInitialized = false;
    private boolean currentLocationShown = false;

    public EarthQuakeFragment() {
        // Required empty public constructor
    }

    // Singleton instance for BottomNavigation optimization
    public static EarthQuakeFragment getInstance() {
        if (instance == null) {
            instance = new EarthQuakeFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // retain fragment during configuration changes
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_earth_quake, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Lazy initialization after 1 second delay
        view.postDelayed(() -> {
            if (isAdded() && !mapInitialized) {
                mapInitialized = true;
                setupMap();
            }
        }, 1000);

        return view;
    }

    private void setupMap() {
        if (!isAdded()) return;

        // Initialize location client
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        }

        // Reuse existing map fragment or create new one
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.map, mapFragment, "MAP_FRAGMENT")
                    .commitNow();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Map fully loaded → hide progress bar
        mMap.setOnMapLoadedCallback(() -> progressBar.setVisibility(View.GONE));

        // Permission check
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        mMap.setMyLocationEnabled(true);

        // Predefined markers
        List<LatLng> locations = new ArrayList<>();
        locations.add(new LatLng(23.8103, 90.4125)); // Dhaka
        locations.add(new LatLng(22.3569, 91.7832)); // Chittagong
        locations.add(new LatLng(24.3636, 88.6241)); // Rajshahi

        for (LatLng loc : locations) {
            mMap.addMarker(new MarkerOptions().position(loc)
                    .title("Lat: " + loc.latitude + ", Lng: " + loc.longitude));
        }

        // Get current location once
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null && isAdded() && getActivity() != null) {
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(current).title("You are here"));

                        // Move camera only once
                        if (!currentLocationShown) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 6));
                            currentLocationShown = true;

                            // Show current location Toast
                            Toast.makeText(getActivity(),
                                    "Current Location:\nLatitude: " + location.getLatitude() +
                                            "\nLongitude: " + location.getLongitude(),
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // If null, move camera to first predefined location
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(0), 6));
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            setupMap();
        }
    }
}
