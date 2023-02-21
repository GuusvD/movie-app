package com.teamc11.MovieApp.applicationlogic.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.domain.Review;
import java.util.ArrayList;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {
    private final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Review> reviews;
    private LayoutInflater inflater;
    private Context context;

    // Constructor to create a MovieReviewAdapter
    public MovieReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.inflater = LayoutInflater.from(context);
        this.reviews = reviews;
        this.context = context;
        // Create a Log that the method is finished
        Log.i(TAG, "MovieReviewAdapter constructor finished!");
    }

    // A class that holds the movies
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mMovieReviewAuthorName;
        private TextView mMovieReviewContent;
        private MovieReviewAdapter adapter;

        // Constructor to create a ViewHolder
        public ViewHolder(@NonNull View itemView, MovieReviewAdapter adapter) {
            super(itemView);
            mMovieReviewAuthorName = itemView.findViewById(R.id.account_rating_title);
            mMovieReviewContent = itemView.findViewById(R.id.account_rating);
            this.adapter = adapter;
            // Create a Log that the method is finished
            Log.i(TAG, "MovieHolder constructor finished!");
        }
    }

    // Inflate the item layout and return the ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Assign the movie_review_item layout to the View item
        View movieReviewItem = inflater.inflate(R.layout.movie_review_item, parent, false);
        // Create a Log that the method is finished
        Log.i(TAG, "onCreateViewHolder method finished!");
        // Return MovieHolder
        return new ViewHolder(movieReviewItem, this);
    }

    // Connect the data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current Movie
        Review currentMovieReview = (Review) reviews.get(position);
        // Assign the values to the TextViews in the holder
        holder.mMovieReviewAuthorName.setText(context.getString(R.string.author_label)  + " " + currentMovieReview.getAuthorName());
        holder.mMovieReviewContent.setText(currentMovieReview.getContent());

        // Create a Log that the method is finished
        Log.i(TAG, "onBindViewHolder method finished!");
    }

    // Return the size from the movies
    @Override
    public int getItemCount() {
        // Create a Log that the method is finished
        Log.i(TAG, "getItemCount method finished!");
        // Return the size of movies
        return reviews.size();
    }
}