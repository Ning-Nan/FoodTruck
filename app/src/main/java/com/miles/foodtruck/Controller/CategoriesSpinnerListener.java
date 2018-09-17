package com.miles.foodtruck.controller;

import android.view.View;
import android.widget.AdapterView;
import com.miles.foodtruck.adapter.RecyclerAdapter;
import com.miles.foodtruck.model.abstracts.AbstractTrackable;
import com.miles.foodtruck.model.TrackacbleManager;
import java.util.ArrayList;


public class CategoriesSpinnerListener implements AdapterView.OnItemSelectedListener {

    private ArrayList<AbstractTrackable> foodTrucks;
    private RecyclerAdapter mAdapter;

    public CategoriesSpinnerListener(ArrayList<AbstractTrackable> foodTrucks,
                                     RecyclerAdapter mAdapter) {

        this.foodTrucks = foodTrucks;
        this.mAdapter = mAdapter;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        //Position 0 is "All Categories"
        if (position == 0)
        {
            foodTrucks = TrackacbleManager.getTrackableList();
        }
        else{

            foodTrucks = TrackacbleManager.getTrucksInCategory((String) parent.getItemAtPosition(position));

        }

        //Update Recycler adapter
        mAdapter.updateTrackables(foodTrucks);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
