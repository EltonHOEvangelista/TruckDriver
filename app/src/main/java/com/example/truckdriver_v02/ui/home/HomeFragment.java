package com.example.truckdriver_v02.ui.home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.truckdriver_v02.R;

import com.example.truckdriver_v02.data.SQLite.DbHandler;
import com.example.truckdriver_v02.data.account.Driver;
import com.example.truckdriver_v02.data.vehicle.Truck;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.Task;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private MapView mapView;
    private GoogleMap myMap;
    private final int ACCESS_LOCATION_REQUEST_CODE = 1001;
    FusedLocationProviderClient fusedLocationProviderClient;    //API to manage location request
    private LocationRequest locationRequest;    //hold location request
    Marker deviceLocationMarker;    //vehicle icon on map
    Circle deviceLocationAccurancyCircle; //vehicle icon setup

    public Driver driver = new Driver(null, null);

    public Truck truck = new Truck();

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the map element by its ID
        mapView = view.findViewById(R.id.mapView);

        // Initialize the MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //Initialize Google Location API
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //Initialize location request
        locationRequest = new LocationRequest.Builder(6_000)
                .setMinUpdateIntervalMillis(500)
                .setMinUpdateDistanceMeters(1)
                .setPriority(android.location.LocationRequest.QUALITY_HIGH_ACCURACY)
                .build();

        //load driver's details on Home Fragment's Driver Class
        SetAccountDetails();

        //load vehicle's details on Home Fragment's Truck Class
        SetVehicleDetails();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        myMap = map;

        //Checking permission to get location
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //map configuration
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);    //default map (normal)
            myMap.setOnMapLongClickListener(this);  //to perform actions for user's long click on map
            myMap.setOnMarkerDragListener(this);    // to set a listener that responds to events related to dragging markers on map
            //myMap.setMyLocationEnabled(true);   //to create a button on the top right corner of the map to focus on device's location

            //method to check settings and start location updates
            checkSettingsAndStartLocationUpdates();
        }
        //If not authorized, Request user's permission to access device location
        else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_LOCATION_REQUEST_CODE);
        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_LOCATION_REQUEST_CODE);
        }
    }

    private void checkSettingsAndStartLocationUpdates() {

        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(getActivity());

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                //settings of device are satisfied and start location updates
                startLocationUpdates();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(getActivity(), ACCESS_LOCATION_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    //location Callback
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            if (locationResult == null){
                return;
            }
            super.onLocationResult(locationResult);

            Log.d(TAG, "onLocationResult " + locationResult.getLastLocation());

            //call method to set device location marker
            if (myMap != null){
                setDeviceLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    //method to start location updates
    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    //method to stop location updates. Called when map destroyed
    private void stopLocationUpdates() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void setDeviceLocationMarker(Location location){

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if(deviceLocationMarker == null){

            //create a new marker
            MarkerOptions markerOptions = new MarkerOptions();;    //vehicle icon setup
            markerOptions.position(latLng);
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);

            switch (driver.getDriverStatus()){

                case 0:
                    //unavailable
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.black_truck_out));
                    break;

                case 1:
                    //available
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.black_truck));
                    break;

                default:
                    //do something
                    break;
            }

            deviceLocationMarker = myMap.addMarker(markerOptions);
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        }
        else{
            //use the previously created marker
            deviceLocationMarker.setPosition(latLng);
            deviceLocationMarker.setRotation(location.getBearing());
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        }

        if (deviceLocationAccurancyCircle == null){
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(2);
            circleOptions.strokeColor(Color.TRANSPARENT);
            circleOptions.fillColor(Color.TRANSPARENT);
            circleOptions.radius(location.getAccuracy());
            deviceLocationAccurancyCircle = myMap.addCircle(circleOptions);
        }
        else{
            deviceLocationAccurancyCircle.setCenter(latLng);
            deviceLocationAccurancyCircle.setRadius(location.getAccuracy());
        }

        //Update latitude and longitude at Truck Class
        truck.setLatitude(String.valueOf(location.getLatitude()));
        truck.setLongitude(String.valueOf(location.getLongitude()));

        //calling method to transmit data
        TransmitDataToServer();
    }

    //get driver account details and set them into Driver Class
    public void SetAccountDetails() {

        //Instantiating DbHandler
        DbHandler dbHandler = new DbHandler(getActivity());

        //get opened session (equal 1) on app by account id
        int accountId = dbHandler.getOpenedSession();

        //get account details
        String[] driverData;
        driverData = dbHandler.getAccountDetails(accountId);

        //close dbHandler
        dbHandler.close();

        driver.setAccountId(Integer.parseInt(driverData[0]));
        driver.setFirstName(driverData[1]);
        driver.setLastName(driverData[2]);
        driver.setPhone(Long.parseLong(driverData[3]));
        driver.setEmail(driverData[4]);
        driver.setActiveAccount(Integer.parseInt(driverData[6]));
        driver.setDriverStatus(Integer.parseInt(driverData[7]));

        /*
        accountData[0] = String.valueOf(accountNumber);
        accountData[1] = firstName;
        accountData[2] = lastName;
        accountData[3] = String.valueOf(phone);
        accountData[4] = email;
        accountData[5] = password;
        accountData[6] = String.valueOf(isEnable);
        accountData[7] = String.valueOf(driverStatus);
         */
    }

    //method to get vehicle details and set them into Truck Class
    public void SetVehicleDetails() {

        //Instantiating DbHandler
        DbHandler dbHandler = new DbHandler(getActivity());

        //get account details
        String[] vehicleData;
        vehicleData = dbHandler.getVehicleDetails(driver.getAccountId());

        //close dbHandler
        dbHandler.close();

        truck.setVehicleId(Integer.parseInt(vehicleData[0]));
        truck.setPlate(vehicleData[1]);
        truck.setVin(vehicleData[2]);
        truck.setManufacturer(vehicleData[3]);
        truck.setModel(vehicleData[4]);
        truck.setVehicleColor(vehicleData[5]);
        truck.setCapacity(vehicleData[6]);
        truck.setTrailer(vehicleData[7]);
        truck.setOwnerAccountNumber(Integer.parseInt(vehicleData[8]));

        /*
        vehicleData[0] = String.valueOf(VehicleId);
        vehicleData[1] = Plate;
        vehicleData[2] = Vin;
        vehicleData[3] = Manufacturer;
        vehicleData[4] = Model;
        vehicleData[5] = Color;
        vehicleData[6] = Capacity;
        vehicleData[7] = Trailer;
        vehicleData[8] = String.valueOf(accountNumber);
         */
    }

    public void ResetMarkerOption() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
        deviceLocationMarker = null;
        mapView.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        stopLocationUpdates();
        deviceLocationMarker = null;
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
    }

    //method to transmit data to the server
    public void TransmitDataToServer(){
        /*
        Specifically for this project, sending data to the server is not included in the scope of the application and, therefore,
        the data will only be logged in app.
        */

        //get account and vehicle details.

        String dataQueryT;

        dataQueryT = driver.getAccountId() +
                ", " + driver.getFirstName() +
                ", " + driver.getLastName() +
                ", " + driver.getPhone() +
                ", " + driver.getEmail() +
                ", " + driver.getDriverStatus();

        Log.e("Data Transmission (account)", dataQueryT);

        dataQueryT = truck.getVehicleId() +
                ", " + truck.getPlate() +
                ", " + truck.getOwnerAccountNumber() +
                ", " + truck.getVin() +
                ", " + truck.getManufacturer() +
                ", " + truck.getModel() +
                ", " + truck.getVehicleColor() +
                ", " + truck.getTrailer() +
                ", " + truck.getCapacity()+
                ", " + truck.getLatitude() +
                ", " + truck.getLongitude();

        Log.e("Data Transmission (vehicle)", dataQueryT);
    }
}