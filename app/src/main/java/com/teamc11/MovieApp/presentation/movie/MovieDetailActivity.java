package com.teamc11.MovieApp.presentation.movie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.applicationlogic.InputFilterMinMax;
import com.teamc11.MovieApp.applicationlogic.loaders.movie.MovieRatingLoader;
import com.teamc11.MovieApp.applicationlogic.adapters.MovieReviewAdapter;
import com.teamc11.MovieApp.domain.MovieDetailed;
import com.teamc11.MovieApp.presentation.AccountActivity;

public class MovieDetailActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener, LoaderManager.LoaderCallbacks<String> {
    private static final String LOG = MovieDetailActivity.class.getSimpleName();
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    //Localize all views
    private ImageView mImageview;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mReleaseDate;
    private TextView mEmptyRating;
    private TextView mRating;
    private TextView mRatingInput;
    public static String ratingInput;
    private TextView mRuntime;
    private TextView mGenres;
    private TextView mProducers;
    private RecyclerView recyclerView;
    private MovieReviewAdapter movieReviewAdapter;
    private ImageButton shareButton;

    //current detailed Movie
    public static MovieDetailed currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        //fill page with all collected info
        try {
            fillPageWithMovieInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        YouTubePlayerFragment youTubePlayerFragment =
                (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            youTubePlayerFragment.getView().setVisibility(View.GONE);
        } else if (networkInfo.isConnected()) {
            youTubePlayerFragment.initialize("AIzaSyDw5I2X263AQsAelj3IJWc9Ufm2_1eZjHM",
                    this);
        }

        shareButton = findViewById(R.id.Button_Share);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder listText = new StringBuilder();
                listText.append("Title: ");
                listText.append(currentMovie.getTitle());
                listText.append("\n");
                listText.append("Synopsys: ");
                listText.append(currentMovie.getOverview());
                listText.append("\n");
                listText.append("Genres: ");
                listText.append(currentMovie.getAllGenres());
                listText.append("\n");
                listText.append("Vote Average: ");
                listText.append(currentMovie.getVoteAverage());
                listText.append("\n");
                listText.append("Released on: ");
                listText.append(currentMovie.getReleaseDate());

                String mimeType = "text/plain";
                ShareCompat.IntentBuilder
                        .from(MovieDetailActivity.this)
                        .setType(mimeType)
                        .setChooserTitle(R.string.share_text_with)
                        .setText(listText.toString())
                        .startChooser();
            }
        });
    }

    //go back to previous activity when go back button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void fillPageWithMovieInfo() {
        // use intent to fill all needed data
        String image = currentMovie.getBackdropPath();
        String title = currentMovie.getTitle();
        String overview = currentMovie.getOverview();
        Double rating = currentMovie.getVoteAverage();
        String runtime = currentMovie.getRuntimeInHoursAndMinutes();
        String genres = currentMovie.getGenresString();
        String producers = currentMovie.getAllProductionCompanies();
        String date = currentMovie.getReleaseDate();

        //set local view equal to layout view
        mImageview = findViewById(R.id.Movie_detail_image);
        mTitle = findViewById(R.id.Movie_detail_title);
        mOverview = findViewById(R.id.Movie_detail_overview);
        mRating = findViewById(R.id.Movie_detail_rating);
        mRuntime = findViewById(R.id.Movie_detail_runtime);
        mGenres = findViewById(R.id.Movie_detail_genres);
        mProducers = findViewById(R.id.Movie_detail_producedBy);
        mReleaseDate = findViewById(R.id.Movie_detail_date);
        mEmptyRating = findViewById(R.id.Movie_detail_rating_empty);
        mRatingInput = findViewById(R.id.textFieldRating);

        //set value to views, if the data is there
        if (!title.isEmpty()) {
            mTitle.setText(title);
        }

        if (!overview.isEmpty()) {
            mOverview.setText(overview);
        }

        if (!date.isEmpty()) {
            mReleaseDate.setText(date);
        }

        if (currentMovie.getReviews().isEmpty()) {
            mEmptyRating.setVisibility(View.VISIBLE);
        }

        if (!(rating == null)) {
            mRating.setText(rating.toString());
        }

        if (!runtime.isEmpty()) {
            mRuntime.setText(runtime);
        }

        if (!genres.isEmpty()) {
            mGenres.setText(genres);
        }

        if (!producers.isEmpty()) {
            mProducers.setText(producers);
        }

        mRatingInput.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});

        Button mSubmit = findViewById(R.id.Button_add_rating);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRatingInput.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.rating_error), Toast.LENGTH_SHORT).show();
                } else {
                    ratingInput = mRatingInput.getText().toString();
                    onClickData();
                }
            }
        });

        // Get a handle to the RecyclerView.
        recyclerView = findViewById(R.id.recyclerView);
        // Create an adapter and supply the data to be displayed.
        movieReviewAdapter = new MovieReviewAdapter(this, currentMovie.getReviews());
        // Connect the adapter with the RecyclerView.
        recyclerView.setAdapter(movieReviewAdapter);
        // Set focusable on false
        recyclerView.setFocusable(false);
        // Give the RecyclerView a layout manager.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (currentMovie.getBackdropPath().equals("https://image.tmdb.org/t/p/originalnull") || networkInfo == null) {
            mImageview.setImageResource(R.drawable.backdrop_placeholder);
        } else if (networkInfo.isConnected()) {
            Glide.with(this).load(currentMovie.getBackdropPath()).into(mImageview);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.cueVideo(currentMovie.getTrailer());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s)", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    public void onClickData() {
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a Log that the method is finished
        Log.i(TAG, "onCreateLoader method finished!");
        // Return the MovieRatingLoader
        return new MovieRatingLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        startActivity(new Intent(MovieDetailActivity.this, AccountActivity.class));
        Toast.makeText(getApplicationContext(), getString(R.string.youve_rated) + " " + currentMovie.getTitle() + " " + getString(R.string.with_rating) + " " + ratingInput, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}