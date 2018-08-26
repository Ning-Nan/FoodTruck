package com.miles.foodtruck.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.miles.foodtruck.Model.Abstract.AbstractTrackable;
import com.miles.foodtruck.Model.Abstract.AbstractTracking;
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
            intent.putExtra("Operation", "Add");
            intent.putExtra("TrackableId",Integer.toString(foodTruck.getId()));

            context.startActivity(intent);
           //Toast.makeText(context,"test",Toast.LENGTH_SHORT).show();
        }

    }
}
