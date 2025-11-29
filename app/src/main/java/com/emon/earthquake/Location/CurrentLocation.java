package com.emon.earthquake.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

public class CurrentLocation {

    private static CurrentLocation instance;
    private LatLng location;
    private final MutableLiveData<LatLng> locationLiveData = new MutableLiveData<>();

    private CurrentLocation() { }

    public static CurrentLocation getInstance() {
        if (instance == null) {
            instance = new CurrentLocation();
        }
        return instance;
    }

    public void setLocation(double lat, double lng) {
        this.location = new LatLng(lat, lng);
        locationLiveData.postValue(location); // Notify observers
    }

    public LatLng getLocation() {
        return location;
    }

    public boolean isLocationSet() {
        return location != null;
    }

    public LiveData<LatLng> getLocationLiveData() {
        return locationLiveData;
    }
}
