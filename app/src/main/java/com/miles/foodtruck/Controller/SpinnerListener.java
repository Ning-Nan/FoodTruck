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
    private RecyclerAdapter mAdapter;

    public SpinnerListener(ArrayList<FoodTruck> foodTrucks,RecyclerAdapter mAdapter) {

        this.foodTrucks = foodTrucks;
        this.mAdapter = mAdapter;
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


        mAdapter.update(foodTrucks);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
