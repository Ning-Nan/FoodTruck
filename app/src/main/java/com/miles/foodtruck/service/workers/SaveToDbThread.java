package com.miles.foodtruck.service.workers;

import android.support.v7.app.AppCompatActivity;

import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.DbManager;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;

public class SaveToDbThread extends Thread{

    private AbstractTracking tracking;
    private AppCompatActivity activity;

    public SaveToDbThread(AbstractTracking tracking, AppCompatActivity activity){

        this.tracking = tracking;
        this.activity = activity;

    }
    @Override
    public void run() {

        DbManager.getSingletonInstance(activity.getApplicationContext()).saveOnAdded(tracking);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Helpers.callToast(Constant.SavedMessage,activity.getApplicationContext());

                activity.finish();

            }
        };

        activity.runOnUiThread(runnable);

    }
}
