package com.miles.foodtruck.View;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miles.foodtruck.Controller.PickerOnClickListener;
import com.miles.foodtruck.R;
import com.miles.foodtruck.Util.Constant;

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
        Button saveBtn = findViewById(R.id.save_button);

        switch (operation){
            case Constant.AddOperation:
                trackableTitle.setText(intent.getStringExtra(Constant.trackableName));
                break;
            case Constant.EditOperation:
                break;
            default:
                break;
        }

        datePicker.setOnClickListener(new PickerOnClickListener(datePicker, getSupportFragmentManager()));
        timePicker.setOnClickListener(new PickerOnClickListener(timePicker, getSupportFragmentManager()));


    }









    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
