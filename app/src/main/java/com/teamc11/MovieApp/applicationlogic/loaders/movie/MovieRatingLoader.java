package com.teamc11.MovieApp.applicationlogic.loaders.movie;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.teamc11.MovieApp.datastorage.NetworkUtils;
import com.teamc11.MovieApp.presentation.LoginActivity;
import com.teamc11.MovieApp.presentation.movie.MovieDetailActivity;

public class MovieRatingLoader extends AsyncTaskLoader<String> {
    private final String TAG = MovieRatingLoader.class.getSimpleName();

    // Constructor to create a MovieRatingLoader
    public MovieRatingLoader(Context context) {
        super(context);
        // Create a Log that the method is finished
        Log.i(TAG, "MovieRatingLoader constructor finished!");
    }

    // Load everything before the start
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // Force the loader to load
        forceLoad();
        // Create a Log that the method is finished
        Log.i(TAG, "onStartLoading method finished!");
    }

    // Processing the retrieveNeedJSON in the background to get the results
    @Nullable
    @Override
    public String loadInBackground() {
        String result = NetworkUtils.postRating("rating", MovieDetailActivity.currentMovie.getId(), LoginActivity.user.getSessionID());

        // Return result
        return result;
    }
}