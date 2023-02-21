package com.teamc11.MovieApp.presentation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.applicationlogic.LanguageHelper;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // TAG for logging
    private static final String TAG = SettingsActivity.class.getSimpleName();

    Switch darkMode;
    private SharedPreferences mPreferences = null;
    public static boolean booleanVal;
    public static String languageVal;
    public static int helperLanguage;
    private boolean languageChosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get spinner item array
        getSpinner();

        // Switch for darkmode
        darkMode = findViewById(R.id.darkMode_switch);
        mPreferences = getSharedPreferences("mypref", 0);
        booleanVal = mPreferences.getBoolean("night_mode", false);
        if (booleanVal) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            darkMode.setChecked(true);
        }
        // Checks if switch for darkmode is changed
        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = mPreferences.edit();
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkMode.setChecked(true);
                    editor.putBoolean("night_mode", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkMode.setChecked(false);
                    editor.putBoolean("night_mode", false);
                }
                editor.commit();
            }
        });

        // Create LanguageHelper
        LanguageHelper helper = new LanguageHelper(this);

        // Change Language button
        Button languageButton = findViewById(R.id.change_language_button);

        // Language button setOnClickListener
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mPreferences.edit();
                SettingsActivity.helperLanguage = mPreferences.getInt("language_helper", 2);
                if (languageChosen) {
                    if (helperLanguage == 2) {
                        helper.upDateResource(languageVal);
                        editor.putString("language_mode", "en");
                        editor.putInt("helper_language", 2);
                    } else if (helperLanguage == 1) {
                        helper.upDateResource(languageVal);
                        editor.putString("language_mode", "nl");
                        editor.putInt("helper_language", 1);
                    }
                    Log.i(TAG, "Changed language to:" + languageVal);
                    editor.commit();
                    recreate();
                } else {
                    Log.i(TAG, "Choose language first");
                }
            }
        });
    }

    // Get dropdown for languages
    private void getSpinner() {
        Spinner language = (Spinner) findViewById(R.id.language_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        language.setAdapter(adapter);
        language.setOnItemSelectedListener(this);
    }

    // Select Language from dropdown
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        languageChosen = true;
        if (i != 0) {
            Toast.makeText(parent.getContext(), parent.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
        }
        if (i == 2) {
            languageVal = "en";
            helperLanguage = 2;
        } else if (i == 1) {
            languageVal = "nl";
            helperLanguage = 1;
        } else {
            helperLanguage = 0;
            languageChosen = false;
        }
        Log.i(TAG, "Chosen language =" + String.valueOf(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}