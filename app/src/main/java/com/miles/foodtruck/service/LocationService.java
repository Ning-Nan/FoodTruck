package com.miles.foodtruck.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

//Get current location
public class LocationService implements LocationListener {

    private LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private AppCompatActivity activity;
    private static Location currLocation = null;

    public LocationService(AppCompatActivity activity) {

        this.activity = activity;
    }

    public void initLocation(Context context) {

        //Get the location service and start to request.
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //Permission check.
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                2000, 0, this);
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                2000, 0, this );

        //send last known.
        if (currLocation == null)
        {
            currLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (currLocation == null)
            {
                currLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }
        }




    }

    public static Location getCurrLocation(){
        return currLocation;
    }



    @Override
    public void onLocationChanged(Location location) {

        //Update current location.
        currLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
