package com.miles.foodtruck.service.workers;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.DbManager;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;

//change to intend service
public class SaveToDbThread extends Thread{

    private AbstractTracking tracking;
    private AppCompatActivity activity;
    private Context context;

    public SaveToDbThread(AbstractTracking tracking, AppCompatActivity activity, Context context){

        this.tracking = tracking;
        this.activity = activity;
        this.context = context;

    }
    @Override
    public void run() {

        DbManager.getSingletonInstance(activity.getApplicationContext()).saveOnAdded(tracking);

    }
}
