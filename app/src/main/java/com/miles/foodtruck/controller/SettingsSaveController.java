package com.miles.foodtruck.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import com.miles.foodtruck.R;
import com.miles.foodtruck.service.ActionReceiver;
import com.miles.foodtruck.service.ReminderService;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;

import static android.content.Context.MODE_PRIVATE;

public class SettingsSaveController implements View.OnClickListener {

    private AppCompatActivity activity;

    public SettingsSaveController(AppCompatActivity activity){

        this.activity = activity;
    }

    @Override
    public void onClick(View v) {

        //get input values
        EditText suggestionFrequency =
                (EditText) activity.findViewById(R.id.suggestion_setting);
        EditText remindBefore =
                (EditText) activity.findViewById(R.id.remind_before_setting);
        EditText remindAgain =
                (EditText) activity.findViewById(R.id.remind_again_setting);

        //Save to shared preferences
        SharedPreferences settings = activity.getSharedPreferences(Constant.SettingName,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Constant.SuggestionFrequency,
                Integer.parseInt(suggestionFrequency.getText().toString()));
        editor.putInt(Constant.RemindBefore,Integer.parseInt(remindBefore.getText().toString()));
        editor.putInt(Constant.RemindAgain,Integer.parseInt(remindAgain.getText().toString()));
        editor.commit();


        Helpers.callToast(Constant.SavedMessage,
                activity.getApplicationContext());


        //settings changed. Reset the suggestions period.
        resetSuggestion();

        //re-apply remind before
        ReminderService reminderService = new ReminderService(v.getContext());
        reminderService.setAll();

        activity.finish();


    }

    private void resetSuggestion(){

        //Re-apply the set time.
        //Use same request code to update.
        SharedPreferences settings = activity.getSharedPreferences(Constant.SettingName,MODE_PRIVATE);
        AlarmManager am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity.getApplicationContext(), ActionReceiver.class);
        intent.putExtra(Constant.Action,Constant.AlarmAction);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(),
                Constant.SuggestionRequestCode,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        long millis = settings.getInt(Constant.SuggestionFrequency,60)  * 1000;

        //Will set again in the broadcast receiver.
        //"Repeat" is not accrue.
        am.setExact(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + millis ,pendingIntent);

    }
}
