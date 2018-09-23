package com.miles.foodtruck.service.Workers;

import android.content.Context;

import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.DbManager;
import com.miles.foodtruck.service.TrackingService;
import com.miles.foodtruck.util.Helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkerThreads {

    public static void saveTrackingThread(final AbstractTracking tracking, final Context context){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                DbManager.getSingletonInstance(context).saveOnChanged(tracking);

            }
        });

        thread.start();
    }



    public static void updateLocationThread(final Context context){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                ArrayList<AbstractTracking> trackings =
                        TrackingManager.getSingletonInstance().getAll();

                if (trackings.size()!=0)
                {
                    //For each tracking in the system, update the current location
                    for (AbstractTracking tracking: trackings) {

                        tracking.setCurrLocation("");

                        //Get available tracking info from tracking service
                        //Filter the tracking info, remove non-stops
                        List<TrackingService.TrackingInfo> trackingInfos =
                                Helpers.getTrackingInfoForTrackable(tracking.getTrackingId(),
                                        new Date(),context,true);

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
                                    tracking.setMeetLocation(Double.toString(trackingInfo.latitude)
                                            + "," +
                                            Double.toString(trackingInfo.longitude));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });

        thread.start();



    }

}
