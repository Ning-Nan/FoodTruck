package com.miles.foodtruck.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.miles.foodtruck.R;
import com.miles.foodtruck.service.ActionReceiver;
import com.miles.foodtruck.service.workers.SuggestionAsyncTask;
import com.miles.foodtruck.util.Constant;

import java.util.ArrayList;


//Class get called when at specific time.
//Get models and push them to notification.
public class SuggestionNotification {


    //Static so we know currently showing index, trackables.
    //Convenient for Add, next function.
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

        //get based on current index.
        SuggestionAsyncTask.TrackableInfo trackableInfo = trackableInfos.get(index);

        //Update in receiver, So receiver know which one is showing.
        ActionReceiver.trackableInfo = trackableInfo;

        //Display String
        String str =String.format(
                "Trackable Name: %s\nWalk Time: %d minute(s)\nDistance: %fkm\nMeet Time: %s",
                trackableInfo.title,trackableInfo.duration/60,(double)trackableInfo.distance/1000,
                trackableInfo.meetTime.toString()) ;


        //Intents for different operations.
        //Passed to Broadcast Receiver.
        Intent addIntent = new Intent(context,ActionReceiver.class);
        addIntent.putExtra(Constant.Action,Constant.AddAction);
        PendingIntent pendingAddIntent = PendingIntent.getBroadcast(context,
                Constant.AddActionRequestCode,addIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Intent nextIntent = new Intent(context,ActionReceiver.class);
        nextIntent.putExtra(Constant.Action,Constant.NextAction);
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(context,
                Constant.NextActionRequestCode,nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Intent dismissIntent = new Intent(context,ActionReceiver.class);
        dismissIntent.putExtra(Constant.Action,Constant.DismissAction);
        PendingIntent pendingDismissIntent = PendingIntent.getBroadcast(context,
                Constant.DismissActionRequestCode,dismissIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        //Build the notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(Constant.SuggestionTitle)
                .setContentText(trackableInfo.title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(str))
                .addAction(R.drawable.ic_launcher_background,Constant.AddAction,pendingAddIntent)
                .addAction(R.drawable.ic_launcher_background,Constant.NextAction,pendingNextIntent)
                .addAction(R.drawable.ic_launcher_background,Constant.DismissAction,pendingDismissIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Constant.SuggestionNotificationCode,mBuilder.build());
    }


}
