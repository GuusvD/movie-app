package com.teamc11.MovieApp.datastorage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.teamc11.MovieApp.domain.Movie;
import com.teamc11.MovieApp.domain.MovieDetailed;
import com.teamc11.MovieApp.domain.User;

@Database(entities = {Movie.class, MovieDetailed.class, User.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MovieRoomDatabase extends RoomDatabase {
    public abstract MovieDAO movieDAO();
    public abstract MovieDetailedDAO movieDetailedDAO();
    public abstract UserDAO userDAO();

    private static MovieRoomDatabase INSTANCE;

    public static MovieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MovieRoomDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}