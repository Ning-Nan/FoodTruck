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

import com.miles.foodtruck.R;
import com.miles.foodtruck.controller.SuggestionNotification;
import com.miles.foodtruck.model.Tracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.workers.SaveIntentService;
import com.miles.foodtruck.service.workers.SuggestionAsyncTask;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;
import java.util.Date;
import static android.content.Context.MODE_PRIVATE;

public class ActionReceiver extends BroadcastReceiver
{
    //Network status to prevent jump out suggestion when entering app with internet on.
    public static int networkStatus = -1;
    //Currently operating suggestion in Notification area.
    public static SuggestionAsyncTask.TrackableInfo trackableInfo;


    @Override
    public void onReceive(Context context, Intent intent) {

        //Only the internet monitoring action is set.
        //No action set for other intent send to BroadcastReceiver.
        if (intent.getAction() != null)
        {
            //Double check the action.
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
            {
                //Get network status
                ConnectivityManager manager =
                        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();

                //has internet
                if (networkInfo != null)
                {
                    //-1 means just enter the app.
                    if (networkStatus  == -1)
                    {
                        networkStatus = 1;
                    }
                    //That means lost internet then have again.
                    //Do suggestion.
                    else
                    {
                        networkStatus = 1;
                        SuggestionAsyncTask suggestionAsyncTask = new SuggestionAsyncTask(context);
                        suggestionAsyncTask.execute();
                    }

                }
                //Lost internet.
                else
                {
                    networkStatus = 0;
                    Helpers.callToast("Lost Internet",context);
                }
            }
        }
        //Not internet change action.
        //Actions for notifications...etc
        else{
            String action=intent.getStringExtra(Constant.Action);

            switch (action){
                //Suggestion next
                case Constant.NextAction:
                    nextNotification();
                    break;

                //Notification close
                case Constant.DismissAction:
                    dismissNotification(context,intent);
                    break;
                //Suggestion add tracking
                case Constant.AddAction:
                    addTracking(context);
                    break;
                //Make Notification for Suggestion.
                case Constant.AlarmAction:
                    setAlarm(context);
                    break;
                //Remind notification
                case Constant.ReminderAction:
                    showReminder(context, intent);
                    break;
                //Remind notification cancel all following reminders.
                case Constant.CancelAction:
                    cancelReminder(context, intent);
                    break;
                //Remind me again in XX seconds.
                case Constant.AgainAction:
                    againReminder(context, intent);
                    break;
            }

        }




    }

    //When user click remind me in XX seconds in the notification
    private void againReminder(Context context, Intent intent){

        SharedPreferences settings = context.getSharedPreferences(Constant.SettingName, MODE_PRIVATE);

        //Value from user setting
        long remindAgain = settings.getInt(Constant.RemindAgain,60)  * 1000;

        //Get the tracking
        String trackingId = intent.getStringExtra(Constant.trackingId);
        AbstractTracking tracking = TrackingManager.getSingletonInstance().get(trackingId);

        Date now = new Date();
        long time = now.getTime() + remindAgain;

        //Set the alarm again based on the given time
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(context, ActionReceiver.class);
        intent1.putExtra(Constant.Action,Constant.ReminderAction);
        intent1.putExtra(Constant.trackingId,tracking.getTrackingId());

        //use the same id so the notification will not be mess.
        //refs are kept in the ReminderService class
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                (Integer) ReminderService.ids.get(tracking.getTrackingId()),intent1,
                PendingIntent.FLAG_CANCEL_CURRENT);

        //set alarm
        am.setExact(AlarmManager.RTC_WAKEUP,
                time ,pendingIntent);
        //Hide current notification after clicked.
        NotificationManagerCompat.from(context).cancel(
                (Integer) ReminderService.ids.get(tracking.getTrackingId()));

    }

    //Cancel all the reminders in the future(That is how I understand from the assignment)
    private void cancelReminder(Context context, Intent intent){

        //get id ref. Close this notification
        int id = (int) ReminderService.ids.get(intent.getStringExtra(Constant.trackingId));
        NotificationManagerCompat.from(context).cancel(id);

        //remove all alarm
        ReminderService reminderService = new ReminderService(context);
        reminderService.removeAll();



    }

    //Show next suggestion
    private void nextNotification(){
        if (SuggestionNotification.trackableInfos.size() <= SuggestionNotification.index + 1)
        {
            //case end of the Suggestion

        }
        else
        {
            //Show next suggestion
            SuggestionNotification.index += 1;
            SuggestionNotification.show();
        }
    }

    //Close the notification
    private void dismissNotification(Context context,Intent intent){

        //Case Close the suggestion Notification.
        if (intent.getStringExtra(Constant.trackingId) == null)
        {
            int notificationId = Constant.SuggestionNotificationCode;
            NotificationManagerCompat.from(context).cancel(notificationId);
        }

        else{

            //Case Reminder for tracking
            int id = (int) ReminderService.ids.get(intent.getStringExtra(Constant.trackingId));
            NotificationManagerCompat.from(context).cancel(id);

        }

    }

    //Make the suggestion Notification
    //And set for next suggestion time
    private void setAlarm(Context context){
        //Get suggestions and show
        SuggestionAsyncTask suggestionAsyncTask = new SuggestionAsyncTask(context);
        suggestionAsyncTask.execute();

        //Set for next suggestion time.
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(context, ActionReceiver.class);
        intent1.putExtra(Constant.Action,Constant.AlarmAction);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                Constant.SuggestionRequestCode,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        //Get setting
        SharedPreferences settings = context.getSharedPreferences(
                Constant.SettingName, MODE_PRIVATE);
        int millis = settings.getInt(Constant.SuggestionFrequency,60)  * 1000;

        //set alarm
        am.setExact(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + millis ,pendingIntent);
    }

    //Add showing tracking
    private void addTracking(Context context)
    {
        //get tracking from static value.
        //Set from Show next.
        AbstractTracking tracking = new Tracking();
        tracking.setTitle(trackableInfo.title);
        tracking.setMeetTime(trackableInfo.meetTime);
        tracking.setMeetLocation(trackableInfo.meetLocation);
        tracking.setTargetStartTime(trackableInfo.targetStartTime);
        tracking.setTargetEndTime(trackableInfo.targetEndTime);
        tracking.setTrackableId(trackableInfo.trackableId);
        tracking.setTravelTime(trackableInfo.duration);

        //get manager
        TrackingManager trackingManager = TrackingManager.getSingletonInstance();

        //Set random tracking id (Repeat checked.)
        tracking.setTrackingId(Helpers.random(5,trackingManager));

        //Add to memory
        trackingManager.addToTracking(tracking);

        //Reset the reminders.
        ReminderService reminderService = new ReminderService(context);
        reminderService.setAll();

        //Save to database
        Intent intent = new Intent(context, SaveIntentService.class);
        intent.putExtra(Constant.trackingId,tracking.getTrackingId());
        context.startService(intent);

        int notificationId = Constant.SuggestionNotificationCode;
        NotificationManagerCompat.from(context).cancel(notificationId);
        Helpers.callToast(Constant.SavedMessage,context);

    }

    //Show the upcoming tracking reminder
    private void showReminder(Context context, Intent intent){

        String trackingId = intent.getStringExtra(Constant.trackingId);
        int id = (int) ReminderService.ids.get(trackingId);

        //get this tracking information
        AbstractTracking tracking =
                TrackingManager.getSingletonInstance().get(trackingId);

        //Display string
        String str =String.format("Coming Tracking!\nTracking Name: %s\nMeet Time: %s\nMeet Location: %s",
                tracking.getTitle(),tracking.getMeetTime().toString(),tracking.getMeetLocation()) ;

        //set button actions
        Intent dismissIntent = new Intent(context,ActionReceiver.class);
        dismissIntent.putExtra(Constant.Action,Constant.DismissAction);
        dismissIntent.putExtra(Constant.trackingId,trackingId);
        PendingIntent pendingDismissIntent = PendingIntent.getBroadcast(
                context,Constant.DismissActionRequestCode,dismissIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(context,ActionReceiver.class);
        cancelIntent.putExtra(Constant.Action,Constant.CancelAction);
        cancelIntent.putExtra(Constant.trackingId,trackingId);
        PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(
                context,Constant.CancelRequestCode,cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent againIntent = new Intent(context,ActionReceiver.class);
        againIntent.putExtra(Constant.Action,Constant.AgainAction);
        againIntent.putExtra(Constant.trackingId,trackingId);
        PendingIntent pendingAgainIntent = PendingIntent.getBroadcast(
                context,Constant.AgainRequestCode,againIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        //get settings and button name
        SharedPreferences settings = context.getSharedPreferences(Constant.SettingName,MODE_PRIVATE);
        String againTitle = String.format("Remind in %ssecs",
                Integer.toString(settings.getInt(Constant.RemindAgain,60)));


        //Build the notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(Constant.TrackingReminder)
                .setContentText(tracking.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(str))
                .addAction(R.drawable.ic_launcher_background,Constant.DismissAction,pendingDismissIntent)
                .addAction(R.drawable.ic_launcher_background,Constant.CancelAction,pendingCancelIntent)
                .addAction(R.drawable.ic_launcher_background,againTitle,pendingAgainIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id,mBuilder.build());


    }
}
