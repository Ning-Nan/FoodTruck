package com.miles.foodtruck.Controller;

import android.view.View;

import com.miles.foodtruck.Model.Abstract.AbstractTrackable;
import com.miles.foodtruck.Model.Abstract.AbstractTracking;

public class AddTrackingBtnListener implements View.OnClickListener {

    private AbstractTrackable foodTruck;
    private AbstractTracking tracking;

    public AddTrackingBtnListener(AbstractTrackable foodTruck, AbstractTracking tracking){
        this.foodTruck = foodTruck;
        this.tracking = tracking;
    }

    @Override
    public void onClick(View v) {

    }
}
