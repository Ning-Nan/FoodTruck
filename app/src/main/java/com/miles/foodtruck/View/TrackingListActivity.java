package com.miles.foodtruck.view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import com.miles.foodtruck.adapter.RecyclerAdapter;
import com.miles.foodtruck.controller.OnMenuItemSelect;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.R;

import java.util.ArrayList;

public class TrackingListActivity extends AppCompatActivity {
    private ArrayList<AbstractTracking> trackings;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TrackingManager trackingManager = TrackingManager.getSingletonInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.trackings);
        setTitle(R.string.tracking_list_title);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initTracking();
        initRecyclerView();
    }


    @Override
    protected void onResume() {
        //To update the current location in that view.
        //Since the adapter is already binded.
        //The data change needs to be passed again to the adapter.
        //Maybe use update? notify dataset changed?
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return OnMenuItemSelect.onOptionsItemSelected(item,this);
    }



    private void initTracking(){

        trackings = trackingManager.getAll();
    }

    private void initRecyclerView(){

        mRecyclerView = (RecyclerView) findViewById(R.id.traking_recycler_view);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new RecyclerAdapter(null,trackings, this);
        trackingManager.addObserver(mAdapter);
        mRecyclerView.setAdapter(mAdapter);

    }


}
