package com.teamc11.MovieApp.applicationlogic.loaders.list;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.teamc11.MovieApp.datastorage.NetworkUtils;
import com.teamc11.MovieApp.presentation.LoginActivity;

import org.json.JSONObject;

public class ListCreateLoader extends AsyncTaskLoader<String> {
    private final String TAG = ListCreateLoader.class.getSimpleName();

    // Constructor to create a ListCreateLoader
    public ListCreateLoader(Context context) {
        super(context);
        // Create a Log that the method is finished
        Log.i(TAG, "ListCreateLoader constructor finished!");
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

    // Processing the createList in the background
    @Nullable
    @Override
    public String loadInBackground() {
       return parseCreateListResponse(NetworkUtils.createList("list", LoginActivity.user.getSessionID()));
    }

    public String parseCreateListResponse(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            return String.valueOf(jsonObject.getInt(("status_code")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}