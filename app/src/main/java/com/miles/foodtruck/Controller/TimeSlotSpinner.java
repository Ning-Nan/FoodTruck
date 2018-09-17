package com.miles.foodtruck.controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.service.TrackingService;
import com.miles.foodtruck.util.Helpers;

import java.util.Date;
import java.util.List;

public class TimeSlotSpinner implements AdapterView.OnItemSelectedListener {

    private List<TrackingService.TrackingInfo> trackingInfos;
    private SaveBtnOnClickListener saveBtnOnClickListener;
    private TextView location;

    public TimeSlotSpinner(List<TrackingService.TrackingInfo> trackingInfos,
                           SaveBtnOnClickListener saveBtnOnClickListener,
                           TextView location){

        this.trackingInfos = trackingInfos;
        this.saveBtnOnClickListener = saveBtnOnClickListener;
        this.location = location;

    }

    //After selecting the time slot, update the text for location
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        saveBtnOnClickListener.setTimeSelected(trackingInfos.get(position));
        location.setText(Double.toString(trackingInfos.get(position).latitude)
                + "," + Double.toString(trackingInfos.get(position).longitude));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //For edit case, slot must be setup correctly
    public int initSelected(String trackingId){

        if (trackingId != null){

            AbstractTracking tracking = TrackingManager.getSingletonInstance().get(trackingId);
            Date meetDate = tracking.getMeetTime();

            for (int i = 0; i < trackingInfos.size(); i++) {

                Date option = trackingInfos.get(i).date;
                Date end = Helpers.caculateEndTime(option,trackingInfos.get(i).stopTime);

                if(meetDate.before(option) || meetDate.after(end))
                {
                    continue;
                }

                return i;

            }
        }

        return 0;


    }
}
