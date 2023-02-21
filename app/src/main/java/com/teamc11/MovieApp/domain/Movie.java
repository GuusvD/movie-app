package com.teamc11.MovieApp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "movie_table")
public class Movie implements Serializable, Watchable {

    @PrimaryKey
    private int id;

    private boolean adult;
    private String backdropPath;
    private ArrayList<Integer> genreIDs;
    private ArrayList<Genre> genres;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
    private String releaseDate;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;
    private byte[] posterPathByte;

    public Movie(boolean adult, String backdropPath,
                 int id, ArrayList<Integer> genreIDs, String originalLanguage, String originalTitle,
                 String overview, double popularity, String posterPath, String releaseDate, String title, boolean video,
                 double voteAverage, int voteCount, byte[] posterPathByte) {
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIDs = genreIDs;
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.posterPathByte = posterPathByte;
    }

    //getters
    public boolean isAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return this.backdropPath;
    }

    public void setGenreIDs(ArrayList<Integer> genreIDs) {
        this.genreIDs = genreIDs;
    }

    public byte[] getPosterPathByte() {
        return posterPathByte;
    }

    public void setPosterPathByte(byte[] posterPathPathByte) {
        this.posterPathByte = posterPathPathByte;
    }

    public ArrayList<Integer> getGenreIDs() {
        return genreIDs;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getText() {
        return originalTitle +  " " + title;
    }

    public String getAllGenres() {
        StringBuilder genresTemp = new StringBuilder();
        int listSize = this.genres.size();

        if (listSize > 0) {
            for (int i = 0; i < listSize; i++) {
                genresTemp.append(this.genres.get(i).getName());

                if (i != (listSize - 1)) {
                    genresTemp.append(", ");
                }
            }
        }
        return genresTemp.toString();
    }

    //setters
    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return  "\n" + "Is for Adults: " + isAdult() + "\n" +
                "Backdrop Path: " + getBackdropPath() + "\n" +
                "Genres: " + getAllGenres() + "\n" +
                "ID: " + getId() + "\n" +
                "Original Language: " + getOriginalLanguage() + "\n" +
                "Original Title: " + getOriginalTitle() + "\n" +
                "Synopsis: " + getOverview() + "\n" +
                "Popularity: " + getPopularity() + "\n" +
                "Poster Path: " + getPosterPath() + "\n" +
                "Release Date: " + getReleaseDate() + "\n" +
                "Title: " + getTitle() + "\n" +
                "Video available: " + isVideo() + "\n" +
                "Vote Average: " + getVoteAverage() + "\n" +
                "Vote Count: " + getVoteCount();
    }
}