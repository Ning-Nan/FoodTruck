package com.miles.foodtruck.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.model.Tracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.service.ReminderService;
import com.miles.foodtruck.service.TrackingService;
import com.miles.foodtruck.service.workers.RemoveFromDbThread;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;
import com.miles.foodtruck.view.MapsActivity;
import com.miles.foodtruck.view.SettingsActivity;

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
    private Activity activity;

    public OnLongClickListener(String trackableId, AbstractTracking tracking,Activity activity){

        this.trackableId = trackableId;
        this.tracking = tracking;
        this.activity = activity;

    }

    @Override
    public boolean onLongClick(View v) {

        //Case called from Trackables. Log route information.
        if (tracking == null){



            Intent intent = new Intent(activity, MapsActivity.class);
            intent.putExtra(Constant.trackableId,trackableId);
            activity.startActivity(intent);

        }
        else
        {   //Case called from Tracking List. Remove item.
            //Also remove from database, use another thread.
            TrackingManager.getSingletonInstance().remove((Tracking) tracking);
            ReminderService reminderService = new ReminderService(activity.getApplicationContext());
            reminderService.removeAll();
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
