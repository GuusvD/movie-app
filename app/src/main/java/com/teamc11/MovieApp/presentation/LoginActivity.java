package com.teamc11.MovieApp.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.applicationlogic.LanguageHelper;
import com.teamc11.MovieApp.applicationlogic.loaders.LoginLoader;
import com.teamc11.MovieApp.datastorage.MovieViewModel;
import com.teamc11.MovieApp.domain.User;
import com.teamc11.MovieApp.presentation.movie.MovieActivity;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<User> {
    private SharedPreferences mPreferences = null;
    private final String LOG = LoginActivity.class.getSimpleName();
    private String username = "";
    private String password = "";
    public static User user;
    private ProgressBar progressBar;
    private MovieViewModel movieViewModel;
    private NetworkInfo networkInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //init viewModel
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        // Set theme
        setTheme();
        // set language
        setLanguage();

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        // Get progress bar
        progressBar = findViewById(R.id.progressBarMain);
        progressBar.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                //checks for wifi, without use database else use loader
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo == null) {
                    for (int i = 0; i < movieViewModel.getUserList().size(); i++) {
                        if (movieViewModel.getUserList().get(i).getUsername().equals(username)) {
                            user = movieViewModel.getUserList().get(i);
                        }
                    }

                    progressBar.setVisibility(View.VISIBLE);

                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        startActivity(new Intent(LoginActivity.this, MovieActivity.class));
                    } else {
                        showLoginFailed();
                        startActivity(new Intent(LoginActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                } else if (networkInfo.isConnected()) {
                    onClickData();
                }
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    username = usernameEditText.getText().toString();
                    password = passwordEditText.getText().toString();

                    //checks for wifi, without use database else use loader
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo == null) {
                        for (int i = 0; i < movieViewModel.getUserList().size(); i++) {
                            if (movieViewModel.getUserList().get(i).getUsername().equals(username)) {
                                user = movieViewModel.getUserList().get(i);
                            }
                        }

                        progressBar.setVisibility(View.VISIBLE);

                        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                            startActivity(new Intent(LoginActivity.this, MovieActivity.class));
                        } else {
                            showLoginFailed();
                            startActivity(new Intent(LoginActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        }
                    } else if (networkInfo.isConnected()) {
                        onClickData();
                    }

                    return false;
                }

                return false;
            }
        });
    }

    public void onClickData() {
        progressBar.setVisibility(View.VISIBLE);
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    private void showLoginFailed() {
        Toast.makeText(getApplicationContext(), this.getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
    }

    private void setTheme() {
        mPreferences = getSharedPreferences("mypref", 0);
        SettingsActivity.booleanVal = mPreferences.getBoolean("night_mode", false);
        if (SettingsActivity.booleanVal) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void setLanguage() {
        mPreferences = getSharedPreferences("mypref", 0);
        SettingsActivity.languageVal = mPreferences.getString("language_mode", "en");
        SettingsActivity.helperLanguage = mPreferences.getInt("helper_language", 2);
        Log.i(LOG, SettingsActivity.languageVal);
        LanguageHelper helper = new LanguageHelper(this);

        if (SettingsActivity.helperLanguage == 2) {
            helper.upDateResource("en");
        } else if (SettingsActivity.helperLanguage == 1) {
            helper.upDateResource("nl");
        }
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        // Create a Log that the method is finished
        Log.i(LOG, "onCreateLoader method finished!");
        // Return the LoginLoader
        return new LoginLoader(this, username, password);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<User> loader, User data) {
        if (data != null) {
            user = data;
            user.setPassword(password);

            //add user to database
            movieViewModel.insertUser(user);

            Log.d(LOG, data.toString());
            startActivity(new Intent(LoginActivity.this, MovieActivity.class));
        } else {
            showLoginFailed();
            this.startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<User> loader) {
        // Create a Log that the method is finished
        Log.i(LOG, "onLoaderReset method finished!");
    }
}