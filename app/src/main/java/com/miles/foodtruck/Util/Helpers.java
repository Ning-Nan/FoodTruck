package com.miles.foodtruck.Util;


import android.util.Log;

import com.miles.foodtruck.Service.TrackingService;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Helpers {

    public static int[]  extractDateTime(String type, String str){

        switch (type)
        {

            case Constant.DatePicker:
                String[] output = str.split("/");

                int[] dates = {Integer.parseInt(output[0]),Integer.parseInt(output[1]),Integer.parseInt(output[2])};

                return dates;

            case Constant.TimePicker:

                int hours;
                int minutes;

                if (str.substring(str.length()-2,str.length()) .equals("AM"))
                {
                    hours = Integer.parseInt(str.substring(0,1));
                }
                else
                    {
                        if (Integer.parseInt(str.substring(0,1)) == 12)
                        {
                            hours = 12;
                        }
                        else
                            {
                                hours  = Integer.parseInt(str.substring(0,1)) + 12;
                            }

                    }

                minutes = Integer.parseInt(str.substring(3,4));
                int[] time = {hours, minutes};
                return time;

        }


        return null;
    }

    public static boolean onSubmitCheck(String title, String dateText, String timeText, String trackableId, TrackingService trackingService){

        if (title.equals(""))
        {

        }





        Log.i("ASD", "Parsed File Contents:");
        trackingService.logAll();

        try
        {
            // 5 mins either side of 05/07/2018 1:05:00 PM
            // PRE: make sure tracking_data.txt contains valid matches
            // PRE: make sure device locale matches provided DateFormat (you can change either device settings or String param)
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
            String searchDate = "05/07/2018 1:06:00 PM";
            int searchWindow = 10; // +/- 5 mins
            Date date = dateFormat.parse(searchDate);
            List<TrackingService.TrackingInfo> matched = trackingService
                    .getTrackingInfoForTimeRange(date, searchWindow, 0);
            Log.i("SAD", String.format("Matched Query: %s, +/-%d mins", searchDate, searchWindow));
            trackingService.log(matched);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return false;
    }





}
