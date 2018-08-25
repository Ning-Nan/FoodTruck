package com.miles.foodtruck.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.miles.foodtruck.Adapter.RecyclerAdapter;
import com.miles.foodtruck.Model.FoodTruck;
import com.miles.foodtruck.R;
import com.miles.foodtruck.Service.FileReader;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<FoodTruck> foodTrucks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        try {
            foodTrucks = FileReader.getTrackableList(getAssets().open(getString(R.string.truck_file_name)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initRecyclerView();



    }

    private void initRecyclerView(){

        mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(foodTrucks);
        mRecyclerView.setAdapter(mAdapter);

    }

}
