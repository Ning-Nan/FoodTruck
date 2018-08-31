package com.miles.foodtruck.Controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.miles.foodtruck.Model.Abstract.AbstractTracking;
import com.miles.foodtruck.Model.Tracking;
import com.miles.foodtruck.Model.TrackingManager;
import com.miles.foodtruck.Service.TrackingService;
import com.miles.foodtruck.Util.Constant;
import com.miles.foodtruck.Util.Helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class SaveBtnOnClickListener implements View.OnClickListener {

    private Bundle bundle;
    private EditText dateText;
    private EditText timeText;
    private EditText titleText;
    private Date dateSelected;
    private AppCompatActivity activity;



    public SaveBtnOnClickListener(Bundle bundle, EditText titleText, EditText dateText, EditText timeText, AppCompatActivity activity){

        this.bundle = bundle;
        this.titleText = titleText;
        this.dateText = dateText;
        this.timeText = timeText;
        this.activity = activity;

    }
    @Override
    public void onClick(View v) {


        if (titleText.getText().toString().equals("") ||dateText.getText().toString().equals("")||timeText.getText().toString().equals("") )
        {
            Helpers.callToast(Constant.EmptyMessage,v.getContext());
            return;
        }

        String searchDate = dateText.getText().toString() + " " + timeText.getText().toString();

        dateSelected = Helpers.strToDate(searchDate);

        TrackingService.TrackingInfo trackingInfo = checkDateTimeInRange(v.getContext());

        if (trackingInfo == null)
        {
            Helpers.callToast(Constant.WrongTimeMessage, v.getContext());
            return;
        }

        AbstractTracking tracking = new Tracking();

        tracking.setTitle(titleText.getText().toString());
        tracking.setMeetTime(dateSelected);
        tracking.setMeetLocation(Double.toString(trackingInfo.latitude) + "," + Double.toString(trackingInfo.longitude));
        tracking.setTargetStartTime(trackingInfo.date);
        tracking.setTargetEndTime(Helpers.caculateEndTime(trackingInfo.date,trackingInfo.stopTime));
        tracking.setTrackableId(bundle.getInt(Constant.trackableId));

        TrackingManager trackingManager = TrackingManager.getSingletonInstance();

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


    private TrackingService.TrackingInfo checkDateTimeInRange(Context context){

        List<TrackingService.TrackingInfo> matched = Helpers.getTrackingInfoForTrackable(Integer.toString(bundle.getInt(Constant.trackableId)),dateSelected,context,true);

        for (int i = 0; i < matched.size(); i++) {
            Date start = matched.get(i).date;
            Date end = Helpers.caculateEndTime(start,matched.get(i).stopTime);

            if ( (dateSelected.after(start) && dateSelected.before(end)) || dateSelected.getTime() == start.getTime()||
                    dateSelected.getTime() == end.getTime())
            {
                return matched.get(i);
            }


        }

        return null;

    }

    private boolean checkTrackableId (TrackingService.TrackingInfo trackingInfo){

        if (trackingInfo.trackableId != bundle.getInt(Constant.trackableId))
        {
            return false;
        }
        return true;
    }



}
