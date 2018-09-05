package com.miles.foodtruck.View;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.miles.foodtruck.Controller.OnMenuItemSelect;
import com.miles.foodtruck.Controller.PickerOnClickListener;
import com.miles.foodtruck.Controller.SaveBtnOnClickListener;
import com.miles.foodtruck.Controller.TimeSlotSpinner;
import com.miles.foodtruck.R;
import com.miles.foodtruck.Service.TrackingService;
import com.miles.foodtruck.Util.Constant;
import com.miles.foodtruck.Util.Helpers;
import java.util.Date;
import java.util.List;

public class ModifyTrackingActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_tracking_activity);
        setTitle(R.string.modify_tracking_title);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        initView();

    }

    //Init the UI with passed in values.
    private void initView(){

        String operation = intent.getStringExtra(Constant.operation);
        TextView trackableTitle = (TextView) findViewById(R.id.trackable_name);
        EditText datePicker = (EditText) findViewById(R.id.date_picker);
        EditText timePicker = (EditText) findViewById(R.id.time_picker);
        EditText trackingTitle = (EditText) findViewById(R.id.input_tracking_name);
        Button saveBtn = (Button) findViewById(R.id.save_button);

        trackableTitle.setText(intent.getStringExtra(Constant.trackableName));

        //If is editing, should get values that already set
        if (operation.equals(Constant.EditOperation))
        {
            trackingTitle.setText(intent.getStringExtra(Constant.trackingTitle));
            datePicker.setText(intent.getStringExtra(Constant.dateText));
            timePicker.setText(intent.getStringExtra(Constant.timeText));

        }
        datePicker.setOnClickListener(new PickerOnClickListener(datePicker, getSupportFragmentManager()));
        timePicker.setOnClickListener(new PickerOnClickListener(timePicker, getSupportFragmentManager()));

        SaveBtnOnClickListener saveBtnOnClickListener= new SaveBtnOnClickListener(intent.getExtras(),trackingTitle,
                datePicker,timePicker,this);
        saveBtn.setOnClickListener(saveBtnOnClickListener);
        TextView location = (TextView) findViewById(R.id.location_text);
        initSpinner(saveBtnOnClickListener,location);


    }

    //init the time slots. Passed in listener used for updating the selected time slot.
    private void initSpinner(SaveBtnOnClickListener saveBtnOnClickListener, TextView location){

        Spinner spinner = (Spinner) findViewById(R.id.time_slot_spinner);

        List<TrackingService.TrackingInfo> availables = Helpers.getTrackingInfoForTrackable(Integer.toString(intent.getExtras().getInt(Constant.trackableId)),new Date(),this,true);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Helpers.getSpinnerItem(availables));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        TimeSlotSpinner timeSlotSpinner = new TimeSlotSpinner(availables,saveBtnOnClickListener,location);
        spinner.setOnItemSelectedListener(timeSlotSpinner);

        spinner.setSelection(timeSlotSpinner.initSelected(intent.getStringExtra(Constant.trackingId)));

    }










    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return OnMenuItemSelect.onOptionsItemSelected(item,this);
    }



}
