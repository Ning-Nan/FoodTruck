package com.miles.foodtruck.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.miles.foodtruck.R;
import com.miles.foodtruck.controller.SuggestionNotification;
import com.miles.foodtruck.model.Tracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.workers.SaveIntentService;
import com.miles.foodtruck.service.workers.SaveToDbThread;
import com.miles.foodtruck.service.workers.SuggestionAsyncTask;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

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
                    dismissNotification(context,intent);
                    break;
                case"Add":
                    addTracking(context);
                    break;

                case "Alarm":
                    setAlarm(context);
                    break;

                case "Reminder":
                    showReminder(context, intent);
                    break;

                case "Cancel":
                    cancelReminder(context, intent);
                    break;
                case "Again":
                    againReminder(context, intent);
                    break;
            }

        }




    }

    private void againReminder(Context context, Intent intent){

        SharedPreferences settings = context.getSharedPreferences("setting", MODE_PRIVATE);
        long millis = settings.getInt("RemindBefore",60)  * 1000;
        long remindAgain = settings.getInt("RemindAgain",60)  * 1000;


        String trackingId = intent.getStringExtra(Constant.trackingId);

        AbstractTracking tracking = TrackingManager.getSingletonInstance().get(trackingId);

        Date now = new Date();

        long time = now.getTime() + remindAgain;

        if (time > tracking.getMeetTime().getTime())
        {
            return;
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(context, ActionReceiver.class);
        intent1.putExtra("Action","Reminder");
        intent1.putExtra(Constant.trackingId,tracking.getTrackingId());
//        intent1.putExtra("ReminderTime",time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                (Integer) ReminderService.ids.get(tracking.getTrackingId()),intent1,PendingIntent.FLAG_CANCEL_CURRENT);

        now.setTime(time);

        Log.w("Reset alram", now.toString());
        am.setExact(AlarmManager.RTC_WAKEUP,
                time ,pendingIntent);


        NotificationManagerCompat.from(context).cancel(
                (Integer) ReminderService.ids.get(tracking.getTrackingId()));

    }

    private void cancelReminder(Context context, Intent intent){

        int id = (int) ReminderService.ids.get(intent.getStringExtra(Constant.trackingId));
        NotificationManagerCompat.from(context).cancel(id);

        ReminderService reminderService = new ReminderService(context);
        reminderService.removeAll();



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

    private void dismissNotification(Context context,Intent intent){

        if (intent.getStringExtra(Constant.trackingId) == null)
        {
            int notificationId = -4;
            NotificationManagerCompat.from(context).cancel(notificationId);
        }

        else{

            int id = (int) ReminderService.ids.get(intent.getStringExtra(Constant.trackingId));
            NotificationManagerCompat.from(context).cancel(id);



        }

    }

    private void setAlarm(Context context){
        SuggestionAsyncTask suggestionAsyncTask = new SuggestionAsyncTask(context);
        suggestionAsyncTask.execute();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent1 = new Intent(context, ActionReceiver.class);
        intent1.putExtra("Action","Alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                3,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences settings = context.getSharedPreferences("setting", MODE_PRIVATE);
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

        //SET DEFAULT VALUE HERE.
        tracking.setTravelTime(trackableInfo.duration);

        TrackingManager trackingManager = TrackingManager.getSingletonInstance();


        tracking.setTrackingId(Helpers.random(5,trackingManager));

        trackingManager.addToTracking(tracking);

        ReminderService reminderService = new ReminderService(context);
        reminderService.setAll();

        Intent intent = new Intent(context, SaveIntentService.class);

        intent.putExtra(Constant.trackingId,tracking.getTrackingId());

        context.startService(intent);

        int notificationId = -4;
        NotificationManagerCompat.from(context).cancel(notificationId);

        Helpers.callToast(Constant.SavedMessage,context);

//        //modify database in separate thread.
//        SaveToDbThread thread = new SaveToDbThread(tracking,context);
//        thread.start();
    }

    private void showReminder(Context context, Intent intent){

        String trackingId = intent.getStringExtra(Constant.trackingId);
        int id = (int) ReminderService.ids.get(trackingId);

        Log.w("Remainder","trackingId:" + trackingId);
        Log.w("Remainder", "id: " + Integer.toString(id));


        AbstractTracking tracking =
                TrackingManager.getSingletonInstance().get(trackingId);


        String str =String.format("Coming Tracking!\nTracking Name: %s\nMeet Time: %s\nMeet Location: %s",
                tracking.getTitle(),tracking.getMeetTime().toString(),tracking.getMeetLocation()) ;

        Intent dismissIntent = new Intent(context,ActionReceiver.class);
        dismissIntent.putExtra("Action","Dismiss");
        dismissIntent.putExtra(Constant.trackingId,trackingId);
        PendingIntent pendingDismissIntent = PendingIntent.getBroadcast(context,-3,dismissIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(context,ActionReceiver.class);
        cancelIntent.putExtra("Action","Cancel");
        cancelIntent.putExtra(Constant.trackingId,trackingId);
        PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(context,-5,cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent againIntent = new Intent(context,ActionReceiver.class);
        againIntent.putExtra("Action","Again");
        againIntent.putExtra(Constant.trackingId,trackingId);

        PendingIntent pendingAgainIntent = PendingIntent.getBroadcast(context,-6,againIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        SharedPreferences settings = context.getSharedPreferences("setting",MODE_PRIVATE);
        String againTitle = String.format("Remind in %ssecs",
                Integer.toString(settings.getInt("RemindAgain",60)));


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Tracking Reminder")
                .setContentText(tracking.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(str))
                .addAction(R.drawable.ic_launcher_background,"Dismiss",pendingDismissIntent)
                .addAction(R.drawable.ic_launcher_background,"Cancel",pendingCancelIntent)
                .addAction(R.drawable.ic_launcher_background,againTitle,pendingAgainIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(id,mBuilder.build());


    }
}
