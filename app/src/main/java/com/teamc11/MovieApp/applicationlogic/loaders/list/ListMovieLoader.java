package com.teamc11.MovieApp.applicationlogic.loaders.list;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.teamc11.MovieApp.applicationlogic.loaders.list.ListLoader;
import com.teamc11.MovieApp.datastorage.NetworkUtils;

import java.util.ArrayList;

public class ListMovieLoader extends AsyncTaskLoader<ArrayList<String>> {
    private final String TAG = ListLoader.class.getSimpleName();
    private ArrayList<Integer> movieIDs;

    // Constructor to create a ListMovieLoader
    public ListMovieLoader(Context context, ArrayList<Integer> movieIDs) {
        super(context);
        this.movieIDs = movieIDs;
        // Create a Log that the method is finished
        Log.i(TAG, "MovieDetailedLoader constructor finished!");
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
    public ArrayList<String> loadInBackground() {
        ArrayList<String> jsonArray = new ArrayList<>();

        for(int i = 0; i < movieIDs.size(); i++) {
            //add movie details to list
            jsonArray.add(NetworkUtils.retrieveNeededJSON("details", movieIDs.get(i), null, null));
            //add add trailer path to movie
            jsonArray.add(NetworkUtils.retrieveNeededJSON("trailer", movieIDs.get(i), null, null));
            //add reviews to movie
            jsonArray.add(NetworkUtils.retrieveNeededJSON("review", movieIDs.get(i), null, null));
        }
        return jsonArray;
    }
}