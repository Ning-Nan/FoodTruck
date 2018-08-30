package com.miles.foodtruck.Util;


import android.content.Context;
import android.widget.Toast;
import com.miles.foodtruck.Model.TrackingManager;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


public class Helpers {

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
}