package com.miles.foodtruck.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.miles.foodtruck.controller.SuggestionNotification;
import com.miles.foodtruck.service.workers.SuggestionAsyncTask;

public class ActionReceiver extends BroadcastReceiver
{



    @Override
    public void onReceive(Context context, Intent intent) {

        String action=intent.getStringExtra("Action");
        if(action.equals("Next")){
            nextNotification();
        }
        else if (action.equals("Dismiss")){

            dismissNotification(context);
        }
        else if (action.equals("Alarm"))
        {
            SuggestionAsyncTask suggestionAsyncTask = new SuggestionAsyncTask(context);
            suggestionAsyncTask.execute();
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
}
