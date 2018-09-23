package com.miles.foodtruck.model;


import com.miles.foodtruck.model.abstracts.AbstractTracking;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

public class TrackingManager extends Observable{

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

        //Check same id
        while (iter.hasNext()) {
           AbstractTracking tempTracking = iter.next();
            if (tempTracking.getTrackingId().equals(tracking.getTrackingId()))
            {
                iter.remove();
            }
        }

        //Sort by date
        int position = -1;
        for (int i = 0; i < trackings.size(); i++) {
            if (tracking.getMeetTime().before(trackings.get(i).getMeetTime())) {

                position = i;
                break;
            }
        }
        if (position !=-1){
            trackings.add(position, tracking);
        }
        else {
            trackings.add(tracking);

        }

        //Notify tracking list adapter to update
        setChanged();
        notifyObservers(trackings);

    }

    public ArrayList<AbstractTracking> getAll(){

        return trackings;
    }

    public void remove(Tracking tracking)
    {
        trackings.remove(tracking);
        setChanged();
        notifyObservers(trackings);
    }

    public AbstractTracking get(String id){

        for (int i = 0; i <trackings.size() ; i++) {

            if (trackings.get(i).getTrackingId().equals(id))
            {
                return trackings.get(i);
            }

        }

        return null;

    }


}





