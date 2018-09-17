package com.miles.foodtruck.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.miles.foodtruck.model.abstracts.AbstractTrackable;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.model.TrackacbleManager;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.view.ModifyTrackingActivity;
import java.text.SimpleDateFormat;


/*
    Reusable button listener for both tracking and trackables.
 */
public class AddOrEditTrackingListener implements View.OnClickListener {

    private AbstractTrackable foodTruck;
    private AbstractTracking tracking;
    private Context context;



    public AddOrEditTrackingListener(AbstractTrackable foodTruck, AbstractTracking tracking, Context context){
        this.foodTruck = foodTruck;
        this.tracking = tracking;
        this.context = context;
    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(context, ModifyTrackingActivity.class);

        //case edit tracking (From Tracking List)
        //pass values that UI need and also the controllers need.
        if (foodTruck == null){

            intent.putExtra(Constant.operation, Constant.EditOperation);
            intent.putExtra(Constant.trackableId, tracking.getTrackableId());
            intent.putExtra(Constant.trackableName, TrackacbleManager.getTrackable(tracking.getTrackableId()).getName());
            intent.putExtra(Constant.trackingTitle, tracking.getTitle());
            intent.putExtra(Constant.trackingId, tracking.getTrackingId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            intent.putExtra(Constant.dateText,dateFormat.format(tracking.getMeetTime()));
            dateFormat = new SimpleDateFormat("h:mm:ss aa");
            intent.putExtra(Constant.timeText,dateFormat.format(tracking.getMeetTime()));

        }

        //case add new
        else
        {
            intent.putExtra(Constant.operation, Constant.AddOperation);
            intent.putExtra(Constant.trackableId,foodTruck.getId());
            intent.putExtra(Constant.trackableName,foodTruck.getName());
        }

        context.startActivity(intent);


    }
}
