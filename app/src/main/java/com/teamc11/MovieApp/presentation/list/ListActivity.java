package com.teamc11.MovieApp.presentation.list;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.ByteArrayBuffer;
import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.applicationlogic.adapters.ListAdapter;
import com.teamc11.MovieApp.applicationlogic.loaders.list.ListLoader;
import com.teamc11.MovieApp.applicationlogic.loaders.movie.MovieLoader;
import com.teamc11.MovieApp.domain.Genre;
import com.teamc11.MovieApp.domain.List;
import com.teamc11.MovieApp.domain.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>> {
    private final String TAG = ListActivity.class.getSimpleName();
    private ArrayList<List> lists = new ArrayList<>();
    private RecyclerView listRecyclerView;
    private ListAdapter listAdapter;
    private ProgressBar progressBarList;
    private Button mCreationDateBtn;
    private Button mRatingBtn;
    private Button mTitleBtn;
    private boolean creationBool = false;
    private boolean titleBool = false;
    private boolean ratingBool = false;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        LoaderManager.getInstance(this).initLoader(0,null,this);

        // Get progress bar
        progressBarList = findViewById(R.id.progressBarList);
        // Get a handle to the RecyclerView.
        listRecyclerView = findViewById(R.id.listRecyclerView);
        // Create an adapter and supply the data to be displayed.
        listAdapter = new ListAdapter(this, lists);
        // Connect the adapter with the RecyclerView.
        listRecyclerView.setAdapter(listAdapter);
        // Give the RecyclerView a layout manager.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            listRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            listRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        // searchview instantieren
        this.searchView = findViewById(R.id.search_view_list);
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

        //localize sorting buttons
        mCreationDateBtn = findViewById(R.id.button_sort_CreationDate);
        mRatingBtn = findViewById(R.id.button_sort_Rating);
        mTitleBtn = findViewById(R.id.button_sort_title);

        //add onclicklistener for all buttons
        mCreationDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<List> filteredList = lists;
                if(!creationBool) {
                    titleBool = false;
                    ratingBool = false;
                    creationBool = true;
                    //sort list on highest id (higher id == newer list)
                    Collections.sort(filteredList, new Comparator<List>() {
                        @Override
                        public int compare(List item, List t1) {
                            String s1 = item.getId();
                            String s2 = t1.getId();
                            return s2.compareToIgnoreCase(s1);
                        }
                    });
                } else {
                    //reverse list when already clicked
                    Collections.reverse(filteredList);
                }
                //update adapter
                listAdapter.filter(filteredList);
            }
        });

        mRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<List> filteredList = lists;
                if(!ratingBool) {
                    titleBool = false;
                    ratingBool = true;
                    creationBool = false;

                for (List l : filteredList) {
                    double averageRating = 0.0;

                    for(Movie m : l.getMovies()) {
                        averageRating += m.getVoteAverage();
                    }
                    if(!l.getMovies().isEmpty()) {
                        //calculate average
                        averageRating = averageRating / l.getMovies().size(); // will be NaN without if statement when list doesn't have movies
                    }
                    //set list's average
                    l.setAverageRating(averageRating);
                }
                //sort list from high average to low
                    Collections.sort(filteredList, new Comparator<List>() {
                        @Override
                        public int compare(List c1, List c2) {
                            return Double.compare(c2.getAverageRating(), c1.getAverageRating());
                        }
                    });
                } else {
                    //if pressed again reverse list
                    Collections.reverse(filteredList);
                }
                //update adapter
                listAdapter.filter(filteredList);
            }
        });

        mTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<List> filteredList = lists;
                if(!titleBool) {
                    titleBool = true;
                    ratingBool = false;
                    creationBool = false;
                    //sort list alphabetically
                    Collections.sort(filteredList, new Comparator<List>() {
                        @Override
                        public int compare(List item, List t1) {
                            String s1 = item.getName();
                            String s2 = t1.getName();
                            return s1.compareToIgnoreCase(s2);
                        }
                    });
                } else {
                    //reverse list if
                    Collections.reverse(filteredList);
                }
                listAdapter.filter(filteredList);
            }
        });
    }

    public void createList(View view) {
        Intent intent = new Intent(this, ListCreateActivity.class);
        startActivity(intent);
    }

    // Load the ListLoader
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        // Create a Log that the method is finished
        Log.i(TAG, "onCreateLoader method finished!");
        // Return the ListLoader
        return new ListLoader(this);
    }

    // Put each found result in a List object and store them in the ArrayList
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<String>> loader, ArrayList<String> data) {
            // Clear list before adding new items
            lists.clear();
            // Assign JSON String to a List and store them in the List ArrayList
            parseLists(data);

            //fill list of Genre (name + id)
            ArrayList<Genre> genreNames = MovieLoader.parseGenres(data.get(0));

            //for every list
            for(List l : lists) {
                //fill list with genre names using list of ids
                MovieLoader.alterMoviesGenres(l.getMovies(), genreNames);
            }

            // Notify the adapter that new data is available
            listAdapter.notifyDataSetChanged();
            // Sets visibility of recyclerview on visible
            listRecyclerView.setVisibility(View.VISIBLE);
            // Sets visibility of progressbar on gone
            progressBarList.setVisibility(View.GONE);
            // Create a Log that the method is finished
            Log.i(TAG, "onLoadFinished method finished!");
    }

    public ArrayList<List> parseLists(ArrayList<String> data) {
        for(int i = 2; i < data.size(); i++) {
            try {
                // Get the results array
                JSONObject jsonObject = new JSONObject(data.get(i));

                ArrayList<Movie> moviesTemp = new ArrayList<>();

                String id = jsonObject.optString("id");
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                String posterPath = jsonObject.getString("poster_path");
                String createdBy = jsonObject.getString("created_by");

                JSONArray itemsArray = jsonObject.getJSONArray("items");

                for (int j = 0; j < itemsArray.length(); j++) {
                    JSONObject movieInfo = itemsArray.getJSONObject(j);

                    boolean adult = movieInfo.optBoolean("adult");

                    JSONArray idArray = movieInfo.getJSONArray("genre_ids");

                    ArrayList<Integer> genreIDs = new ArrayList<>();

                    for(int k = 0; k < idArray.length(); k++) {
                        genreIDs.add(idArray.getInt(k));
                    }

                    int MovieId = movieInfo.getInt("id");
                    String originalLanguage = movieInfo.getString("original_language");
                    String originalTitle = movieInfo.getString("original_title");
                    String overview = movieInfo.getString("overview");
                    Double popularity = movieInfo.getDouble("popularity");
                    String listPosterPath = "https://image.tmdb.org/t/p/original" + movieInfo.getString("poster_path");
                    String backdropPath = "https://image.tmdb.org/t/p/original" + movieInfo.getString("backdrop_path");

                    String releaseDate = movieInfo.getString("release_date");
                    String title = movieInfo.getString("title");
                    boolean video = movieInfo.getBoolean("video");
                    double voteAverage = movieInfo.getDouble("vote_average");
                    int voteCount = movieInfo.getInt("vote_count");

                    ImageUrlToBytes imgToBytes = new ImageUrlToBytes();
                    byte[] posterPathByte = imgToBytes.execute(listPosterPath).get();

                    //add id to list
                    moviesTemp.add(new Movie(adult, backdropPath, MovieId, genreIDs, originalLanguage,
                            originalTitle, overview, popularity, posterPath, releaseDate, title,
                            video, voteAverage,voteCount, posterPathByte));
                }

                int itemCount = jsonObject.getInt("item_count");
                int favoriteCount = jsonObject.getInt("favorite_count");
                String iso_639_1 = jsonObject.getString("iso_639_1");

                // Create a List and put each List in the List ArrayList
                List list = new List(id, name, description, posterPath, createdBy, moviesTemp, itemCount, favoriteCount, iso_639_1);
                //add list to the list of lists
                lists.add(list);
            } catch (Exception e) {
                // Create a Log that something went wrong
                Log.e(TAG, "Something went wrong!");
                e.printStackTrace();
            }
        }
        // Create a Log showing toString of returned list
        Log.d(TAG, String.valueOf(lists.size()));
        // Return the List ArrayList
        return lists;
    }

    // Reset the Loader
    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<String>> loader) {
        // Create a Log that the method is finished
        Log.i(TAG, "onLoaderReset method finished!");
    }

    // Searchview Filter
    private String filter(String newText) {
        ArrayList<List> filteredList = new ArrayList<>();

        for (List item : lists) {
            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        listAdapter.filter(filteredList);
        return null;
    }

    private class ImageUrlToBytes extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... strings) {
            try {
                URL imageUrl = new URL(strings[0]);
                URLConnection urlConnection = imageUrl.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(500);

                int current = 0;
                while ((current = bufferedInputStream.read()) != -1) {
                    byteArrayBuffer.append((byte) current);
                }

                return byteArrayBuffer.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}