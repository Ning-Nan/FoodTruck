package com.miles.foodtruck.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.miles.foodtruck.R;
import com.miles.foodtruck.service.ActionReceiver;
import com.miles.foodtruck.service.workers.SuggestionAsyncTask;

import java.util.ArrayList;

public class SuggestionNotification {


    public static ArrayList<SuggestionAsyncTask.TrackableInfo> trackableInfos;
    private static Context context;
    public static int index = -1;

    public SuggestionNotification(Context context,
                                  ArrayList<SuggestionAsyncTask.TrackableInfo> trackableInfos){

        this.context = context;
        this.trackableInfos = trackableInfos;
        this.index = 0;

    }


    public static void show(){

        //get First
        SuggestionAsyncTask.TrackableInfo trackableInfo = trackableInfos.get(index);

        ActionReceiver.trackableInfo = trackableInfo;

        //Display String
        String str =String.format("Trackable Name: %s\nWalk Time: %d minute(s)\nDistance: %fkm\nMeet Time: %s",
                trackableInfo.title,trackableInfo.duration/60,(double)trackableInfo.distance/1000,
                trackableInfo.meetTime.toString()) ;


        Intent addIntent = new Intent(context,ActionReceiver.class);
        addIntent.putExtra("Action","Add");
        PendingIntent pendingAddIntent = PendingIntent.getBroadcast(context,3,addIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Intent nextIntent = new Intent(context,ActionReceiver.class);
        nextIntent.putExtra("Action","Next");
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(context,1,nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Intent dismissIntent = new Intent(context,ActionReceiver.class);
        dismissIntent.putExtra("Action","Dismiss");
        PendingIntent pendingDismissIntent = PendingIntent.getBroadcast(context,2,dismissIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Food Truck Suggestion")
                .setContentText(trackableInfo.title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(str))
                .addAction(R.drawable.ic_launcher_background,"Add",pendingAddIntent)
                .addAction(R.drawable.ic_launcher_background,"Next",pendingNextIntent)
                .addAction(R.drawable.ic_launcher_background,"Dismiss",pendingDismissIntent)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(1,mBuilder.build());
    }


}
