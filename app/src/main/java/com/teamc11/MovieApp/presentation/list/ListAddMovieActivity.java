package com.teamc11.MovieApp.presentation.list;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.applicationlogic.adapters.MovieAdapter;
import com.teamc11.MovieApp.applicationlogic.loaders.movie.MovieLoader;
import com.teamc11.MovieApp.domain.Movie;
import com.teamc11.MovieApp.domain.MovieDetailed;
import com.teamc11.MovieApp.domain.Watchable;
import com.teamc11.MovieApp.presentation.movie.MovieActivity;

import java.util.ArrayList;

public class ListAddMovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ArrayList<Watchable>>> {
    private final String TAG = MovieActivity.class.getSimpleName();
    public static ArrayList<Movie> movies = new ArrayList<>();
    public static ArrayList<MovieDetailed> detailedMovies = new ArrayList<>();

    private RecyclerView movieRecyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    public String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add_movie);

        //progressbar
        this.progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Get a handle to the RecyclerView.
        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        // Create an adapter and supply the data to be displayed.
        movieAdapter = new MovieAdapter(this, movies, detailedMovies, "addMovie", getIntent().getStringExtra("ID"));
        // Connect the adapter with the RecyclerView.
        movieRecyclerView.setAdapter(movieAdapter);
        // Give the RecyclerView a layout manager.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        // instantiate searchview
        this.searchView = findViewById(R.id.search_view_list);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!"".equals(s)) {
                    //set searchString
                    searchString = s;

                    //close soft keyboard
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    //empty query text
                    searchView.setQuery("", false);

                    //stop focussing on searchView
                    searchView.setIconified(true);

                    //show progressbar
                    progressBar.setVisibility(View.VISIBLE);

                    //start loader
                    instantiateLoader();
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void instantiateLoader() {
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    // Load the MovieLoader
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        // Return the MovieLoader
        return new MovieLoader(this, "search", searchString);
    }

    // Put each found result in a Movie object and store them in the ArrayList
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<ArrayList<Watchable>>> loader, ArrayList<ArrayList<Watchable>> data) {
        // Clear list before adding new items
        movies.clear();
        detailedMovies.clear();

        ArrayList<Boolean> addToList = new ArrayList<>();

        //set list with normal movies
        for (int i = 0; i < data.get(0).size(); i++) {
            addToList.clear();
            for (int j = 0; j < ListDetailActivity.list.getMovies().size(); j++) {
                if ((data.get(0).get(i).getId() == ListDetailActivity.list.getMovies().get(j).getId())) {
                    addToList.add(false);
                }
            }
            if(!addToList.contains(false)) {
                movies.add((Movie) data.get(0).get(i));
            }
        }

        //set list of detailed movies
        for (int i = 0; i < data.get(1).size(); i++) {
            addToList.clear();
            for(int j = 0; j < movies.size(); j++) {
                if(!(data.get(1).get(i).getId() == movies.get(j).getId())) {
                    addToList.add(false);
                } else {
                    addToList.add(true);
                }
            }
            if(addToList.contains(true)) {
                detailedMovies.add((MovieDetailed) data.get(1).get(i));
            }
        }

        // Notify the adapter that new data is available
        movieAdapter.notifyDataSetChanged();
        //sets visibility of recyclerview on visible
        movieRecyclerView.setVisibility(View.VISIBLE);
        //sets visibility of progressbar on gone
        progressBar.setVisibility(View.GONE);
        // Create a Log that the method is finished
        Log.i(TAG, "onLoadFinished method finished!");
    }

    // Reset the Loader
    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<ArrayList<Watchable>>> loader) {
        // Create a Log that the method is finished
        Log.i(TAG, "onLoaderReset method finished!");
    }
}