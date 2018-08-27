package com.miles.foodtruck.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;


import com.miles.foodtruck.Model.Abstract.AbstractTrackable;
import com.miles.foodtruck.Model.Abstract.AbstractTracking;
import com.miles.foodtruck.Util.Constant;
import com.miles.foodtruck.View.ModifyTrackingActivity;



public class ModifyTrackingListener implements View.OnClickListener {

    private AbstractTrackable foodTruck;
    private AbstractTracking tracking;
    private Context context;



    public ModifyTrackingListener(AbstractTrackable foodTruck, AbstractTracking tracking, Context context){
        this.foodTruck = foodTruck;
        this.tracking = tracking;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(context, ModifyTrackingActivity.class);

        //case edit tracking
        if (foodTruck == null){

        }

        //case add new tracking
        else
        {
            intent.putExtra(Constant.operation, Constant.AddOperation);
            intent.putExtra(Constant.trackableId,Integer.toString(foodTruck.getId()));
            intent.putExtra(Constant.trackableName,foodTruck.getName());


            context.startActivity(intent);
        }

    }
}
