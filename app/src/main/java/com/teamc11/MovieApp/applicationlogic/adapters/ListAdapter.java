package com.teamc11.MovieApp.applicationlogic.adapters;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.datastorage.NetworkUtils;
import com.teamc11.MovieApp.domain.List;
import com.teamc11.MovieApp.domain.Movie;
import com.teamc11.MovieApp.presentation.list.ListDetailActivity;
import com.teamc11.MovieApp.presentation.LoginActivity;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private final String TAG = ListAdapter.class.getSimpleName();
    private ArrayList<List> lists;
    private LayoutInflater inflater;
    private Context context;

    // Constructor to create a ListAdapter
    public ListAdapter(Context context, ArrayList<List> lists) {
        this.inflater = LayoutInflater.from(context);
        this.lists = lists;
        this.context = context;
        // Create a Log that the method is finished
        Log.i(TAG, "ListAdapter constructor finished!");
    }

    // Inflate the item layout and return the ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Assign the list_item layout to the View item
        View listItem = inflater.inflate(R.layout.list_item, parent, false);
        // Create a Log that the method is finished
        Log.i(TAG, "onCreateViewHolder method finished!");
        // Return ViewHolder
        return new ViewHolder(listItem);
    }

    // Connect the data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current List
        List currentList = (List) lists.get(position);

        // Assign the values to the TextViews in the holder
        holder.mListName.setText(currentList.getName());
        holder.mListDescription.setText(currentList.getDescription());
        holder.list = currentList;

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(context.getString(R.string.deleteListConfirmation));
                builder.setMessage(context.getString(R.string.deleteListConfirmMessage));

                builder.setPositiveButton(context.getString(R.string.confirmAction), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        ListDeleter l = new ListDeleter(currentList.getId());
                        try {
                            l.execute().get();
                            lists.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                            Toast.makeText(context.getApplicationContext(), context.getString(R.string.list_deleted), Toast.LENGTH_SHORT).show();
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

        holder.mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder listText = new StringBuilder();
                listText.append("List name: ");
                listText.append(currentList.getName());
                listText.append("\n");
                listText.append("Description: ");
                listText.append(currentList.getDescription());
                listText.append("\n");
                listText.append("Created by: ");
                listText.append(currentList.getCreatedBy());
                listText.append("\n");
                listText.append("Movies: ");
                listText.append("\n");

                for(int i = 0; i < currentList.getMovies().size(); i++) {
                    listText.append(" -");
                    listText.append(currentList.getMovies().get(i).getTitle());

                    if(!(i == currentList.getMovies().size() - 1)) {
                        listText.append("\n");
                    }
                }

                String mimeType = "text/plain";
                ShareCompat.IntentBuilder
                        .from((Activity) context)
                        .setType(mimeType)
                        .setChooserTitle(R.string.share_text_with)
                        .setText(listText.toString())
                        .startChooser();
            }
        });

        //Add an onclick on each item to navigate to the detail page
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)  {
                //make intent
                Intent intent = new Intent(view.getContext(), ListDetailActivity.class);

                //set list used for list details
                ListDetailActivity.list = currentList;

                //start intent
                context.startActivity(intent);

                //log intent
                Log.d(TAG, intent.toString());
            }
        });
        // Create a Log that the method is finished
        Log.i(TAG, "onBindViewHolder method finished!");
    }

    // Return the size from the lists
    @Override
    public int getItemCount() {
        // Create a Log that the method is finished
        Log.i(TAG, "getItemCount method finished!");
        // Return the size of lists
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Localize views
        public final TextView mListName;
        public final TextView mListDescription;
        public final ImageButton mDeleteButton;
        public final ImageButton mShareButton;
        List list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mListName = itemView.findViewById(R.id.List_item_name);
            mListDescription = itemView.findViewById(R.id.List_item_description);
            mDeleteButton = itemView.findViewById(R.id.deleteListBtn);
            mShareButton = itemView.findViewById(R.id.shareListBtn);
        }
    }

    public class ListDeleter extends AsyncTask<Void, Void, String> {
        private String id;

        public ListDeleter(String id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.deleteList(this.id, LoginActivity.user.getSessionID());
        }
    }

    public void filter(ArrayList<List> filteredList) {
        lists = filteredList;
        notifyDataSetChanged();
    }

}