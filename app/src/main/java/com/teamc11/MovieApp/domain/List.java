package com.teamc11.MovieApp.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class List implements Serializable {
    private String id;
    private String name;
    private String description;
    private String posterPath;
    private String createdBy;
    private ArrayList<Movie> movies;
    private int itemCount;
    private int favoriteCount;
    private String iso_639_1;
    private double averageRating;

    public List(String id, String name, String description, String posterPath, String createdBy, ArrayList<Movie> movies, int itemCount, int favoriteCount, String iso_639_1) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.posterPath = posterPath;
        this.createdBy = createdBy;
        this.movies = movies;
        this.itemCount = itemCount;
        this.favoriteCount = favoriteCount;
        this.iso_639_1 = iso_639_1;
    }

    public List(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}