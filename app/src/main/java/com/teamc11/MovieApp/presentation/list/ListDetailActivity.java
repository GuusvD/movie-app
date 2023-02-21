package com.teamc11.MovieApp.presentation.list;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.applicationlogic.loaders.list.ListMovieLoader;
import com.teamc11.MovieApp.applicationlogic.adapters.MovieAdapter;
import com.teamc11.MovieApp.applicationlogic.loaders.movie.MovieLoader;
import com.teamc11.MovieApp.domain.List;
import com.teamc11.MovieApp.domain.Movie;
import com.teamc11.MovieApp.domain.MovieDetailed;

import java.util.ArrayList;

public class ListDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>> {
    private static final String LOG = ListDetailActivity.class.getSimpleName();

    //Localize all views
    private TextView mName;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    public static ArrayList<Integer> movieIDs;
    public static ArrayList<MovieDetailed> detailedMovies;
    private String listID;
    public static List list;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        progressBar = findViewById(R.id.progressBarMain);

        listID = list.getId();

        //instantiate ArrayList with ids
        movieIDs = new ArrayList<>();

        Log.e(LOG, LOG);

        //add id of each movie in current list to separate arraylist
        for (Movie m : list.getMovies()) {
            movieIDs.add(m.getId());
        }

        //call loader
        LoaderManager.getInstance(this).initLoader(0, null, this);

    }

    public void fillPageWithListInfo(List list) {
        //use intent to fill all needed data
        String name = list.getName();

        //set local view equal to layout view
        mName = findViewById(R.id.List_name);

        //set value to views, if the data is there
        if (!name.isEmpty()) {
            mName.setText(name);
        }

        // Get a handle to the RecyclerView.
        recyclerView = findViewById(R.id.listRecyclerView);
        // Create an adapter and supply the data to be displayed.
        movieAdapter = new MovieAdapter(this, list.getMovies(), detailedMovies, "deleteMovie", getIntent().getStringExtra("ID"));
        // Connect the adapter with the RecyclerView.
        recyclerView.setAdapter(movieAdapter);
        // Set focusable on false
        recyclerView.setFocusable(false);
        // Give the RecyclerView a layout manager.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }


    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new ListMovieLoader(this, movieIDs);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<String>> loader, ArrayList<String> data) {
        detailedMovies = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            MovieDetailed newMovie = MovieLoader.getMovieWithDetailedInformation(data.get(i));
            i++;
            newMovie.setTrailer(MovieLoader.parseTrailer(data.get(i)));
            i++;
            newMovie.setReviews(MovieLoader.getAllReviews(data.get(i)));
            detailedMovies.add(newMovie);
        }

        //fill page with all collected info
        fillPageWithListInfo(list);

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<String>> loader) {
        // Create a Log that the method is finished
        Log.i(LOG, "onLoaderReset method finished!");
    }

    public void addMovie(View view) {
        Intent intent = new Intent(this, ListAddMovieActivity.class);
        intent.putExtra("ID", listID);
        startActivity(intent);
    }
}