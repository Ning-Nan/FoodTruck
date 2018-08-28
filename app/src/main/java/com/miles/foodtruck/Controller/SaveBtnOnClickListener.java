package com.miles.foodtruck.Controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.miles.foodtruck.Service.TrackingService;
import com.miles.foodtruck.Util.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class SaveBtnOnClickListener implements View.OnClickListener {

    private Bundle bundle;
    private EditText dateText;
    private EditText timeText;
    private EditText titleText;

    private static final String LOG_TAG = TrackingService.class.getName();


    public SaveBtnOnClickListener(Bundle bundle, EditText titleText, EditText dateText, EditText timeText){

        this.bundle = bundle;
        this.titleText = titleText;
        this.dateText = dateText;
        this.timeText = timeText;

    }
    @Override
    public void onClick(View v) {



        Log.w("Test", titleText.getText().toString() );
        Log.w("Test", dateText.getText().toString());
        Log.w("Test", timeText.getText().toString());




        TrackingService trackingService = TrackingService.getSingletonInstance(v.getContext());
        Log.i(LOG_TAG, "Parsed File Contents:");
        trackingService.logAll();

        try
        {
            // 5 mins either side of 05/07/2018 1:05:00 PM
            // PRE: make sure tracking_data.txt contains valid matches
            // PRE: make sure device locale matches provided DateFormat (you can change either device settings or String param)
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
            String searchDate = "05/07/2018 1:06:00 PM";
            int searchWindow = 10; // +/- 5 mins
            Date date = dateFormat.parse(searchDate);
            List<TrackingService.TrackingInfo> matched = trackingService
                    .getTrackingInfoForTimeRange(date, searchWindow, 0);
            Log.i(LOG_TAG, String.format("Matched Query: %s, +/-%d mins", searchDate, searchWindow));
            trackingService.log(matched);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


    }
}
