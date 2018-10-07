package com.miles.foodtruck.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.miles.foodtruck.R;
import com.miles.foodtruck.model.TrackacbleManager;
import com.miles.foodtruck.model.Tracking;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.model.abstracts.AbstractTrackable;
import com.miles.foodtruck.model.abstracts.AbstractTracking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


//Note: This class Manage all the operations related with the database.
//Will be called by the asyncTask.
public class DbManager {

    private static DbManager singletonInstance;
    private SQLiteDatabase db;
    private Context context;

    public DbManager(Context applicationContext) {
        context = applicationContext;
    }

    //Singleton, call init when first time created instance.
    public static DbManager getSingletonInstance(Context context)
    {

        if (singletonInstance == null){
            singletonInstance = new DbManager(context.getApplicationContext());
            singletonInstance.init();
            singletonInstance.loadOnStart();
        }

        return singletonInstance;
    }


    //create table or get the database instance.
    private void init(){
        db = SimpleDBOpenHelper.getSingletonInstance(context).getWritableDatabase();
    }

    //Load Trackable from txt file and insert into database.
    //Load Tracking from database to the Tracking manager.
    private void loadOnStart(){

        ArrayList<AbstractTrackable> foodTrucks = new ArrayList<>();

        //get trackbles from text file
        try {
            foodTrucks =  TrackacbleManager.readTrackableList(
                    context.getResources().openRawResource(R.raw.food_truck_data));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Try to insert the trackables to database.
        //The try catch block throw the records that the database already has.
        for (int i = 0; i < foodTrucks.size(); i++) {

            AbstractTrackable foodTruck = foodTrucks.get(i);
            ContentValues values = new ContentValues();
            values.put("id", foodTruck.getId());
            values.put("name", foodTruck.getName());
            values.put("url", foodTruck.getUrl());
            values.put("description", foodTruck.getDescription());
            values.put("category", foodTruck.getCategory());

            try {
                db.insertOrThrow(SimpleDBOpenHelper.TABLE_TRACKABLE,
                        null, values);
            }catch (SQLiteConstraintException e){
                //Do nothing as this means this id already exits.
            }
            values.clear();
        }



        //Start to load trackings from database
        Cursor cursor = db.query(SimpleDBOpenHelper.TABLE_TRACKING,null,null,
                null,null,null,null,null);

        if(cursor.getCount() > 0)
        {
            //Move to first index
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                String id = cursor.getString(cursor.getColumnIndex("id"));
                int trackableId = cursor.getInt(cursor.getColumnIndex("trackableId"));
                String title = cursor.getString(cursor.getColumnIndex("title"));

                //Date object Stored as long timestamp in db.
                //Read and convert the dates.
                long targetStartTimestamp =
                        cursor.getLong(cursor.getColumnIndex("targetStartTime"));
                long targetEndTimestamp =
                        cursor.getLong(cursor.getColumnIndex("targetEndTime"));
                long meetTimeStamp =
                        cursor.getLong(cursor.getColumnIndex("meetTime"));
                Date targetStartTime = new Date(targetStartTimestamp);
                Date targetEndTime = new Date(targetEndTimestamp);
                Date meetTime = new Date(meetTimeStamp);
                String currLocation = cursor.getString(cursor.getColumnIndex("currLocation"));
                String meetLocation = cursor.getString(cursor.getColumnIndex("meetLocation"));

                AbstractTracking tracking = new Tracking();
                tracking.setTrackingId(id);
                tracking.setTrackableId(trackableId);
                tracking.setTitle(title);
                tracking.setTargetStartTime(targetStartTime);
                tracking.setTargetEndTime(targetEndTime);
                tracking.setMeetTime(meetTime);
                tracking.setCurrLocation(currLocation);
                tracking.setMeetLocation(meetLocation);
                TrackingManager.getSingletonInstance().addWithNoConflic(tracking);


                //Move next
                cursor.moveToNext();
            }
        }

        cursor.close();



    }

    //Save trackings when modified or added.
    public void saveOnAdded(AbstractTracking tracking){


        ContentValues values = new ContentValues();
        values.put("id",tracking.getTrackingId());
        values.put("trackableId",tracking.getTrackableId());
        values.put("title",tracking.getTitle());
        //Save time as timestamp
        values.put("targetStartTime",tracking.getTargetStartTime().getTime());
        values.put("targetEndTime",tracking.getTargetEndTime().getTime());
        values.put("meetTime",tracking.getMeetTime().getTime());
        values.put("currLocation",tracking.getCurrLocation());
        values.put("meetLocation",tracking.getMeetLocation());

        //Insert if not exist.
        //Update if exist.
        db.insertWithOnConflict(SimpleDBOpenHelper.TABLE_TRACKING,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        values.clear();
        //db.close();
    }

    //Save trackings when removed.
    public void saveOnRemoved(AbstractTracking tracking){

        db.delete(SimpleDBOpenHelper.TABLE_TRACKING,"id="+ "'" + tracking.getTrackingId()+
                "'",null);
        //db.close();

    }

    public void close(){
        db.close();
    }

}
