package com.miles.foodtruck.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.model.Tracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.service.TrackingService;
import com.miles.foodtruck.service.workers.SaveIntentService;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;
import java.util.Date;


public class SaveBtnOnClickListener implements View.OnClickListener {

    private Bundle bundle;
    private EditText dateText;
    private EditText timeText;
    private EditText titleText;
    private AppCompatActivity activity;
    private TrackingService.TrackingInfo trackingInfoSlected;



    public SaveBtnOnClickListener(Bundle bundle, EditText titleText,
                                  EditText dateText, EditText timeText, AppCompatActivity activity){

        this.bundle = bundle;
        this.titleText = titleText;
        this.dateText = dateText;
        this.timeText = timeText;
        this.activity = activity;

    }
    @Override
    public void onClick(View v) {


        //Check no filed is empty
        if (titleText.getText().toString().equals("") ||dateText.getText().toString().equals("")
                ||timeText.getText().toString().equals("") )
        {
            Helpers.callToast(Constant.EmptyMessage,v.getContext());
            return;
        }

        //Date from pickers.
        String searchDate = dateText.getText().toString() + " " +
                timeText.getText().toString();
        Date dateSelected = Helpers.strToDate(searchDate);

        //Check if the date is in the selected time slot
        if (checkDateTimeInRange(dateSelected) == false)
        {
            Helpers.callToast(Constant.WrongTimeMessage, v.getContext());
            return;
        }

        //Set up new tracking based on UI input and passed in values.
        AbstractTracking tracking = new Tracking();
        tracking.setTitle(titleText.getText().toString());
        tracking.setMeetTime(dateSelected);
        tracking.setMeetLocation(Double.toString(trackingInfoSlected.latitude) + "," +
                Double.toString(trackingInfoSlected.longitude));
        tracking.setTargetStartTime(trackingInfoSlected.date);
        tracking.setTargetEndTime(Helpers.caculateEndTime(trackingInfoSlected.date,
                trackingInfoSlected.stopTime));
        tracking.setTrackableId(bundle.getInt(Constant.trackableId));


        TrackingManager trackingManager = TrackingManager.getSingletonInstance();

        //Edit case - Set same id
        if (bundle.getString(Constant.trackingId) !=null)
        {
            tracking.setTrackingId(bundle.getString(Constant.trackingId));
        }
        //Add case - generate random string id
        else{

            tracking.setTrackingId(Helpers.random(5,trackingManager));

        }


        trackingManager.addToTracking(tracking);

        //Save to Database.
        //As this activity finish after button clicked.
        //Save here better than on stop. As may have not changed.
        Intent intent = new Intent(activity, SaveIntentService.class);
        intent.putExtra(Constant.trackingId,tracking.getTrackingId());
        activity.startService(intent);


        Helpers.callToast(Constant.SavedMessage,activity.getApplicationContext());
        activity.finish();
    }


    //Check if the selected date in that time slot from spinner
    private boolean checkDateTimeInRange(Date slectedDate){

        Date start = trackingInfoSlected.date;
        Date end = Helpers.caculateEndTime(start,trackingInfoSlected.stopTime);

        if ( (slectedDate.after(start) && slectedDate.before(end)) ||
                slectedDate.getTime() == start.getTime()||
                slectedDate.getTime() == end.getTime())
        {
            return true;
        }
        return false;

    }


    //Used by spinner to update the selected time slot
    public void setTimeSelected(TrackingService.TrackingInfo trackingInfo){

        trackingInfoSlected = trackingInfo;

    }



}
