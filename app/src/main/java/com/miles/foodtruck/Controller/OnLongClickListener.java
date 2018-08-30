package com.miles.foodtruck.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.miles.foodtruck.Adapter.RecyclerAdapter;
import com.miles.foodtruck.Model.Abstract.AbstractTracking;
import com.miles.foodtruck.Model.TrackacbleManager;
import com.miles.foodtruck.Model.TrackingManager;
import com.miles.foodtruck.Util.Constant;
import com.miles.foodtruck.Util.Helpers;

import java.util.ArrayList;

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

        }
        else
        {
            ArrayList<AbstractTracking> trackings=TrackingManager.getSingletonInstance().getAll();
            trackings.remove(tracking);
            adapter.updateTrackings(trackings);
            adapter.notifyDataSetChanged();
            Helpers.callToast(Constant.RemovedMessage, v.getContext());

        }



        return false;
    }
}
