package com.miles.foodtruck.service.workers;

import android.app.Activity;
import android.content.Context;

import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.DbManager;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;


//Called when remove tracking from tracking list
public class RemoveFromDbThread extends Thread {

    private AbstractTracking tracking;
    private Context context;

    public RemoveFromDbThread(AbstractTracking tracking, Context context){

        this.tracking = tracking;
        this.context = context;

    }

    @Override
    public void run() {

        //Remove from DbManger.
        DbManager.getSingletonInstance(context).saveOnRemoved(tracking);

        //runnable for UI update
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Helpers.callToast(Constant.RemovedMessage,context);

            }
        };

        //Run in Main thread.
        ((Activity)context).runOnUiThread(runnable);

    }
}
