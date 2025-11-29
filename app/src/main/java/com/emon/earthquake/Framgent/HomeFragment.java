package com.emon.earthquake.Framgent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.emon.earthquake.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView current_location_tv;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        current_location_tv = view.findViewById(R.id.current_location_tv);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        fetchCurrentLocation();

        return view;
    }

    private void fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null && isAdded()) {
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        fetchPlaceName(current);
                    } else {
                        current_location_tv.setText("Location not available yet");
                    }
                })
                .addOnFailureListener(e -> current_location_tv.setText("Error fetching location"));
    }

    private void fetchPlaceName(LatLng location) {
        new Thread(() -> {
            String name = "Unknown Location";
            try {
                Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);

                if (addresses != null && !addresses.isEmpty()) {
                    Address addr = addresses.get(0);

                    if (addr.getLocality() != null) {
                        name = addr.getLocality();
                    } else if (addr.getSubAdminArea() != null) {
                        name = addr.getSubAdminArea();
                    } else if (addr.getAdminArea() != null) {
                        name = addr.getAdminArea();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            final String finalName = name;
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> current_location_tv.setText(finalName));
            } else {
                Log.d("HomeFragment", "Fragment not attached yet");
            }
        }).start();
    }
}
