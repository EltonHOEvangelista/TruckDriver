package com.example.truckdriver_v02.ui.home;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import java.util.List;

public class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

        // This method is called when the location changes
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Do something with the new location (e.g., update UI, send to server)
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
