package com.miles.foodtruck.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.util.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ReminderService {

    private Context context;
    public static HashMap ids  = new HashMap();

    public static ArrayList<AbstractTracking> trackings = new ArrayList<>();

    public ReminderService(Context context)
    {
        this.context = context;

    }



    public void setAll(){

        if (ids.size() != 0)
        {
            removeAll();

        }

        SharedPreferences settings = context.getSharedPreferences("setting", MODE_PRIVATE);
        long millis = settings.getInt("RemindBefore",60)  * 1000;

        trackings = (ArrayList<AbstractTracking>) TrackingManager.getSingletonInstance().getAll().clone();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Log.w("asd",Integer.toString(trackings.size()));

        for (AbstractTracking tracking:trackings) {

            ids.put(tracking.getTrackingId(),ids.size()+1);

            Date now = new Date();

            long time = tracking.getMeetTime().getTime() - millis;

            if (time < now.getTime())
            {
                continue;
            }



            Intent intent = new Intent(context, ActionReceiver.class);
            intent.putExtra("Action","Reminder");
            intent.putExtra(Constant.trackingId,tracking.getTrackingId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    (Integer) ids.get(tracking.getTrackingId()),intent,PendingIntent.FLAG_CANCEL_CURRENT);

//            SharedPreferences settings = getSharedPreferences("setting", MODE_PRIVATE);
//            int millis = settings.getInt("SuggestionFrequency",60)  * 1000;


            am.setExact(AlarmManager.RTC_WAKEUP,
                    time ,pendingIntent);
            
        }
    }
    public void removeAll() {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (AbstractTracking tracking : trackings) {

            int id = (int) ids.get(tracking.getTrackingId());

            Intent intent = new Intent(context, ActionReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    id,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            am.cancel(pendingIntent);


        }

        //reset ids.
        ids.clear();
        //clear trackings.
        trackings.clear();

    }
}
