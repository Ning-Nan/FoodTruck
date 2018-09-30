package com.miles.foodtruck.controller;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.miles.foodtruck.R;
import com.miles.foodtruck.service.Workers.SuggestionAsyncTask;

import java.util.ArrayList;

public class SuggestionNotification {


    private ArrayList<SuggestionAsyncTask.TrackableInfo> trackableInfos;
    private Context context;
    private NotificationCompat.Builder mBuilder;

    public SuggestionNotification(Context context,
                                  ArrayList<SuggestionAsyncTask.TrackableInfo> trackableInfos){

        this.context = context;
        this.trackableInfos = trackableInfos;
        init();

    }


    private void init(){

        //get First
        SuggestionAsyncTask.TrackableInfo trackableInfo = trackableInfos.get(0);

        //Display String
        String str =String.format("Trackable Name: %s\nWalk Time: %d\nDistance: %d\nMeet Time: %s",
                trackableInfo.title,trackableInfo.duration/60,trackableInfo.distance,
                trackableInfo.meetTime.toString()) ;

        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Food Truck Suggestion")
                .setContentText(trackableInfo.title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(str))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }



    public void show(){

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(1,mBuilder.build());


    }

    public void showNext(){

    }
}
