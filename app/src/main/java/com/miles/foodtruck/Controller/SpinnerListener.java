package com.miles.foodtruck.Controller;

import android.view.View;
import android.widget.AdapterView;
import com.miles.foodtruck.Adapter.RecyclerAdapter;
import com.miles.foodtruck.Model.Abstract.AbstractTrackable;
import com.miles.foodtruck.Model.TrackacbleManager;

import java.util.ArrayList;

public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    private ArrayList<AbstractTrackable> foodTrucks;
    private RecyclerAdapter mAdapter;

    public SpinnerListener(ArrayList<AbstractTrackable> foodTrucks, RecyclerAdapter mAdapter) {

        this.foodTrucks = foodTrucks;
        this.mAdapter = mAdapter;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position == 0)
        {
            foodTrucks = TrackacbleManager.getTrackableList();
        }
        else{

            foodTrucks = TrackacbleManager.getTrucksInCategory((String) parent.getItemAtPosition(position));

        }


        mAdapter.updateTrackables(foodTrucks);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
