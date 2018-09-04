package com.miles.foodtruck.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import com.miles.foodtruck.Model.Abstract.AbstractTracking;
import com.miles.foodtruck.Model.Tracking;
import com.miles.foodtruck.Model.TrackingManager;
import com.miles.foodtruck.Service.TrackingService;
import com.miles.foodtruck.Util.Constant;
import com.miles.foodtruck.Util.Helpers;
import java.util.Date;


public class SaveBtnOnClickListener implements View.OnClickListener {

    private Bundle bundle;
    private EditText dateText;
    private EditText timeText;
    private EditText titleText;
    private AppCompatActivity activity;
    private TrackingService.TrackingInfo trackingInfoSlected;



    public SaveBtnOnClickListener(Bundle bundle, EditText titleText, EditText dateText, EditText timeText, AppCompatActivity activity){

        this.bundle = bundle;
        this.titleText = titleText;
        this.dateText = dateText;
        this.timeText = timeText;
        this.activity = activity;

    }
    @Override
    public void onClick(View v) {

        //Check no filed is empty
        if (titleText.getText().toString().equals("") ||dateText.getText().toString().equals("")||timeText.getText().toString().equals("") )
        {
            Helpers.callToast(Constant.EmptyMessage,v.getContext());
            return;
        }

        //Date from pickers.
        String searchDate = dateText.getText().toString() + " " + timeText.getText().toString();
        Date dateSelected = Helpers.strToDate(searchDate);

        //Check if the date is in the selected time slot
        if (checkDateTimeInRange(dateSelected) == false)
        {
            Helpers.callToast(Constant.WrongTimeMessage, v.getContext());
            return;
        }

        //New tracking
        AbstractTracking tracking = new Tracking();
        tracking.setTitle(titleText.getText().toString());
        tracking.setMeetTime(dateSelected);
        tracking.setMeetLocation(Double.toString(trackingInfoSlected.latitude) + "," + Double.toString(trackingInfoSlected.longitude));
        tracking.setTargetStartTime(trackingInfoSlected.date);
        tracking.setTargetEndTime(Helpers.caculateEndTime(trackingInfoSlected.date,trackingInfoSlected.stopTime));
        tracking.setTrackableId(bundle.getInt(Constant.trackableId));

        TrackingManager trackingManager = TrackingManager.getSingletonInstance();

        //Edit - Set same id
        //Add - generate random string id
        if (bundle.getString(Constant.trackingId) !=null)
        {
            tracking.setTrackingId(bundle.getString(Constant.trackingId));
        }
        else{

            tracking.setTrackingId(Helpers.random(5,trackingManager));

        }

        trackingManager.addToTracking(tracking);
        Helpers.callToast(Constant.SavedMessage, v.getContext());
        activity.finish();

    }


    //Check if the selected date in that time slot from spinner
    private boolean checkDateTimeInRange(Date slectedDate){

        Date start = trackingInfoSlected.date;
        Date end = Helpers.caculateEndTime(start,trackingInfoSlected.stopTime);

        if ( (slectedDate.after(start) && slectedDate.before(end)) || slectedDate.getTime() == start.getTime()||
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
