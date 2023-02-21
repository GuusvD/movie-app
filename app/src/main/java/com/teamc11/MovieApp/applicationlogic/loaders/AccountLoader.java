package com.teamc11.MovieApp.applicationlogic.loaders;

import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import com.teamc11.MovieApp.datastorage.NetworkUtils;
import com.teamc11.MovieApp.presentation.LoginActivity;
import java.util.ArrayList;

public class AccountLoader extends AsyncTaskLoader<ArrayList<String>> {
    private final String TAG = AccountLoader.class.getSimpleName();

    // Constructor to create a AccountLoader
    public AccountLoader(Context context) {
        super(context);
        // Create a Log that the method is finished
        Log.i(TAG, "AccountLoader constructor finished!");
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
        ArrayList<String> accountTemp = new ArrayList<>();

        accountTemp.add(NetworkUtils.retrieveNeededJSON("rating", LoginActivity.user.getId(), LoginActivity.user.getSessionID(), null));

        // Return result
        return accountTemp;
    }
}
