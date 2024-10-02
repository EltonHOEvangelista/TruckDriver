package com.example.truckdriver_v02.ui.home

import android.Manifest
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.MotionEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.truckdriver_v02.R
import com.example.truckdriver_v02.data.SQLite.DbHandler
import com.example.truckdriver_v02.data.account.Driver
import com.example.truckdriver_v02.data.vehicle.Truck
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment : Fragment(), OnMapReadyCallback, OnMapLongClickListener, OnMarkerDragListener {
    private lateinit var mapView: MapView
    private lateinit var myMap: GoogleMap
    private val ACCESS_LOCATION_REQUEST_CODE = 1001
    var fusedLocationProviderClient: FusedLocationProviderClient? =
        null //API to manage location request
    private var locationRequest: LocationRequest? = null //hold location request
    var deviceLocationMarker: Marker? = null //vehicle icon on map
    var deviceLocationAccurancyCircle: Circle? = null //vehicle icon setup

    @JvmField
    var driver: Driver = Driver(null.toString(), null.toString())

    var truck: Truck = Truck()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the map element by its ID
        mapView = view.findViewById(R.id.mapView)

        // Initialize the MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        //Initialize Google Location API
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)

        //Initialize location request
        locationRequest = LocationRequest.Builder(6000)
            .setMinUpdateIntervalMillis(500)
            .setMinUpdateDistanceMeters(1f)
            .setPriority(android.location.LocationRequest.QUALITY_HIGH_ACCURACY)
            .build()

        //load driver's details on Home Fragment's Driver Class
        SetAccountDetails()

        //load vehicle's details on Home Fragment's Truck Class
        SetVehicleDetails()

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        myMap = map

        //Checking permission to get location
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //map configuration

            myMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL //default map (normal)
            myMap!!.setOnMapLongClickListener(this) //to perform actions for user's long click on map
            myMap!!.setOnMarkerDragListener(this) // to set a listener that responds to events related to dragging markers on map

            //myMap.setMyLocationEnabled(true);   //to create a button on the top right corner of the map to focus on device's location

            //method to check settings and start location updates
            checkSettingsAndStartLocationUpdates()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_LOCATION_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_LOCATION_REQUEST_CODE
            )
        }
    }

    private fun checkSettingsAndStartLocationUpdates() {
        val request = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!).build()
        val client = LocationServices.getSettingsClient(requireActivity())

        val locationSettingsResponseTask = client.checkLocationSettings(request)
        locationSettingsResponseTask.addOnSuccessListener { //settings of device are satisfied and start location updates
            startLocationUpdates()
        }

        locationSettingsResponseTask.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(requireActivity(), ACCESS_LOCATION_REQUEST_CODE)
                } catch (ex: SendIntentException) {
                    throw RuntimeException(ex)
                }
            }
        }
    }

    //location Callback
    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult == null) {
                return
            }
            super.onLocationResult(locationResult)

            Log.d(MotionEffect.TAG, "onLocationResult " + locationResult.lastLocation)

            //call method to set device location marker
            if (myMap != null) {
                setDeviceLocationMarker(locationResult.lastLocation)
            }
        }
    }

    //method to start location updates
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    //method to stop location updates. Called when map destroyed
    private fun stopLocationUpdates() {
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
    }

    private fun setDeviceLocationMarker(location: Location?) {
        val latLng = LatLng(
            location!!.latitude, location.longitude
        )

        if (deviceLocationMarker == null) {
            //create a new marker

            val markerOptions = MarkerOptions()
            //vehicle icon setup

            markerOptions.position(latLng)
            markerOptions.rotation(location.bearing)
            markerOptions.anchor(0.5.toFloat(), 0.5.toFloat())

            when (driver.driverStatus) {
                0 ->                     //unavailable
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.black_truck_out))

                1 ->                     //available
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.black_truck))

                else -> {}
            }
            deviceLocationMarker = myMap!!.addMarker(markerOptions)
            myMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
        } else {
            //use the previously created marker
            deviceLocationMarker!!.position = latLng
            deviceLocationMarker!!.rotation = location.bearing
            myMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
        }

        if (deviceLocationAccurancyCircle == null) {
            val circleOptions = CircleOptions()
            circleOptions.center(latLng)
            circleOptions.strokeWidth(2f)
            circleOptions.strokeColor(Color.TRANSPARENT)
            circleOptions.fillColor(Color.TRANSPARENT)
            circleOptions.radius(location.accuracy.toDouble())
            deviceLocationAccurancyCircle = myMap!!.addCircle(circleOptions)
        } else {
            deviceLocationAccurancyCircle!!.center = latLng
            deviceLocationAccurancyCircle!!.radius = location.accuracy.toDouble()
        }

        //Update latitude and longitude at Truck Class
        truck.latitude = location.latitude.toString()
        truck.longitude = location.longitude.toString()

        //calling method to transmit data
        TransmitDataToServer()
    }

    //get driver account details and set them into Driver Class
    fun SetAccountDetails() {
        //Instantiating DbHandler

        val dbHandler = DbHandler(activity)

        //get opened session (equal 1) on app by account id
        val accountId = dbHandler.openedSession

        //get account details
        driver = dbHandler.getAccountDetails(accountId)

        //close dbHandler
        dbHandler.close()
    }

    //method to get vehicle details and set them into Truck Class
    fun SetVehicleDetails() {

        //Instantiating DbHandler
        val dbHandler = DbHandler(activity)

        //get account details
        truck = dbHandler.getVehicleDetails(driver.accountId)

        //close dbHandler
        dbHandler.close()
    }

    fun ResetMarkerOption() {
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        deviceLocationMarker = null
        mapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        deviceLocationMarker = null
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onMapLongClick(latLng: LatLng) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    //method to transmit data to the server
    fun TransmitDataToServer() {
        /*
        Specifically for this project, sending data to the server is not included in the scope of the application and, therefore,
        the data will only be logged in app.
        */

        //get account and vehicle details.
        var dataQueryT = driver.accountId.toString() +
                ", " + driver.firstName +
                ", " + driver.lastName +
                ", " + driver.phoneNumber +
                ", " + driver.eMail +
                ", " + driver.driverStatus

        Log.e("Data Transmission (account)", dataQueryT)

        dataQueryT = truck.vehicleId.toString() +
                ", " + truck.plate +
                ", " + truck.ownerAccountNumber +
                ", " + truck.vin +
                ", " + truck.manufacturer +
                ", " + truck.model +
                ", " + truck.vehicleColor +
                ", " + truck.trailer +
                ", " + truck.capacity +
                ", " + truck.latitude +
                ", " + truck.longitude

        Log.e("Data Transmission (vehicle)", dataQueryT)
    }
}