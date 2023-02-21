package com.teamc11.MovieApp.presentation.movie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.applicationlogic.adapters.MovieAdapter;
import com.teamc11.MovieApp.applicationlogic.loaders.movie.MovieLoader;
import com.teamc11.MovieApp.datastorage.MovieViewModel;
import com.teamc11.MovieApp.domain.Movie;
import com.teamc11.MovieApp.domain.MovieDetailed;
import com.teamc11.MovieApp.domain.Watchable;
import com.teamc11.MovieApp.presentation.AccountActivity;
import com.teamc11.MovieApp.presentation.FiltersActivity;
import com.teamc11.MovieApp.presentation.LoginActivity;
import com.teamc11.MovieApp.presentation.SettingsActivity;
import com.teamc11.MovieApp.presentation.list.ListActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class MovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ArrayList<Watchable>>> {
    private final String TAG = MovieActivity.class.getSimpleName();
    public static ArrayList<Movie> movies = new ArrayList<>();
    public static ArrayList<MovieDetailed> detailedMovies = new ArrayList<>();
    private NetworkInfo networkInfo;

    private RecyclerView movieRecyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBarMain;
    private SearchView searchView;

    private SharedPreferences mPreferences = null;
    public static int ratingVal;
    public static String genreVal;
    public static String dateVal;

    private MovieViewModel movieViewModel;
    private boolean asc_release_date;
    private boolean asc_rating;
    private boolean asc_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataInit();

        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataInit();
                swipeRefresh.setRefreshing(false);
            }
        });

        // Get progress bar
        progressBarMain = findViewById(R.id.progressBarMain);
        // Get a handle to the RecyclerView.
        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        // Create an adapter and supply the data to be displayed.
        movieAdapter = new MovieAdapter(this, movies, detailedMovies, "overview", null);
        // Connect the adapter with the RecyclerView.
        movieRecyclerView.setAdapter(movieAdapter);
        // Give the RecyclerView a layout manager.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        if (networkInfo == null) {
            setFilters();
            movieAdapter.notifyDataSetChanged();
            movieRecyclerView.setVisibility(View.VISIBLE);
            progressBarMain.setVisibility(View.GONE);
        }

        // searchview instantieren
        this.searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        // Create a Log that the method is finished
        Log.i(TAG, "onCreate method finished!");
    }

    private void setFilters() {
        mPreferences = getSharedPreferences("mypref", 0);
        // Shared filter button settings
        // Rating switch
        FiltersActivity.ratingSwitchVal = mPreferences.getBoolean("rating_switch_mode", false);
        if (FiltersActivity.ratingSwitchVal) {
            ratingVal = FiltersActivity.ratingVal;
        }

        // rating seekbar
        FiltersActivity.ratingVal = mPreferences.getInt("rating_mode", 0);
        if (FiltersActivity.ratingVal == 0) {
            FiltersActivity.ratingVal = MovieActivity.ratingVal;
        }

        // Genre switch
        FiltersActivity.genreSwitchVal = mPreferences.getBoolean("genre_switch_mode", false);
        if (FiltersActivity.genreSwitchVal) {
            genreVal = FiltersActivity.genreVal;
        }

        // genre dropdown
        FiltersActivity.genreVal = mPreferences.getString("genre_mode", "");
        if (FiltersActivity.genreVal != null) {
            FiltersActivity.genreVal = MovieActivity.genreVal;
        }

        // Date
        FiltersActivity.dateVal = mPreferences.getString("date_mode", "");

        // filter on filters
        if (ratingVal > 0 || genreVal != null || dateVal != null) {
            filter(genreVal, dateVal, ratingVal);
        }

        Log.i(TAG, "genre pref = " + genreVal);
        Log.i(TAG, "rating pref = " + ratingVal);
        Log.i(TAG, "date pref = " + dateVal);
    }

    // Searchview Filter
    private String filter(String genreVal, String dateVal, int ratingVal) {
        ArrayList<Movie> filteredList = new ArrayList<>();
        Log.i(TAG, "Filter: " + genreVal + " " + dateVal + " " + ratingVal);
        if (ratingVal > 0) {
            for (Movie item : movies) {
                if (Double.compare(Math.round(item.getVoteAverage()),new Double(ratingVal))==0) {
                    filteredList.add(item);
                    if (dateVal != null) {
                        if (!item.getReleaseDate().contains(dateVal)) {
                            filteredList.remove(item);
                        }
                    }
                    if (genreVal != null) {
                        if (!item.getAllGenres().contains(genreVal)) {
                            filteredList.remove(item);
                        }
                    }
                }
            }
        } else {
            for (Movie item : movies) {
                filteredList.add(item);
                if (dateVal != null) {
                    if (!item.getReleaseDate().contains(dateVal)) {
                        filteredList.remove(item);
                    }
                }
                if (genreVal != null) {
                    if (!item.getAllGenres().contains(genreVal)) {
                        filteredList.remove(item);
                    }
                }
            }
        }

        movieAdapter.filterList(filteredList);
        return null;
    }

    // Searchview Filter
    private String filter(String newText) {
        ArrayList<Movie> filteredList = new ArrayList<>();

        for (Movie item : movies) {
            if (item.getText().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        movieAdapter.filterList(filteredList);
        return null;
    }

    // Load the MovieLoader
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        // Create a Log that the method is finished
        Log.i(TAG, "onCreateLoader method finished!");
        // Return the MovieLoader
        return new MovieLoader(this, "discover", null);
    }

    // Put each found result in a Movie object and store them in the ArrayList
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<ArrayList<Watchable>>> loader, ArrayList<ArrayList<Watchable>> data) {
        // Clear list before adding new items
        movies.clear();
        detailedMovies.clear();

        //set list with normal movies
        for (int i = 0; i < data.get(0).size(); i++) {
            movies.add((Movie) data.get(0).get(i));
        }

        orderListOnPopularity(movies);

        //set list of detailed movies
        for (int i = 0; i < data.get(1).size(); i++) {
            detailedMovies.add((MovieDetailed) data.get(1).get(i));
        }
        // set filters
        setFilters();
        //insert movies in database
        insertIntoDatabase(movies);
        //insert detailed movies in database
        insertDetailedIntoDatabase(detailedMovies);
        // Notify the adapter that new data is available
        movieAdapter.notifyDataSetChanged();
        //sets visibility of recyclerview on visible
        movieRecyclerView.setVisibility(View.VISIBLE);
        //sets visibility of progressbar on gone
        progressBarMain.setVisibility(View.GONE);
        // Create a Log that the method is finished
        Log.i(TAG, "onLoadFinished method finished!");
    }

    // Reset the Loader
    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<ArrayList<Watchable>>> loader) {
        // Create a Log that the method is finished
        Log.i(TAG, "onLoaderReset method finished!");
    }

    // Create menu in header
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.account:
                Intent intentAccount= new Intent(MovieActivity.this, AccountActivity.class);
                startActivity(intentAccount);
                return true;
            case R.id.lists:
                Intent intentLists = new Intent(MovieActivity.this, ListActivity.class);
                startActivity(intentLists);
                return true;
            case R.id.filter:
                Intent intentFilters = new Intent(MovieActivity.this, FiltersActivity.class);
                startActivity(intentFilters);
                return true;
            case R.id.setting:
                Intent intentSettings = new Intent(MovieActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(getString(R.string.logoutConfirmation));
                builder.setMessage(getString(R.string.logoutConfirmMessage));

                builder.setPositiveButton(getString(R.string.confirmAction), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.user = null;
                        Intent intentLogout = new Intent(MovieActivity.this, LoginActivity.class);
                        startActivity(intentLogout);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(getString(R.string.cancelAction), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
        }
        return true;
    }

    public void dataInit() {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (LoaderManager.getInstance(this).getLoader(0) == null && networkInfo == null && movieViewModel.getMovieList().isEmpty()) {
            Toast.makeText(this, getString(R.string.firstTimeInternet), Toast.LENGTH_LONG).show();
        } else if (networkInfo == null) {
            ArrayList<Movie> moviesUnordered = (ArrayList<Movie>) movieViewModel.getMovieList();
            ArrayList<MovieDetailed> moviesDetailed = (ArrayList<MovieDetailed>) movieViewModel.getMovieDetailedList();
            movies = orderListOnPopularity(moviesUnordered);
            detailedMovies = moviesDetailed;

            Toast.makeText(this, getString(R.string.usingRoomLoad), Toast.LENGTH_LONG).show();
        } else if (networkInfo.isConnected()) {
            movieViewModel.deleteAll();
            LoaderManager.getInstance(this).initLoader(0, null, this);
          
            Toast.makeText(this, getString(R.string.usingAPILoad), Toast.LENGTH_LONG).show();
        }
    }

    public void sortReleaseDate(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!asc_release_date) {
                movies.sort((o1, o2) -> o1.getReleaseDate().compareTo(o2.getReleaseDate()));
                asc_release_date = true;
            } else {
                movies.sort((o1, o2) -> o2.getReleaseDate().compareTo(o1.getReleaseDate()));
                asc_release_date = false;
            }
        }
        movieAdapter.notifyDataSetChanged();
    }

    public void sortRating(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!asc_rating) {
                movies.sort((o1, o2) -> Double.valueOf(o1.getVoteAverage()).compareTo(Double.valueOf(o2.getVoteAverage())));
                asc_rating = true;
            } else {
                movies.sort((o1, o2) -> Double.valueOf(o2.getVoteAverage()).compareTo(Double.valueOf(o1.getVoteAverage())));
                asc_rating = false;
            }
        }
        movieAdapter.notifyDataSetChanged();
    }

    public void sortTitle(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!asc_title) {
                movies.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
                asc_title = true;
            } else {
                movies.sort((o1, o2) -> o2.getTitle().compareTo(o1.getTitle()));
                asc_title = false;
            }
        }
        movieAdapter.notifyDataSetChanged();
    }

    public void insertIntoDatabase(ArrayList<Movie> movies) {
        for (int i = 0; i < movies.size(); i++) {
            movieViewModel.insert(movies.get(i));
        }
    }

    public void insertDetailedIntoDatabase(ArrayList<MovieDetailed> moviesDetailed) {
        for (int i = 0; i < moviesDetailed.size(); i++) {
            movieViewModel.insertDetailed(moviesDetailed.get(i));
        }
    }

    public ArrayList<Movie> orderListOnPopularity(ArrayList<Movie> moviesUnordered) {
        Collections.sort(moviesUnordered, new Comparator<Movie>() {
            @Override
            public int compare(Movie movieOne, Movie movieTwo) {
                return (int) (movieTwo.getPopularity() - movieOne.getPopularity());
            }
        });

        return moviesUnordered;
    }

    // getters and setters
    public static void setRatingVal(int ratingVal) {
        MovieActivity.ratingVal = ratingVal;
    }

    public static void setGenreVal(String genreVal) {
        MovieActivity.genreVal = genreVal;
    }

    public static void setDateVal(String dateVal) {
        MovieActivity.dateVal = dateVal;
    }

    public static int getRatingVal() {
        return ratingVal;
    }

    public static String getGenreVal() {
        return genreVal;
    }

    public static String getDateVal() {
        return dateVal;
    }
}