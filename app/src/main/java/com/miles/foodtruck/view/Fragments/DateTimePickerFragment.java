package com.miles.foodtruck.view.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import com.miles.foodtruck.controller.OnDateTimeSelectListener;
import com.miles.foodtruck.R;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;
import java.util.Calendar;

public class DateTimePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        switch (getArguments().getString(Constant.Picker)){

            case Constant.DatePicker:
                return callDateDialog();

            case Constant.TimePicker:
                return callTimeDialog();

            default:
                return null;

        }

    }


    private Dialog callDateDialog(){

        int year , day, month;

        if (!getArguments().getString(Constant.PickerText).equals(""))
        {

            int[] dates = Helpers.extractDateTime(Constant.DatePicker,
                    getArguments().getString(Constant.PickerText));
            day = dates[1];

            //Month index from 0
            month = dates[0] - 1;

            year = dates[2];
        }
        else {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        EditText editText = (EditText) getActivity().findViewById(R.id.date_picker);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(),
                new OnDateTimeSelectListener(editText), year, month, day);

    }

    private Dialog callTimeDialog(){

        int hour, minute;

        if (!getArguments().getString(Constant.PickerText).equals(""))
        {
            int[] time = Helpers.extractDateTime(
                    Constant.TimePicker,getArguments().getString(Constant.PickerText));
            hour = time[0];
            minute = time[1];
        }
        else {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

        }
        EditText editText = (EditText) getActivity().findViewById(R.id.time_picker);
        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(),
                new OnDateTimeSelectListener(editText), hour, minute,true);

    }

}
