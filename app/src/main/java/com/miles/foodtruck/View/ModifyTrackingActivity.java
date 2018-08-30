package com.miles.foodtruck.View;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miles.foodtruck.Controller.OnMenuItemSelect;
import com.miles.foodtruck.Controller.PickerOnClickListener;
import com.miles.foodtruck.Controller.SaveBtnOnClickListener;
import com.miles.foodtruck.R;
import com.miles.foodtruck.Util.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyTrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_tracking_activity);
        setTitle(R.string.modify_tracking_title);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        initView(intent);

    }


    private void initView(Intent intent){

        String operation = intent.getStringExtra(Constant.operation);
        TextView trackableTitle = findViewById(R.id.trackable_name);
        EditText datePicker = findViewById(R.id.date_picker);
        EditText timePicker = findViewById(R.id.time_picker);
        EditText trackingTitle = findViewById(R.id.input_tracking_name);
        Button saveBtn = findViewById(R.id.save_button);

        trackableTitle.setText(intent.getStringExtra(Constant.trackableName));

        if (operation.equals(Constant.EditOperation))
        {
            trackingTitle.setText(intent.getStringExtra(Constant.trackingTitle));
            datePicker.setText(intent.getStringExtra(Constant.dateText));
            timePicker.setText(intent.getStringExtra(Constant.timeText));

        }
        datePicker.setOnClickListener(new PickerOnClickListener(datePicker, getSupportFragmentManager()));
        timePicker.setOnClickListener(new PickerOnClickListener(timePicker, getSupportFragmentManager()));
        saveBtn.setOnClickListener(new SaveBtnOnClickListener(intent.getExtras(),trackingTitle,
                datePicker,timePicker,this));


    }









    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return OnMenuItemSelect.onOptionsItemSelected(item,this);
    }



}
