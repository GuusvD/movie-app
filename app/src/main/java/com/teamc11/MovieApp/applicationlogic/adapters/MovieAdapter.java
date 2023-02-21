package com.teamc11.MovieApp.applicationlogic.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.datastorage.NetworkUtils;
import com.teamc11.MovieApp.domain.Movie;
import com.teamc11.MovieApp.domain.MovieDetailed;
import com.teamc11.MovieApp.presentation.list.ListAddMovieActivity;
import com.teamc11.MovieApp.presentation.list.ListDetailActivity;
import com.teamc11.MovieApp.presentation.movie.MovieActivity;
import com.teamc11.MovieApp.presentation.movie.MovieDetailActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Movie> movies;
    private ArrayList<MovieDetailed> moviesDetailed;
    private ArrayList<Movie> searchViewMovies;
    private LayoutInflater inflater;
    private Context context;
    public String type;
    public String listID;

    public static MovieDetailed clickedMovie;

    // Constructor to create a MovieAdapter
    public MovieAdapter(Context context, ArrayList<Movie> movies, ArrayList<MovieDetailed> moviesDetailed, String type, String listID) {
        this.inflater = LayoutInflater.from(context);
        this.movies = movies;
        this.moviesDetailed = moviesDetailed;
        this.context = context;
        this.type = type;
        this.listID = listID;
        searchViewMovies = new ArrayList<>(movies);
        // Create a Log that the method is finished
        Log.i(TAG, "MovieAdapter constructor finished!");
    }

    // Inflate the item layout and return the MovieHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieItem;

        switch (this.type) {// Assign the movie_item layout to the View item
            case "addMovie":
            case "deleteMovie":
                movieItem = inflater.inflate(R.layout.list_movie_item, parent, false);
                break;
            default:
                movieItem = inflater.inflate(R.layout.movie_item, parent, false);
        }

        // Create a Log that the method is finished
        Log.i(TAG, "onCreateViewHolder method finished!");
        // Return MovieHolder
        return new ViewHolder(movieItem);
    }

    // Connect the data to the MovieHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current Movie
        Movie currentMovie = movies.get(position);
        // Assign the values to the TextViews in the holder
        if (currentMovie.getPosterPathByte() == null) {
            holder.mMovieImage.setImageResource(R.drawable.posterpath_placeholder);
        } else {
            Glide.with(holder.mMovieImage)
                    .load(currentMovie.getPosterPathByte())
                    .centerCrop()
                    .into(holder.mMovieImage);
        }

        holder.mMovieTitle.setText(currentMovie.getTitle());
        holder.mMovieReleaseDate.setText(context.getString(R.string.releaseDateCard) + " " + currentMovie.getReleaseDate());
        holder.mMovieGenres.setText(context.getString(R.string.genresCard) + " " + currentMovie.getAllGenres());
        holder.mMovieRating.setText(context.getString(R.string.ratingCard) + " " + currentMovie.getVoteAverage());

        // Add an onclick on each item to navigate to the detail page
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //TODO:

                switch (type) {
                    case "addMovie":
                        for (int i = 0; i < ListAddMovieActivity.detailedMovies.size(); i++) {
                            if (ListAddMovieActivity.detailedMovies.get(i).getId() == movies.get(holder.getAdapterPosition()).getId()) {
                                clickedMovie = ListAddMovieActivity.detailedMovies.get(i);
                                MovieDetailActivity.currentMovie = clickedMovie;
                            }
                        }
                        break;
                    case "deleteMovie":
                        for (int i = 0; i < ListDetailActivity.detailedMovies.size(); i++) {
                            if (ListDetailActivity.detailedMovies.get(i).getId() == movies.get(holder.getAdapterPosition()).getId()) {
                                clickedMovie = ListDetailActivity.detailedMovies.get(i);
                                MovieDetailActivity.currentMovie = clickedMovie;
                            }
                        }
                        break;
                    default:
                        for (int i = 0; i < moviesDetailed.size(); i++) {
                            if (MovieActivity.detailedMovies.get(i).getId() == movies.get(holder.getAdapterPosition()).getId()) {
                                clickedMovie = MovieActivity.detailedMovies.get(i);
                                MovieDetailActivity.currentMovie = clickedMovie;
                            }
                        }
                }

                //make intent
                Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);

                //start intent
                context.startActivity(intent);
            }
        });

        if ("addMovie".equals(type)) {
            // Add an onclick on each item to navigate to the detail page
            holder.mControlMovieBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    MovieAdder m = new MovieAdder(currentMovie.getId(), listID);
                    try {
                        m.execute().get();

                        ListDetailActivity.list.getMovies().add(movies.get(holder.getAdapterPosition()));
                        for (MovieDetailed detailedMovie : ListAddMovieActivity.detailedMovies) {
                            if (movies.get(holder.getAdapterPosition()).getId() == detailedMovie.getId()) {
                                ListDetailActivity.detailedMovies.add(detailedMovie);
                            }
                        }
                        movies.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        Log.e("e", String.valueOf(holder.getAdapterPosition()));
                        Toast.makeText(context.getApplicationContext(), context.getString(R.string.movie_added), Toast.LENGTH_SHORT).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if ("deleteMovie".equals(type)) {
            holder.mControlMovieBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle(context.getString(R.string.deleteMovieConfirmation));
                    builder.setMessage(context.getString(R.string.deleteMovieConfirmMessage));

                    builder.setPositiveButton(context.getString(R.string.confirmAction), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            MovieDeleter m = new MovieDeleter(currentMovie.getId(), listID);
                            try {
                                m.execute().get();
                                movies.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
                                Log.e("e", String.valueOf(holder.getAdapterPosition()));
                                Toast.makeText(context.getApplicationContext(), context.getString(R.string.movie_deleted), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton(context.getString(R.string.cancelAction), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        // Create a Log that the method is finished
        Log.i(TAG, "onBindViewHolder method finished!");
    }

    // Return the size from the movies
    @Override
    public int getItemCount() {
        // Create a Log that the method is finished
        Log.i(TAG, "getItemCount method finished!");
        // Return the size of movies
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Localize views
        public final ImageView mMovieImage;
        public final TextView mMovieTitle;
        public final TextView mMovieReleaseDate;
        public final TextView mMovieGenres;
        public final TextView mMovieRating;
        public ImageButton mControlMovieBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieImage = itemView.findViewById(R.id.Movie_list_item_image);
            mMovieTitle = itemView.findViewById(R.id.Movie_list_item_title);
            mMovieReleaseDate = itemView.findViewById(R.id.Movie_list_item_release_date);
            mMovieGenres = itemView.findViewById(R.id.Movie_list_item_genres);
            mMovieRating = itemView.findViewById(R.id.Movie_list_item_rating);

            if ("addMovie".equals(type)) {
                mControlMovieBtn = itemView.findViewById(R.id.Movie_list_item_control);
                mControlMovieBtn.setImageResource(R.drawable.ic_add);
            }

            if ("deleteMovie".equals(type)) {
                mControlMovieBtn = itemView.findViewById(R.id.Movie_list_item_control);
            }
        }
    }

    public class MovieAdder extends AsyncTask<Void, Void, String> {
        private int movieID;
        private String listID;

        public MovieAdder(int movieID, String listID) {
            this.movieID = movieID;
            this.listID = listID;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.controlMovieFromList("add", this.listID, this.movieID);
        }
    }

    public class MovieDeleter extends AsyncTask<Void, Void, String> {
        private int movieID;
        private String listID;

        public MovieDeleter(int movieID, String listID) {
            this.movieID = movieID;
            this.listID = listID;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.controlMovieFromList("delete", this.listID, this.movieID);
        }
    }


    // SearchView Filter
    public void filterList(ArrayList<Movie> filteredList) {
        movies = filteredList;
        notifyDataSetChanged();
    }
}