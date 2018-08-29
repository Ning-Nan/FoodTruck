package com.miles.foodtruck.Model;


import com.miles.foodtruck.Model.Abstract.AbstractTracking;

import java.util.ArrayList;
import java.util.Iterator;

public class TrackingManager {

    private ArrayList<AbstractTracking> trackings = new ArrayList<>();


    // Singleton
    private TrackingManager()
    {
    }

    private static class LazyHolder
    {
        static final TrackingManager INSTANCE = new TrackingManager();
    }

    public static TrackingManager getSingletonInstance( )
    {
        return TrackingManager.LazyHolder.INSTANCE;
    }

    public void addToTracking(AbstractTracking tracking)
    {
        Iterator<AbstractTracking> iter = trackings.iterator();


        while (iter.hasNext()) {
           AbstractTracking tempTracking = iter.next();
            if (tempTracking.getTrackingId().equals(tracking.getTrackingId()))
            {
                iter.remove();
            }
        }

        trackings.add(tracking);
    }

    public ArrayList<AbstractTracking> getAll(){

        return trackings;
    }
}





