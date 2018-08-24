package com.miles.foodtruck.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.miles.foodtruck.Adapter.RecyclerAdapter;
import com.miles.foodtruck.Model.FoodTruck;
import com.miles.foodtruck.R;
import com.miles.foodtruck.Service.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mData;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initRecyclerView();
        ArrayList<FoodTruck> foodTrucks = new ArrayList<>();

        try {
            foodTrucks = FileReader.getTrackableList(getAssets().open(String.valueOf(R.string.truck_file_name)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.w("Test", foodTrucks.toString());

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
        mAdapter = new RecyclerAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initData(){
        mData = new ArrayList<>();
        for(int i=0;i<20;i++){
            mData.add("Item "+i);
        }
    }
}
