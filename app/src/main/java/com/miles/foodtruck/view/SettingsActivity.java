package com.miles.foodtruck.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.miles.foodtruck.R;
import com.miles.foodtruck.controller.SettingsSaveController;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    private void init(){

        SharedPreferences settings = getSharedPreferences("setting",MODE_PRIVATE);

        EditText suggestionFrequency = (EditText) findViewById(R.id.suggestion_setting);
        EditText remindBefore = (EditText) findViewById(R.id.remind_before_setting);
        EditText remindAgain = (EditText) findViewById(R.id.remind_again_setting);


        suggestionFrequency.setText(
                Integer.toString(settings.getInt("SuggestionFrequency",60)));
        remindBefore.setText(
                Integer.toString(settings.getInt("RemindBefore",60)));
        remindAgain.setText(
                Integer.toString(settings.getInt("RemindAgain",60)));

        Button saveBtn = (Button) findViewById(R.id.setting_btn);

        saveBtn.setOnClickListener(new SettingsSaveController(this));



    }
}
