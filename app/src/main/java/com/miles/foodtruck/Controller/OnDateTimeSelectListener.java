package com.miles.foodtruck.Controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;



public class OnDateTimeSelectListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private EditText editText;
    public OnDateTimeSelectListener(EditText editText){
        this.editText = editText;

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String months;
        String day;

        //index from 0
        month = month + 1;

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
