package com.miles.foodtruck.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.miles.foodtruck.controller.SuggestionNotification;
import com.miles.foodtruck.model.Tracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.workers.SaveToDbThread;
import com.miles.foodtruck.service.workers.SuggestionAsyncTask;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;

public class ActionReceiver extends BroadcastReceiver
{
    public static int networkStatus = -1;
    public static SuggestionAsyncTask.TrackableInfo trackableInfo;



    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null)
        {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
            {
                ConnectivityManager manager =
                        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();

                if (networkInfo != null)
                {
                    if (networkStatus  == -1)
                    {
                        networkStatus = 1;
                    }
                    else
                    {
                        networkStatus = 1;
                        SuggestionAsyncTask suggestionAsyncTask = new SuggestionAsyncTask(context);
                        suggestionAsyncTask.execute();
                    }

                }
                else
                {
                    networkStatus = 0;
                    Helpers.callToast("Lost Internet",context);
                }
            }
        }
        else{

            String action=intent.getStringExtra("Action");

            switch (action){
                case "Next":
                    nextNotification();
                    break;

                case"Dismiss":
                    dismissNotification(context);
                    break;
                case"Add":
                    addTracking(context);
                    break;

                case "Alarm":
                    setAlarm(context);
                    break;
            }

        }




    }


    private void nextNotification(){
        if (SuggestionNotification.trackableInfos.size() <= SuggestionNotification.index + 1)
        {
            //case end.

        }
        else
        {
            SuggestionNotification.index += 1;
            SuggestionNotification.show();
        }
    }

    private void dismissNotification(Context context){
        int notificationId = 1;
        NotificationManagerCompat.from(context).cancel(notificationId);
    }

    private void setAlarm(Context context){
        SuggestionAsyncTask suggestionAsyncTask = new SuggestionAsyncTask(context);
        suggestionAsyncTask.execute();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent1 = new Intent(context, ActionReceiver.class);
        intent1.putExtra("Action","Alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                3,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences settings = context.getSharedPreferences("setting", context.MODE_PRIVATE);
        int millis = settings.getInt("SuggestionFrequency",60)  * 1000;


        am.setExact(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + millis ,pendingIntent);
    }

    private void addTracking(Context context)
    {
        AbstractTracking tracking = new Tracking();
        tracking.setTitle(trackableInfo.title);
        tracking.setMeetTime(trackableInfo.meetTime);
        tracking.setMeetLocation(trackableInfo.meetLocation);
        tracking.setTargetStartTime(trackableInfo.targetStartTime);
        tracking.setTargetEndTime(trackableInfo.targetEndTime);
        tracking.setTrackableId(trackableInfo.trackableId);


        TrackingManager trackingManager = TrackingManager.getSingletonInstance();


        tracking.setTrackingId(Helpers.random(5,trackingManager));

        trackingManager.addToTracking(tracking);

        //modify database in separate thread.
        SaveToDbThread thread = new SaveToDbThread(tracking,context);
        thread.start();
    }
}
