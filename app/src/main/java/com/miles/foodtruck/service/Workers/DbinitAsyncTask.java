package com.miles.foodtruck.service.Workers;

import android.os.AsyncTask;

import com.miles.foodtruck.service.DbManager;
import com.miles.foodtruck.view.MainActivity;

public class DbinitAsyncTask extends AsyncTask<Void, Integer, Void>{

    private MainActivity activity;

    public DbinitAsyncTask(MainActivity activity){

        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        DbManager.getSingletonInstance(activity.getApplicationContext());

        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        //load complete, so call ui method to update ui
        activity.initTrackable();
        activity.initRecyclerView();
        activity.initSpinner();

    }

}


