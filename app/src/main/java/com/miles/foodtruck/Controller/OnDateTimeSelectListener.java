package com.miles.foodtruck.controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;


/*
    Reusable Picker for both Date and Time.
 */
public class OnDateTimeSelectListener implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    private EditText editText;

    public OnDateTimeSelectListener(EditText editText){
        this.editText = editText;
    }


    //Get integer values from picker.
    //Here need to convert it to the Date String format.
    //Pass the values from picker to TextView.
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String months;
        String day;

        //Picker result month index from 0
        month = month + 1;

        //Math MM/dd
        if ( month < 10){
            months = "0" + Integer.toString(month);
        }
        else
        {
            months = Integer.toString(month);
        }
        if (dayOfMonth < 10){
            day = "0" + Integer.toString(dayOfMonth);
        }
        else
        {
            day = Integer.toString(dayOfMonth);
        }


        String dateStr = months + "/" + day + "/" + Integer.toString(year);

        editText.setText(dateStr);
    }

    //Convert int values to time, matching h:mm:ss aa
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        int hour;
        String minutes;
        String period;

        if (hourOfDay - 12 >= 0)
        {
            period = "PM";

            if(hourOfDay - 12 == 0){
                hour = 12;
            }
            else {
                hour = hourOfDay - 12;
            }

        }
        else {
            period = "AM";
            hour = hourOfDay;
        }
        if (minute < 10){
            minutes = "0" + Integer.toString(minute);
        }
        else{
            minutes = Integer.toString(minute);
        }

        editText.setText(Integer.toString(hour) + ":" + minutes + ":00 " + period);


    }
}
