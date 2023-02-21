package com.teamc11.MovieApp.applicationlogic.loaders.list;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.teamc11.MovieApp.datastorage.NetworkUtils;
import com.teamc11.MovieApp.presentation.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListLoader extends AsyncTaskLoader<ArrayList<String>> {
    private final String TAG = ListLoader.class.getSimpleName();

    // Constructor to create a ListLoader
    public ListLoader(Context context) {
        super(context);
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

        jsonArray.add(NetworkUtils.retrieveNeededJSON("genres", null, null, null));

        jsonArray.add(NetworkUtils.retrieveNeededJSON("created_lists", LoginActivity.user.getId(), LoginActivity.user.getSessionID(), null));

        ArrayList<Integer> listIDs = getListIDs(jsonArray.get(1));

        //get lists
        for (int id : listIDs) {
            jsonArray.add(NetworkUtils.retrieveNeededJSON("list", id, null, null));
        }

        // Return the JSON String from the NetworkUtils.retrieveNeededJSON()
        return jsonArray;
    }

    public ArrayList<Integer> getListIDs(String JSON) {
        ArrayList<Integer> listIDs = new ArrayList<>();
        try {
            // Get the results array
            JSONObject jsonObject = new JSONObject(JSON);

            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for(int i = 0; i < resultsArray.length(); i++) {
                JSONObject result = resultsArray.getJSONObject(i);

                int id = result.getInt("id");

                //add list to the list of lists
                listIDs.add(id);
            }
        } catch (Exception e) {
            // Create a Log that something went wrong
            Log.e(TAG, "Something went wrong!");
            e.printStackTrace();
        }
        // Return the List ArrayList
        return listIDs;
    }
}