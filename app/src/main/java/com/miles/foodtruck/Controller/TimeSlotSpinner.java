package com.miles.foodtruck.Controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.miles.foodtruck.Service.TrackingService;
import java.util.List;

public class TimeSlotSpinner implements AdapterView.OnItemSelectedListener {

    private List<TrackingService.TrackingInfo> trackingInfos;
    private SaveBtnOnClickListener saveBtnOnClickListener;
    private TextView location;

    public TimeSlotSpinner(List<TrackingService.TrackingInfo> trackingInfos, SaveBtnOnClickListener saveBtnOnClickListener, TextView location){

        this.trackingInfos = trackingInfos;
        this.saveBtnOnClickListener = saveBtnOnClickListener;
        this.location = location;

    }

    //After selecting the time slot, update the text for location
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        saveBtnOnClickListener.setTimeSelected(trackingInfos.get(position));
        location.setText(Double.toString(trackingInfos.get(position).latitude) + "," + Double.toString(trackingInfos.get(position).longitude));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
