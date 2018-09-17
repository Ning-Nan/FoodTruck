package com.miles.foodtruck.util;


import android.content.Context;
import android.widget.Toast;
import com.miles.foodtruck.model.TrackingManager;
import com.miles.foodtruck.service.TrackingService;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Helpers {

    //From date time text to picker acceptable int date values
    public static int[] extractDateTime(String type, String str) {

        switch (type) {

            case Constant.DatePicker:
                String[] output = str.split("/");

                int[] dates = {Integer.parseInt(output[0]), Integer.parseInt(output[1]), Integer.parseInt(output[2])};

                return dates;

            case Constant.TimePicker:

                String[] times = str.split(":");
                int hours;
                int minutes;

                if (str.substring(str.length() - 2, str.length()).equals("AM")) {
                    hours = Integer.parseInt(times[0]);
                } else {
                    if (Integer.parseInt(times[0]) == 12) {
                        hours = 12;
                    } else {
                        hours = Integer.parseInt(times[0]) + 12;
                    }

                }

                minutes = Integer.parseInt(times[1]);

                int[] time = {hours, minutes};
                return time;

        }


        return null;
    }


    public static void callToast(String message, Context context) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    //String to Date
    public static Date strToDate(String str) {

        Date date = new Date();

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

        try {
            date = dateFormat.parse(str);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date;
    }

    public static Date caculateEndTime(Date date, int minutes) {
        Date end = new Date();

        end.setTime(date.getTime() + minutes * 60000);

        return end;

    }



    //get random string
    public static String random(int length, TrackingManager trackingManager) {

        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append((char) (ThreadLocalRandom.current().nextInt(33, 128)));
        }

        String generated =  builder.toString();

        for (int i = 0; i < trackingManager.getAll().size(); i++) {

            if (generated.equals(trackingManager.getAll().get(i).getTrackingId()))
            {
                generated = random(5, trackingManager);
                break;
            }
        }
        return generated;
    }

    //Get tracking info list for the selected trackable
    public static List<TrackingService.TrackingInfo> getTrackingInfoForTrackable(String trackableId, Date date, Context context, boolean removeNonStops){

        TrackingService trackingService = TrackingService.getSingletonInstance(context);

        //One whole day (For A1)
        int searchWindow = 60 * 24;

        List<TrackingService.TrackingInfo> matched = trackingService.getTrackingInfoForTimeRange(date,searchWindow,0);

        Iterator<TrackingService.TrackingInfo> iter = matched.iterator();

        if (removeNonStops == true) {
            while (iter.hasNext()) {
                TrackingService.TrackingInfo trackingInfo = iter.next();

                if (!trackableId.equals(Integer.toString(trackingInfo.trackableId))|| trackingInfo.stopTime == 0)
                    iter.remove();
            }
        }
        else{
            while (iter.hasNext()) {
                TrackingService.TrackingInfo trackingInfo = iter.next();

                if (!trackableId.equals(Integer.toString(trackingInfo.trackableId)))
                    iter.remove();
            }

        }

        return matched;
    }

    //Generate spinner time slots for given tracking info list.
    public static ArrayList<String> getSpinnerItem(List<TrackingService.TrackingInfo> trackingInfos){

        ArrayList<String> spinnerItems = new ArrayList<>();

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

        for (int i = 0; i < trackingInfos.size(); i++) {

            String str = dateFormat.format(trackingInfos.get(i).date)
                    + " Stop: " + trackingInfos.get(i).stopTime + " minutes";

            spinnerItems.add(str);
        }

        return spinnerItems;

    }



}