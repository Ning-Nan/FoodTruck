package com.miles.foodtruck.service.Workers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.miles.foodtruck.model.TrackacbleManager;
import com.miles.foodtruck.model.abstracts.AbstractTrackable;
import com.miles.foodtruck.service.TrackingService;
import com.miles.foodtruck.util.Helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SuggestThread extends Thread {

    private AppCompatActivity activity;
    private static final String DEFAULT_LOCATION = "Driving";
    private LocationManager locationManager;


    public SuggestThread(AppCompatActivity activity) {

        this.activity = activity;
    }


    @Override
    public void run() {

        Location location = getUserLocation();

        Log.w("Location Test", location.toString());


        //Get all trackables in the memory
        final ArrayList<AbstractTrackable> trackables =
                TrackacbleManager.getTrackableList();

        //trackable info used to store the distance information for this trackable.
        ArrayList<TrackableInfo> trackableInfos = new ArrayList<>();

        if (trackables.size() != 0) {
            //For each trackable, update the current location and store into trackable info
            for (AbstractTrackable trackable : trackables) {

                //Get available tracking info from tracking service
                //Filter the tracking info, remove non-stops
                List<TrackingService.TrackingInfo> trackingInfos =
                        Helpers.getTrackingInfoForTrackable(Integer.toString(trackable.getId()),
                                new Date(), activity.getApplicationContext(), true);


                //for each tracking info, determine whether it can be arrived before it leaves.
                if (trackingInfos.size() != 0) {

                    for (TrackingService.TrackingInfo trackingInfo : trackingInfos) {

                        //1. use api to calculate distance + time.
                        //2. Check the calculated time if reached or not.
                        //3. if yes, then add to list.
                        //4. if not. skip this tracking info
                        Date start = trackingInfo.date;
                        Date end = Helpers.caculateEndTime(start, trackingInfo.stopTime);
                        Date now = new Date();

                        if ((now.after(start) && now.before(end)) ||
                                now.getTime() == start.getTime() ||
                                now.getTime() == end.getTime()) {

                            break;
                        }


                    }//end for loop
                }


            }
        }


    }


    public static class TrackableInfo {
        public int id;
        public String currLocation;


    }

    public Location getUserLocation() {

        String provider;

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        List<String> list = locationManager.getProviders(true);

        Log.w("Location Test", list.toString());


        if (list.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            Log.w("Location Test", "heleo1");

        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
            Log.w("Location Test", "heleo2");


        } else {


            return null;
        }

        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.w("Location Test", "heleo");

            return null;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        return location;

    }

}
