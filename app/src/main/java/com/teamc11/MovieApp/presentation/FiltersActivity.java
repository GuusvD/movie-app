package com.teamc11.MovieApp.presentation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.presentation.movie.MovieActivity;

import java.util.Calendar;

public class FiltersActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // TAG for logging
    private static final String TAG = FiltersActivity.class.getSimpleName();

    // attributes
    public static Switch genreSwitch;
    public static Switch ratingSwitch;

    public static boolean ratingSwitchVal;
    public static boolean genreSwitchVal;

    private Spinner genre;
    private SeekBar seekbar;
    private TextView ratingView;
    private static TextView dateView;

    private static SharedPreferences mPreferences = null;
    public static int ratingVal;
    public static String genreVal;
    public static String dateVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        // Get spinner item array
        getSpinner();

        // Get views
        seekbar = findViewById(R.id.seekbar_rating);
        ratingView = findViewById(R.id.rating_number);
        dateView = findViewById(R.id.picked_date);

        genreSwitch = findViewById(R.id.genre_switch);
        ratingSwitch = findViewById(R.id.rating_switch);

        // get sharedpreference values
        ratingVal = MovieActivity.getRatingVal();
        genreVal = MovieActivity.getGenreVal();
        dateVal = MovieActivity.getDateVal();

        // set preferences
        mPreferences = getSharedPreferences("mypref", 0);

        genreSwitchVal = mPreferences.getBoolean("genre_switch_mode", false);
        if (genreSwitchVal) {
            genreSwitch.setChecked(true);
        }

        ratingVal = mPreferences.getInt("rating_mode", 0);
        if (ratingVal > 0) {
            seekbar.setProgress(ratingVal);
        }

        ratingSwitchVal = mPreferences.getBoolean("rating_switch_mode", false);
        if (ratingSwitchVal) {
            ratingSwitch.setChecked(true);
        }

        genreVal = mPreferences.getString("genre_mode", "");
        if (genreVal != null) {
            for (int pos = 0; pos < genre.getCount(); pos++) {
                if (genre.getItemAtPosition(pos).toString().equalsIgnoreCase(genreVal)) {
                    genre.setSelection(pos);
                }
            }

        }

        dateVal = mPreferences.getString("date_mode", "");
        if (dateVal != null) {
            FiltersActivity.dateView.setText(dateVal);

        }

        // seekbar listener
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                ratingView.setText("Rating " + String.valueOf(progress));
                mPreferences = getSharedPreferences("filter_mode", 0);
                ratingVal = progress;
                if (genreSwitch.isChecked()) {
                    MovieActivity.setRatingVal(ratingVal);
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putInt("rating_mode", ratingVal);
                    editor.commit();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });

        // genre switch listener
        genreSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = mPreferences.edit();
                if (isChecked) {
                    if (genre.getSelectedItemPosition() > 0) {
                        editor.putString("genre_mode", genreVal);
                        genreSwitch.setChecked(true);
                        MovieActivity.setGenreVal(genreVal);
                        editor.putBoolean("genre_switch_mode", true);
                    } else {
                        genre.setSelection(4);
                        Log.i(TAG, "Choose a genre!");
                    }

                } else {
                    editor.putString("genre_mode", "");
                    genreSwitch.setChecked(false);
                    MovieActivity.setGenreVal("none");
                    editor.putBoolean("genre_switch_mode", false);
                }
                editor.commit();
                Log.i(TAG, "Genre: " + genreVal);
            }
        });

        // rating switch listener
        ratingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = mPreferences.edit();
                if (isChecked) {
                    editor.putInt("rating_mode", seekbar.getProgress());
                    ratingSwitch.setChecked(true);
                    MovieActivity.setRatingVal(seekbar.getProgress());
                    editor.putBoolean("rating_switch_mode", true);
                } else {
                    editor.putInt("rating_mode", 0);
                    ratingSwitch.setChecked(false);
                    MovieActivity.setRatingVal(0);
                    editor.putBoolean("rating_switch_mode", false);
                }
                editor.commit();
                Log.i(TAG, "SeekbarProgress:" + seekbar.getProgress());
            }
        });
    }

    private void getSpinner() {
        genre = (Spinner) findViewById(R.id.genre_filter_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genre.setAdapter(adapter);
        genre.setOnItemSelectedListener(this);
        genreVal = genre.getSelectedItem().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        if (i > 0) {
            if (genreSwitch.isChecked()) {
                genreVal = parent.getItemAtPosition(i).toString();
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("genre_mode", genreVal);
                editor.commit();
                Toast.makeText(parent.getContext(), genreVal, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(parent.getContext(), "Choose a genre!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
            // do nothing
    }

    public void clearDateFilter(View view) {
        SharedPreferences.Editor editor = mPreferences.edit();
        FiltersActivity.dateView.setText(R.string.pick_a_date);
        MovieActivity.setDateVal("");
        editor.putString("date_mode", "Pick a date");
        editor.commit();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Date set to the same format
            String pickedDate = Integer.toString(year) + "-" + Integer.toString(month + 1) + "-" + Integer.toString(day);
            FiltersActivity.dateView.setText(getString(R.string.date_label) + pickedDate);
            SharedPreferences.Editor editor = mPreferences.edit();
            MovieActivity.setDateVal(pickedDate);
            editor.putString("date_mode", pickedDate);
            editor.commit();
            Log.i(TAG, "Picked date: " + pickedDate);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}