package com.emon.earthquake.Framgent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emon.earthquake.Location.CurrentLocation;
import com.emon.earthquake.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class EarthQuakeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ProgressBar progressBar;
    LinearLayout detail_layout;
    View map_click;

    private static EarthQuakeFragment instance;
    private boolean mapInitialized = false;
    private boolean currentLocationShown = false;

    public EarthQuakeFragment() { }

    public static EarthQuakeFragment getInstance() {
        if (instance == null) {
            instance = new EarthQuakeFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View Earthview = inflater.inflate(R.layout.fragment_earth_quake, container, false);
        progressBar = Earthview.findViewById(R.id.progressBar);
        detail_layout = Earthview.findViewById(R.id.detail_layout);
        map_click = Earthview.findViewById(R.id.map);

        progressBar.setVisibility(View.VISIBLE);
        detail_layout.setVisibility(View.GONE);





        // Delay map setup to ensure fragment is fully attached
        Earthview.postDelayed(() -> {
            if (isAdded() && !mapInitialized) {
                mapInitialized = true;
                setupMap();
            }
        }, 500);

        return Earthview;
    }

    private void setupMap() {
        if (!isAdded()) return;

        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        }

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

        detail_layout.setAlpha(0f);

        mMap.setOnMapLoadedCallback(() -> {
            progressBar.setVisibility(View.GONE);

            // Fade-in animation
            detail_layout.setVisibility(View.VISIBLE);
            detail_layout.animate()
                    .alpha(1f)       // fade to fully visible
                    .setDuration(500) // 500ms duration
                    .start();
        });

        // User map click → hide detail layout
        mMap.setOnMapClickListener(latLng -> {
            if (detail_layout.getVisibility() == View.VISIBLE) {
                detail_layout.animate()
                        .alpha(0f) // fade out
                        .setDuration(400) // 400ms duration
                        .withEndAction(() -> detail_layout.setVisibility(View.GONE))
                        .start();
            }
        });



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

        List<Marker> markers = new ArrayList<>();
        for (LatLng loc : locations) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title("Lat: " + loc.latitude + ", Lng: " + loc.longitude));
            markers.add(marker);
        }

        // Marker click listener
        mMap.setOnMarkerClickListener(marker -> {
            if (markers.contains(marker)) {
                detail_layout.setVisibility(View.VISIBLE);
                detail_layout.animate()
                        .alpha(1f)       // fade to fully visible
                        .setDuration(500) // 500ms duration
                        .start();
                Toast.makeText(getActivity(),
                        "Clicked Location:\n" + marker.getTitle(),
                        Toast.LENGTH_LONG).show();
                return true;
            }
            return false;
        });

        // Get current location once
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null && isAdded() && getActivity() != null) {

                        // Save current location in Singleton
                        CurrentLocation.getInstance().setLocation(
                                location.getLatitude(), location.getLongitude()
                        );

                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(current).title("You are here"));

                        if (!currentLocationShown) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 6));
                            currentLocationShown = true;

                            Toast.makeText(getActivity(),
                                    "Current Location:\nLatitude: " + location.getLatitude() +
                                            "\nLongitude: " + location.getLongitude(),
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
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
