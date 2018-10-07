package com.miles.foodtruck.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.miles.foodtruck.adapter.RecyclerAdapter;
import com.miles.foodtruck.controller.OnMenuItemSelect;
import com.miles.foodtruck.controller.CategoriesSpinnerListener;
import com.miles.foodtruck.model.abstracts.AbstractTrackable;
import com.miles.foodtruck.model.TrackacbleManager;
import com.miles.foodtruck.service.ActionReceiver;
import com.miles.foodtruck.service.DbManager;
import com.miles.foodtruck.service.LocationService;
import com.miles.foodtruck.service.ReminderService;
import com.miles.foodtruck.service.workers.*;
import com.miles.foodtruck.R;
import com.miles.foodtruck.util.Constant;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AbstractTrackable> foodTrucks = new ArrayList<>();
    private LocationService locationService;
    private ActionReceiver actionReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle(R.string.trackable_list_title);

        //init broadcast receiver for network monitoring
        initReceiver();

        //Async task to perform database init and load models.
        DbinitAsyncTask dbinitAsyncTask = new DbinitAsyncTask(this);
        dbinitAsyncTask.execute();

        //Start to track device location.
        locationService = new LocationService(this);
        locationService.initLocation(getApplicationContext());

        //Start the suggestion
        initAlarmService();

    }

     public void initAlarmService()
    {
        //Init the suggestion
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ActionReceiver.class);
        intent.putExtra(Constant.Action,Constant.AlarmAction);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                Constant.SuggestionRequestCode,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        //get settings
        SharedPreferences settings = getSharedPreferences(Constant.SettingName, MODE_PRIVATE);
        long millis = settings.getInt(Constant.SuggestionFrequency,60)  * 1000;


        //set alarm.
        //Repeat alarm has delay. So set again when the suggestion showed.
        am.setExact(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + millis ,pendingIntent);


    }

    //init reminder after the tracking loaded.
    public void initReminder(){

        ReminderService reminderService = new ReminderService(getApplicationContext());
        reminderService.setAll();
    }


    //Init recyler View after the db load
    public void initRecyclerView(){

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(foodTrucks,null, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void initTrackable(){

        this.foodTrucks = TrackacbleManager.getTrackableList();
    }


    public void initSpinner(){

        Spinner spinner = (Spinner) findViewById(R.id.truck_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                TrackacbleManager.getCategories());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new CategoriesSpinnerListener(foodTrucks,mAdapter));
    }

    //init receiver for internet monitoring
    private void initReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        actionReceiver = new ActionReceiver();

        registerReceiver(actionReceiver,filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_trackings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return OnMenuItemSelect.onOptionsItemSelected(item,this);

    }

    //Request permission result.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //If accept, then init the location service.
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationService.initLocation(getApplicationContext());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregister receiver and close database
        unregisterReceiver(actionReceiver);
        DbManager.getSingletonInstance(getApplicationContext()).close();
    }
}
