package com.miles.foodtruck.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.miles.foodtruck.R;
import com.miles.foodtruck.util.Helpers;

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

        SharedPreferences settings = activity.getSharedPreferences("setting",MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("SuggestionFrequency",Integer.parseInt(suggestionFrequency.getText().toString()));

        editor.commit();

        Helpers.callToast("Saved. Settings will be applied when next time app starts",
                activity.getApplicationContext());

    }
}
