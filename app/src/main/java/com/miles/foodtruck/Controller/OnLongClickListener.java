package com.miles.foodtruck.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.miles.foodtruck.Adapter.RecyclerAdapter;
import com.miles.foodtruck.Model.Abstract.AbstractTracking;
import com.miles.foodtruck.Model.TrackacbleManager;
import com.miles.foodtruck.Model.Tracking;
import com.miles.foodtruck.Model.TrackingManager;
import com.miles.foodtruck.Service.TrackingService;
import com.miles.foodtruck.Util.Constant;
import com.miles.foodtruck.Util.Helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OnLongClickListener implements View.OnLongClickListener{

    private String trackableId;
    private AbstractTracking tracking;
    private RecyclerAdapter adapter;

    public OnLongClickListener(String trackableId, AbstractTracking tracking, RecyclerAdapter adapter){

        this.trackableId = trackableId;
        this.tracking = tracking;
        this.adapter = adapter;

    }
    @Override
    public boolean onLongClick(View v) {

        if (tracking == null){

            List<TrackingService.TrackingInfo> matched = Helpers.getTrackingInfoForTrackable(trackableId,new Date(),v.getContext(),false);

            for (int i = 0; i < matched.size(); i++) {

                logTrackingInfo(matched.get(i));
            }


        }
        else
        {

            TrackingManager.getSingletonInstance().remove((Tracking) tracking);
            Helpers.callToast(Constant.RemovedMessage, v.getContext());

        }



        return false;
    }


    private void logTrackingInfo(TrackingService.TrackingInfo trackingInfo){

        //TO DO


    }
}
