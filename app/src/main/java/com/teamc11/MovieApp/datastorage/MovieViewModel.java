package com.teamc11.MovieApp.datastorage;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.teamc11.MovieApp.domain.Movie;
import com.teamc11.MovieApp.domain.MovieDetailed;
import com.teamc11.MovieApp.domain.User;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private List<Movie> movieList;
    private List<MovieDetailed> movieDetailedList;
    private List<User> userList;

    public MovieViewModel(Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        movieList = movieRepository.getMovieList();
        movieDetailedList = movieRepository.getMovieDetailedList();
        userList = movieRepository.getUserList();
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void insert(Movie movie) {
        movieRepository.insert(movie);
    }

    public void deleteAll() {
        movieRepository.deleteAll();
    }

    public List<MovieDetailed> getMovieDetailedList() {
        return movieDetailedList;
    }

    public void insertDetailed(MovieDetailed movieDetailed) {
        movieRepository.insertDetailed(movieDetailed);
    }

    public void deleteAllDetailed() {
        movieRepository.deleteAllDetailed();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void insertUser(User user) {
        movieRepository.insertUser(user);
    }

    public void deleteAllUsers() {
        movieRepository.deleteAllUsers();
    }
}