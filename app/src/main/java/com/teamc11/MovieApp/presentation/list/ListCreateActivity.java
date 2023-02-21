package com.teamc11.MovieApp.presentation.list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.teamc11.MovieApp.R;
import com.teamc11.MovieApp.applicationlogic.loaders.list.ListCreateLoader;

public class ListCreateActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private final String TAG = ListCreateActivity.class.getSimpleName();
    public static String nameInput;
    public static String descriptionInput;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_create);

        EditText mName = findViewById(R.id.listNameField);
        EditText mDescription = findViewById(R.id.listDescriptionField);
        Button mSubmit = findViewById(R.id.submit_list);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameInput = mName.getText().toString();
                descriptionInput = mDescription.getText().toString();

                if (!nameInput.isEmpty()) {
                    onClickData();
                } else {
                    failedCreate();
                }
            }
        });
    }

    public void onClickData() {
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    public void failedCreate() {
        Toast.makeText(this, this.getString(R.string.create_list_failed), Toast.LENGTH_SHORT).show();
        this.startActivity(new Intent(this, ListCreateActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a Log that the method is finished
        Log.i(TAG, "onCreateLoader method finished!");
        // Return the ListCreateLoader
        return new ListCreateLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String response) {
        if(!"failed".equals(response)) {
            startActivity(new Intent(ListCreateActivity.this, ListActivity.class));
        } else {
            Log.e(TAG, response);
            failedCreate();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}