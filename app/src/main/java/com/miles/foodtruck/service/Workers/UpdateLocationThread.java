package com.miles.foodtruck.service.Workers;


import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.TrackingService;
import com.miles.foodtruck.util.Helpers;
import com.miles.foodtruck.view.TrackingListActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateLocationThread  extends Thread {

    private TrackingListActivity activity;
    private static final String DEFAULT_LOCATION = "Driving";


    public UpdateLocationThread(TrackingListActivity activity){

        this.activity = activity;
    }

    @Override
    public void run(){

        //Get all trackings in the memory
        final ArrayList<AbstractTracking> trackings =
                TrackingManager.getSingletonInstance().getAll();

        if (trackings.size()!=0)
        {
            //For each tracking, update the current location
            for (AbstractTracking tracking: trackings) {

                //Set the current location as default
                tracking.setCurrLocation(DEFAULT_LOCATION);

                //Get available tracking info from tracking service
                //Filter the tracking info, remove non-stops
                List<TrackingService.TrackingInfo> trackingInfos =
                        Helpers.getTrackingInfoForTrackable(Integer.toString(tracking.getTrackableId()),
                                new Date(),activity.getApplicationContext(),true);
                //If current time fit in the time range, then set the current location.
                if (trackingInfos.size()!=0)
                {

                    for (TrackingService.TrackingInfo trackingInfo: trackingInfos) {
                        Date start = trackingInfo.date;
                        Date end = Helpers.caculateEndTime(start,trackingInfo.stopTime);
                        Date now = new Date();

                        if ( (now.after(start) && now.before(end)) ||
                                now.getTime() == start.getTime()||
                                now.getTime() == end.getTime())
                        {

                            tracking.setCurrLocation(Double.toString(trackingInfo.latitude)
                                    + "," +
                                    Double.toString(trackingInfo.longitude));

                            break;
                        }
                    }
                }
            }
        }

        //Runnable for UI update.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Update adapter dataset.
                activity.getAdapter().updateTrackings(trackings);
                activity.getAdapter().notifyDataSetChanged();
            }
        };

        //Finished update tracking locations,Next do UI update.
        activity.runOnUiThread(runnable);


    }



}
