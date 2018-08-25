package com.miles.foodtruck.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import com.miles.foodtruck.Adapter.RecyclerAdapter;
import com.miles.foodtruck.Model.FoodTruck;
import com.miles.foodtruck.Service.TrucksService;

import java.util.ArrayList;

public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    private ArrayList<FoodTruck> foodTrucks;
    private RecyclerView mRecyclerView;

    public SpinnerListener(ArrayList<FoodTruck> foodTrucks, RecyclerView mRecyclerView) {

        this.foodTrucks = foodTrucks;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position == 0)
        {
            foodTrucks = TrucksService.getTrackableList();
        }
        else{

            foodTrucks = TrucksService.getTrucksInCategory((String) parent.getItemAtPosition(position));

        }

        /*
        Here I am not using notify dataset changed because I found for operation replacing the whole list, will not
        be updated by adapter.
        So I set new Adapter for it.
         */
        mRecyclerView.setAdapter(new RecyclerAdapter(foodTrucks));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
