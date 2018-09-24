package com.miles.foodtruck.service.Workers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.DbManager;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;


public class RemoveFromDbThread extends Thread {

    private AbstractTracking tracking;
    private Context context;

    public RemoveFromDbThread(AbstractTracking tracking, Context context){

        this.tracking = tracking;
        this.context = context;

    }

    @Override
    public void run() {

        DbManager.getSingletonInstance(context).saveOnRemoved(tracking);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Helpers.callToast(Constant.RemovedMessage,context);

            }
        };

        ((Activity)context).runOnUiThread(runnable);

    }
}
