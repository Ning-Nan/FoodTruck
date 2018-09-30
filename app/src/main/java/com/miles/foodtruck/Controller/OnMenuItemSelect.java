package com.miles.foodtruck.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.miles.foodtruck.R;
import com.miles.foodtruck.view.TrackingListActivity;

/*
    Menu action for all activities.
 */
public class OnMenuItemSelect {

    public static boolean onOptionsItemSelected(MenuItem item, AppCompatActivity activity){

        switch (item.getItemId()) {
            case R.id.show_trackings:
                Intent intent = new Intent(activity, TrackingListActivity.class);
                activity.startActivity(intent);
                return true;

            //Back to last activity
            case android.R.id.home:
                activity.finish();
                return true;


            case R.id.suggest_now:
                //TO DO
                //1. MAKE THE SUGGETIONS
                //2. CALL ALERT TO DISPLAY THESE THINGS

                //Test Notification

                return true;

        }
        return false;
    }
}
