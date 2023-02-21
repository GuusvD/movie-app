package com.teamc11.MovieApp.presentation;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.applicationlogic.adapters.AccountAdapter;
import com.teamc11.MovieApp.applicationlogic.loaders.AccountLoader;

import com.teamc11.MovieApp.domain.Rating;
import com.teamc11.MovieApp.domain.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class AccountActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>> {

    private final String TAG = AccountActivity.class.getSimpleName();
    private ArrayList<Rating> ratings = new ArrayList<>();
    private RecyclerView accountRecyclerView;
    private AccountAdapter accountAdapter;

    private TextView mAccountID;
    private TextView mAccountUsername;
    private TextView mAccountName;
    private ImageView mAccountImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        User u = LoginActivity.user;

        mAccountID = findViewById(R.id.account_id);
        mAccountUsername = findViewById(R.id.account_username);
        mAccountName = findViewById(R.id.account_name);
        mAccountImage = findViewById(R.id.account_image);
        mAccountID.setText(getString(R.string.account_id_label) + " " + String.valueOf(u.getId()));
        mAccountUsername.setText(getString(R.string.account_username_label) + " " + u.getUsername());

        if(!u.getName().isEmpty()) {
            mAccountName.setText(getString(R.string.account_name_label) + " " + u.getName());
        } else {
            mAccountName.setText("");
        }

        if(!"https://www.themoviedb.org/t/p/w150_and_h150_facenull".equals(u.getAvatarPath())) {
            Glide.with(this).load(LoginActivity.user.getAvatarPath()).circleCrop().into(mAccountImage);
        }

        LoaderManager.getInstance(this).initLoader(0,null,this);

        // Get a handle to the RecyclerView.
        accountRecyclerView = findViewById(R.id.ratingRecyclerView);
        // Create an adapter and supply the data to be displayed.
        accountAdapter = new AccountAdapter(this, ratings);
        // Connect the adapter with the RecyclerView.
        accountRecyclerView.setAdapter(accountAdapter);
        // Give the RecyclerView a layout manager.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            accountRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            accountRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        // Create a Log that the method is finished
        Log.i(TAG, "onCreate method finished!");
    }

    public void reloadActivity(View view) {
        startActivity(new Intent(this, AccountActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    // Load the AccountLoader
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        // Create a Log that the method is finished
        Log.i(TAG, "onCreateLoader method finished!");
        // Return the AccountLoader
        return new AccountLoader(this);
    }

    // Put each found result in a Rating object and store them in the ArrayList
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<String>> loader, ArrayList<String> data) {
        // Clear ratings before adding new items
        ratings.clear();
        // Assign JSON String to a Rating and store them in the Rating ArrayList
        assignJSONToArrayList(data);
        //Reverse list of ratings so newest are shown first
        Collections.reverse(ratings);
        // Notify the adapter that new data is available
        accountAdapter.notifyDataSetChanged();
        // Create a Log that the method is finished
        Log.i(TAG, "onLoadFinished method finished!");
    }

    public ArrayList<Rating> assignJSONToArrayList(ArrayList<String> data) {
        for(int i = 0; i < data.size(); i++) {
            try {
                // Get the results array
                JSONObject jsonObject = new JSONObject(data.get(i));

                JSONArray itemsArray = jsonObject.getJSONArray("results");

                for (int j = 0; j < itemsArray.length(); j++) {
                    JSONObject item = itemsArray.getJSONObject(j);

                    int id = item.getInt("id");
                    String original_language = item.getString("original_language");
                    String original_title = item.getString("original_title");
                    String overview = item.getString("overview");
                    String release_date = item.getString("release_date");
                    double popularity = item.getDouble("popularity");
                    String title = item.getString("title");
                    boolean video = item.getBoolean("video");
                    double vote_average = item.getDouble("vote_average");
                    int vote_count = item.getInt("vote_count");
                    double rating = item.getDouble("rating");

                    // Create a Rating and put each Rating in the Rating ArrayList
                    Rating ratingObject = new Rating(id, original_language, original_title, overview, release_date, popularity, title,
                                                    video, vote_average, vote_count, rating);
                    //add rating to the list of ratings
                    ratings.add(ratingObject);
                }
            } catch (Exception e) {
                // Create a Log that something went wrong
                Log.e(TAG, "Something went wrong!");
                e.printStackTrace();
            }
        }
        // Create a Log showing toString of returned list
        Log.d(TAG, String.valueOf(ratings.size()));
        // Return the Rating ArrayList
        return ratings;
    }

    // Reset the Loader
    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<String>> loader) {
        // Create a Log that the method is finished
        Log.i(TAG, "onLoaderReset method finished!");
    }
}