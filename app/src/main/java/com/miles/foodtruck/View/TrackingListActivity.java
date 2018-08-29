package com.miles.foodtruck.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.miles.foodtruck.Adapter.RecyclerAdapter;
import com.miles.foodtruck.Model.Abstract.AbstractTracking;
import com.miles.foodtruck.Model.Tracking;
import com.miles.foodtruck.Model.TrackingManager;
import com.miles.foodtruck.R;

import java.util.ArrayList;

public class TrackingListActivity extends AppCompatActivity {
    private ArrayList<AbstractTracking> trackings;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.trackings);
        setTitle(R.string.tracking_list_title);
        initTracking();
        initRecyclerView();
    }

    private void initTracking(){

        TrackingManager trackingManager = TrackingManager.getSingletonInstance();

        trackings = trackingManager.getAll();
    }

    private void initRecyclerView(){

        mRecyclerView = findViewById(R.id.traking_recycler_view);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new RecyclerAdapter(null,trackings, this);
        mRecyclerView.setAdapter(mAdapter);

    }
}
