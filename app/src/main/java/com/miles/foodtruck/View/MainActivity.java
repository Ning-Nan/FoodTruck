package com.miles.foodtruck.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import com.miles.foodtruck.Adapter.RecyclerAdapter;
import com.miles.foodtruck.Controller.SpinnerListener;
import com.miles.foodtruck.Model.Abstract.Trackable;
import com.miles.foodtruck.R;
import com.miles.foodtruck.Model.TrakacbleManager;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Trackable> foodTrucks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initTrackable();
        initRecyclerView();
        initSpinner();



    }

    private void initRecyclerView(){

        mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new RecyclerAdapter(foodTrucks);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initTrackable(){

        try {
            foodTrucks = TrakacbleManager.readTrackableList(getAssets().open(getString(R.string.truck_file_name)));
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void initSpinner(){


        Spinner spinner = findViewById(R.id.truck_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TrakacbleManager.getCategories());

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
}
