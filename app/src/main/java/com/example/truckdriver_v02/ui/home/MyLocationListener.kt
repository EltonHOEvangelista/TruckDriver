package com.example.truckdriver_v02.ui.home

import android.location.Location
import android.location.LocationListener

class MyLocationListener : LocationListener {
    override fun onLocationChanged(location: Location) {
        // This method is called when the location changes

        val latitude = location.latitude
        val longitude = location.longitude

        // Do something with the new location (e.g., update UI, send to server)
    }

    override fun onFlushComplete(requestCode: Int) {
        super.onFlushComplete(requestCode)
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
    }
}
