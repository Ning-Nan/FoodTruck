package com.miles.foodtruck.Controller;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;
import com.miles.foodtruck.R;
import com.miles.foodtruck.Util.Constant;
import com.miles.foodtruck.View.Fragments.DateTimePickerFragment;


/*
    Reusable Picker listener for date and time
 */
public class PickerOnClickListener implements View.OnClickListener{

    private EditText editText;
    private FragmentManager fragmentManager;

    public PickerOnClickListener(EditText editText, FragmentManager fragmentManager){

        this.editText = editText;
        this.fragmentManager = fragmentManager;
    }



    @Override
    public void onClick(View v) {

        DialogFragment pickerFragment = new DateTimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.PickerText,editText.getText().toString());

        //Show different picker
        switch (editText.getId()){
            case R.id.date_picker:
                bundle.putString(Constant.Picker,Constant.DatePicker);
                break;

            case R.id.time_picker:
                bundle.putString(Constant.Picker,Constant.TimePicker);
                break;
            default:
                break;
        }

        pickerFragment.setArguments(bundle);
        pickerFragment.show(fragmentManager,"Picker");

    }

}
