package com.miles.foodtruck.Model;

import com.miles.foodtruck.Model.Abstract.AbstractTracking;

import java.util.ArrayList;

public class TrackingManager {
    private static ArrayList<AbstractTracking> trackings = new ArrayList<>();

    private static ArrayList<AbstractTracking> getTrackings(){
        return trackings;
    }
}
