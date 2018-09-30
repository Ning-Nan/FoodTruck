package com.miles.foodtruck.controller;

import android.util.Log;
import android.view.View;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.model.Tracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.service.TrackingService;
import com.miles.foodtruck.service.workers.RemoveFromDbThread;
import com.miles.foodtruck.util.Helpers;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;


/*
    Reusable Long Click listener for both trackales and trackings
 */
public class OnLongClickListener implements View.OnLongClickListener{

    private static final String LOG_TAG = OnLongClickListener.class.getName();
    private String trackableId;
    private AbstractTracking tracking;

    public OnLongClickListener(String trackableId, AbstractTracking tracking){

        this.trackableId = trackableId;
        this.tracking = tracking;

    }

    @Override
    public boolean onLongClick(View v) {

        //Case called from Trackables. Log route information.
        if (tracking == null){
            //For A1, using current date as searching date.
            List<TrackingService.TrackingInfo> matched = Helpers.getTrackingInfoForTrackable(
                    trackableId,new Date(),
                    v.getContext(),false);
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                    DateFormat.MEDIUM);
            for (int i = 0; i < matched.size(); i++) {
                logTrackingInfo(matched.get(i),dateFormat);
            }

        }
        else
        {   //Case called from Tracking List. Remove item.
            //Also remove from database, use another thread.
            TrackingManager.getSingletonInstance().remove((Tracking) tracking);
            RemoveFromDbThread thread = new RemoveFromDbThread(tracking,v.getContext());
            thread.start();


        }

        return true;
    }


    //Log single tracking info
    private void logTrackingInfo(TrackingService.TrackingInfo trackingInfo,
                                 DateFormat dateFormat){

        String stopTime;
        if (trackingInfo.stopTime > 0)
        {
            stopTime = String.format("Stop %d minutes.", trackingInfo.stopTime);
        }
        else{
            stopTime = "No Stop";

        }

        Log.i(LOG_TAG, String.format("Trackable ID: %d, Time: %s, Location: %.5f,%.5f, %s.",
                trackingInfo.trackableId,dateFormat.format(trackingInfo.date),
                trackingInfo.latitude,
                trackingInfo.longitude, stopTime));

    }
}
