package com.miles.foodtruck.service.workers;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.service.DbManager;
import com.miles.foodtruck.service.ReminderService;
import com.miles.foodtruck.util.Constant;

public class SaveIntentService extends IntentService {


    public SaveIntentService() {
        super("SaveIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String trackingId = intent.getStringExtra(Constant.trackingId);


        AbstractTracking tracking = TrackingManager.getSingletonInstance().get(trackingId);

        DbManager.getSingletonInstance(this).saveOnAdded(tracking);

        ReminderService reminderService = new ReminderService(this);
        reminderService.setAll();


    }
}
