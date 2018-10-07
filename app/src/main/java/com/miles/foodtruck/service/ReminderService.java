package com.miles.foodtruck.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.util.Constant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ReminderService {

    private Context context;
    //Ids to keep ref for notifications and reminders.
    public static HashMap ids  = new HashMap();
    public static ArrayList<AbstractTracking> trackings = new ArrayList<>();

    public ReminderService(Context context)
    {
        this.context = context;

    }

    //Set all reminders.
    public void setAll(){

        //Before set, clear all old refs.
        if (ids.size() != 0)
        {
            removeAll();

        }

        //get Settings
        SharedPreferences settings = context.getSharedPreferences(Constant.SettingName, MODE_PRIVATE);
        long millis = settings.getInt(Constant.RemindBefore,60)  * 1000;

        //clone so we keep the copy. not original one.
        trackings = (ArrayList<AbstractTracking>) TrackingManager.getSingletonInstance().getAll().clone();

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //for each trackings in memory, add reminders for it.
        for (AbstractTracking tracking:trackings) {

            //save ref.
            ids.put(tracking.getTrackingId(),ids.size()+1);

            Date now = new Date();

            long time = tracking.getMeetTime().getTime() - millis;

            //if already missed this tracking, dismiss
            if (time < now.getTime())
            {
                continue;
            }

            //Send the pending intend when reached the set time
            Intent intent = new Intent(context, ActionReceiver.class);
            intent.putExtra(Constant.Action,Constant.ReminderAction);
            intent.putExtra(Constant.trackingId,tracking.getTrackingId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    (Integer) ids.get(tracking.getTrackingId()),intent,PendingIntent.FLAG_CANCEL_CURRENT);

            //set alarm
            am.setExact(AlarmManager.RTC_WAKEUP,
                    time ,pendingIntent);
            
        }
    }
    public void removeAll() {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //For every tracking, cancel the reminders.
        for (AbstractTracking tracking : trackings) {

            //using the same id to cancel.
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
