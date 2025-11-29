package com.emon.earthquake.Location;


import android.app.Application;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationViewModel extends AndroidViewModel {

    private final MutableLiveData<String> placeName = new MutableLiveData<>();

    public LocationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getPlaceName() {
        return placeName;
    }

    public void updatePlaceName(LatLng location) {
        new Thread(() -> {
            String name = "Unknown Location";
            try {
                Geocoder geocoder = new Geocoder(getApplication(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                if (addresses != null && !addresses.isEmpty() && addresses.get(0).getLocality() != null) {
                    name = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
                name = "Error fetching location";
            }
            placeName.postValue(name); // LiveData update
        }).start();
    }
}

