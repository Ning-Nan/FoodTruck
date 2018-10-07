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
import com.miles.foodtruck.util.Helpers;
import com.miles.foodtruck.view.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class SettingsSaveController implements View.OnClickListener {

    private AppCompatActivity activity;
    public SettingsSaveController(AppCompatActivity activity){

        this.activity = activity;
    }
    @Override
    public void onClick(View v) {

        EditText suggestionFrequency =
                (EditText) activity.findViewById(R.id.suggestion_setting);
        EditText remindBefore =
                (EditText) activity.findViewById(R.id.remind_before_setting);
        EditText remindAgain =
                (EditText) activity.findViewById(R.id.remind_again_setting);


        SharedPreferences settings = activity.getSharedPreferences("setting",MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("SuggestionFrequency",Integer.parseInt(suggestionFrequency.getText().toString()));
        editor.putInt("RemindBefore",Integer.parseInt(remindBefore.getText().toString()));
        editor.putInt("RemindAgain",Integer.parseInt(remindAgain.getText().toString()));



        editor.commit();


        Helpers.callToast("Saved.",
                activity.getApplicationContext());

        AlarmManager am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(activity.getApplicationContext(), ActionReceiver.class);
        intent.putExtra("Action","Alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(),
                0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

//        SharedPreferences settings = activity.getSharedPreferences("setting", MODE_PRIVATE);
        long millis = settings.getInt("SuggestionFrequency",60)  * 1000;


        am.setExact(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + millis ,pendingIntent);

        //re-apply remind before
        ReminderService reminderService = new ReminderService(v.getContext());
        reminderService.setAll();

        activity.finish();


    }
}
