package com.miles.foodtruck.service.workers;

import android.os.AsyncTask;
import com.miles.foodtruck.service.DbManager;
import com.miles.foodtruck.view.MainActivity;

//Load on app start.
public class DbinitAsyncTask extends AsyncTask<Void, Integer, Void>{

    private MainActivity activity;

    public DbinitAsyncTask(MainActivity activity){

        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        //First call on DB Manager, will load from database.
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
        activity.initReminder();

    }

}


