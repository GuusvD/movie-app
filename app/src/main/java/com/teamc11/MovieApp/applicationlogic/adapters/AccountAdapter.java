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
import com.teamc11.MovieApp.domain.Rating;
import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private final String TAG = AccountAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private ArrayList<Rating> ratings;
    private Context context;

    // Constructor to create a AccountAdapter
    public AccountAdapter(Context context, ArrayList<Rating> ratings) {
        this.inflater = LayoutInflater.from(context);
        this.ratings = ratings;
        this.context = context;
        // Create a Log that the method is finished
        Log.i(TAG, "AccountAdapter constructor finished!");
    }

    // Inflate the item layout and return the ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Assign the rating_item layout to the View item
        View ratingItem = inflater.inflate(R.layout.account_rating_item, parent, false);
        // Create a Log that the method is finished
        Log.i(TAG, "onCreateViewHolder method finished!");
        // Return ViewHolder
        return new ViewHolder(ratingItem);
    }

    // Connect the data to the AccountHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current Rating
        Rating currentRating = (Rating) ratings.get(position);

        // Assign the values to the TextViews in the holder
        holder.mAccountRatingTitle.setText(currentRating.getTitle());
        holder.mAccountRating.setText(context.getString(R.string.rating_label) + " " + String.valueOf(currentRating.getRating()));

        // Create a Log that the method is finished
        Log.i(TAG, "onBindViewHolder method finished!");
    }

    // Return the size from the ratings
    @Override
    public int getItemCount() {
        // Create a Log that the method is finished
        Log.i(TAG, "getItemCount method finished!");
        // Return the size of ratings
        return ratings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Localize views
        public final TextView mAccountRatingTitle;
        public final TextView mAccountRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAccountRatingTitle = itemView.findViewById(R.id.account_rating_title);
            mAccountRating = itemView.findViewById(R.id.account_rating);
        }
    }

}