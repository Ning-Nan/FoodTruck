package com.miles.foodtruck.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


class SimpleDBOpenHelper extends SQLiteOpenHelper
{
    private static final String LOG_TAG = SimpleDBOpenHelper.class.getName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "foodtruck.db";

    // TABLE (COLUMN) NAMES
    static final String TABLE_TRACKABLE = "tbl_trackable";
    static final String TABLE_TRACKING = "tbl_tracking";

    // SQL CREATE AND DROP TABLE STATEMENTS
    private static final String CREATE_TRACKABLE_TABLE = String.format("CREATE TABLE %s (" +
            "    id          INTEGER    PRIMARY KEY" +
            "                       UNIQUE" +
            "                       NOT NULL," +
            "    name        STRING," +
            "    description STRING," +
            "    url         STRING," +
            "    category    STRING" +
            ");", TABLE_TRACKABLE);

    private static final String CREATE_TRACKING_TABLE = String.format("CREATE TABLE tbl_tracking (" +
            "    id              STRING   PRIMARY KEY" +
            "                             UNIQUE" +
            "                             NOT NULL," +
            "    trackableId     INTEGER," +
            "    title           STRING," +
            "    targetStartTime DATETIME," +
            "    targetEndTime   DATETIME," +
            "    meetTime        DATETIME," +
            "    currLocation    STRING," +
            "    meetLocation    STRING" +
            ");", TABLE_TRACKING);


    private static SimpleDBOpenHelper singletonInstance;

    public static SimpleDBOpenHelper getSingletonInstance(Context context)
    {
        if (singletonInstance == null)
            singletonInstance = new SimpleDBOpenHelper(
                    context.getApplicationContext());

        return singletonInstance;
    }

    private SimpleDBOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Log some information about our database
        Log.i(LOG_TAG, "Created database: " + db.getPath());
        Log.i(LOG_TAG, "Database Version: " + db.getVersion());

        Log.i(LOG_TAG, "Database Open?  " + db.isOpen());
        Log.i(LOG_TAG, "Database readonly?  " + db.isReadOnly());

        // CREATE TABLES
        Log.i(LOG_TAG, "Create the tbl_trackable table using execSQL()");
        db.execSQL(CREATE_TRACKABLE_TABLE);

        Log.i(LOG_TAG,
                "Create the tbl_trackings table using SQLiteStatement.execute()");
        SQLiteStatement sqlSelect = db.compileStatement(CREATE_TRACKING_TABLE);
        sqlSelect.execute();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Not Implemented
    }

}
