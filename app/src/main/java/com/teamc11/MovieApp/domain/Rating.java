package com.teamc11.MovieApp.domain;

import java.io.Serializable;

public class Rating implements Serializable {
    private int id;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_date;
    private double popularity;
    private String title;
    private boolean video;
    private double vote_average;
    private int vote_count;
    private double rating;

    public Rating(int id, String original_language, String original_title, String overview, String release_date, double popularity, String title, boolean video, double vote_average, int vote_count, double rating) {
        this.id = id;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
        this.popularity = popularity;
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public double getRating() {
        return rating;
    }
}
