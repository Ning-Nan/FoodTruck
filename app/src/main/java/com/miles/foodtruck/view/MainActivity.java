package com.miles.foodtruck.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.miles.foodtruck.service.LocationService;
import com.miles.foodtruck.service.workers.*;
import com.miles.foodtruck.R;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AbstractTrackable> foodTrucks = new ArrayList<>();
    private LocationService locationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle(R.string.trackable_list_title);

        //Async task to perform database init and load models.
        DbinitAsyncTask dbinitAsyncTask = new DbinitAsyncTask(this);
        dbinitAsyncTask.execute();

        //Start to track device location.
        locationService = new LocationService(this);
        locationService.initLocation(getApplicationContext());

        //Start the suggestion
//        SuggestionAsyncTask suggestionAsyncTask = new SuggestionAsyncTask(this);
//        suggestionAsyncTask.execute();
        initAlarmService();
    }

    private void initAlarmService()
    {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), ActionReceiver.class);
        intent.putExtra("Action","Alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                3,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences settings = getSharedPreferences("setting", MODE_PRIVATE);
        int millis = settings.getInt("SuggestionFrequency",1) * 60 * 1000;


        am.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + millis , millis,pendingIntent);

    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationService.initLocation(getApplicationContext());
                    //Start the suggestion
                    SuggestionAsyncTask suggestionAsyncTask = new SuggestionAsyncTask(getApplicationContext());
                    suggestionAsyncTask.execute();


                }
            }
        }
    }
}
