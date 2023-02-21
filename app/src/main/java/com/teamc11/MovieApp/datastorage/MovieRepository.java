package com.teamc11.MovieApp.datastorage;

import android.app.Application;
import android.os.AsyncTask;

import com.teamc11.MovieApp.domain.Movie;
import com.teamc11.MovieApp.domain.MovieDetailed;
import com.teamc11.MovieApp.domain.User;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private final MovieDAO movieDAO;
    private final MovieDetailedDAO movieDetailedDAO;
    private final UserDAO userDAO;

    public MovieRepository(Application application) {
        MovieRoomDatabase database = MovieRoomDatabase.getDatabase(application);
        movieDAO = database.movieDAO();
        movieDetailedDAO = database.movieDetailedDAO();
        userDAO = database.userDAO();
    }

    public List<Movie> getMovieList() {
        getAsyncTask getAsyncTask = new getAsyncTask(movieDAO);
        List<Movie> movieList = new ArrayList<>();

        try {
            movieList = getAsyncTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }

    public void insert(Movie movie) {
        new insertAsyncTask(movieDAO).execute(movie);
    }

    public void deleteAll() {
        new deleteAsyncTask(movieDAO).execute();
    }

    public List<MovieDetailed> getMovieDetailedList() {
        getDetailedAsyncTask getDetailedAsyncTask = new getDetailedAsyncTask(movieDetailedDAO);
        List<MovieDetailed> movieDetailedList = new ArrayList<>();

        try {
            movieDetailedList = getDetailedAsyncTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieDetailedList;
    }

    public void insertDetailed(MovieDetailed movieDetailed) {
        new insertDetailedAsyncTask(movieDetailedDAO).execute(movieDetailed);
    }

    public void deleteAllDetailed() {
        new deleteDetailedAsyncTask(movieDetailedDAO).execute();
    }

    public List<User> getUserList() {
        getUserAsyncTask getUserAsyncTask = new getUserAsyncTask(userDAO);
        List<User> userList = new ArrayList<>();

        try {
            userList = getUserAsyncTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void insertUser(User user) {
        new insertUserAsyncTask(userDAO).execute(user);
    }

    public void deleteAllUsers() {
        new deleteUsersAsyncTask(userDAO).execute();
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDAO movieDAO;

        insertAsyncTask(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDAO.insert(movies[0]);
            return null;
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<Movie>> {
        private MovieDAO movieDAO;

        getAsyncTask(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return movieDAO.getAllMovies();
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieDAO movieDAO;

        deleteAsyncTask(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieDAO.deleteAll();
            return null;
        }
    }

    private static class insertDetailedAsyncTask extends AsyncTask<MovieDetailed, Void, Void> {
        private MovieDetailedDAO movieDetailedDAO;

        insertDetailedAsyncTask(MovieDetailedDAO movieDetailedDAO) {
            this.movieDetailedDAO = movieDetailedDAO;
        }

        @Override
        protected Void doInBackground(MovieDetailed... movieDetailed) {
            movieDetailedDAO.insert(movieDetailed[0]);
            return null;
        }
    }

    private static class getDetailedAsyncTask extends AsyncTask<Void, Void, List<MovieDetailed>> {
        private MovieDetailedDAO movieDetailedDAO;

        getDetailedAsyncTask(MovieDetailedDAO movieDetailedDAO) {
            this.movieDetailedDAO = movieDetailedDAO;
        }

        @Override
        protected List<MovieDetailed> doInBackground(Void... voids) {
            return movieDetailedDAO.getAll();
        }
    }

    private static class deleteDetailedAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieDetailedDAO movieDetailedDAO;

        deleteDetailedAsyncTask(MovieDetailedDAO movieDetailedDAO) {
            this.movieDetailedDAO = movieDetailedDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieDetailedDAO.deleteAll();
            return null;
        }
    }

    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        insertUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.insert(users[0]);
            return null;
        }
    }

    private static class getUserAsyncTask extends AsyncTask<Void, Void, List<User>> {
        private UserDAO userDAO;

        getUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDAO.getAllUsers();
        }
    }

    private static class deleteUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDAO userDAO;

        deleteUsersAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDAO.deleteAll();
            return null;
        }
    }
}