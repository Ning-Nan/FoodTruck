package com.miles.foodtruck.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import com.miles.foodtruck.Adapter.RecyclerAdapter;
import com.miles.foodtruck.Controller.OnMenuItemSelect;
import com.miles.foodtruck.Controller.SpinnerListener;
import com.miles.foodtruck.Model.Abstract.AbstractTrackable;
import com.miles.foodtruck.R;
import com.miles.foodtruck.Model.TrackacbleManager;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AbstractTrackable> foodTrucks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle(R.string.trackable_list_title);
        initTrackable();
        initRecyclerView();
        initSpinner();


    }

    private void initRecyclerView(){

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(foodTrucks,null, this);
        mRecyclerView.setAdapter(mAdapter);

    }
    private void initTrackable(){

        try {
            foodTrucks = TrackacbleManager.readTrackableList(getResources().openRawResource(R.raw.food_truck_data));
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void initSpinner(){


        Spinner spinner = findViewById(R.id.truck_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TrackacbleManager.getCategories());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new SpinnerListener(foodTrucks,mAdapter));
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
}
