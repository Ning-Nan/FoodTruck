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








    }
}
