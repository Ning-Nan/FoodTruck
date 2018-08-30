package com.miles.foodtruck.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.miles.foodtruck.R;
import com.miles.foodtruck.View.TrackingListActivity;

public class OnMenuItemSelect {

    public static boolean onOptionsItemSelected(MenuItem item, AppCompatActivity activity){

        switch (item.getItemId()) {
            case R.id.show_trackings:
                Intent intent = new Intent(activity, TrackingListActivity.class);
                activity.startActivity(intent);
                return true;

            case android.R.id.home:
                activity.finish();
                return true;

        }
        return false;
    }
}
