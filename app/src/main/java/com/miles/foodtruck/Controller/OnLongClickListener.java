package com.miles.foodtruck.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.model.Tracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.service.workers.RemoveFromDbThread;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.view.MapsActivity;

/*
    Reusable Long Click listener for both trackales and trackings
 */
public class OnLongClickListener implements View.OnLongClickListener{

    private String trackableId;
    private AbstractTracking tracking;
    private Activity activity;

    public OnLongClickListener(String trackableId, AbstractTracking tracking,Activity activity){

        this.trackableId = trackableId;
        this.tracking = tracking;
        this.activity = activity;

    }

    //Do operation based on different data passed in.
    @Override
    public boolean onLongClick(View v) {

        //Case called from Trackable list. (because no tracking passed in)
        if (tracking == null){

            //Start Map activity, pass that trackable Id.
            Intent intent = new Intent(activity, MapsActivity.class);
            intent.putExtra(Constant.trackableId,trackableId);
            activity.startActivity(intent);

        }
        else
        {   //Case called from Tracking List. Remove item.
            TrackingManager.getSingletonInstance().remove((Tracking) tracking);

            //Also remove from database, use another thread.
            RemoveFromDbThread thread = new RemoveFromDbThread(tracking,v.getContext());
            thread.start();


        }

        return true;
    }
}
