package com.teamc11.MovieApp.domain;

import java.io.Serializable;

public class Author implements Serializable {
    // Attributes
    private String name;
    private String username;
    private String avatarPath;
    private Integer rating;

    // Constructor
    public Author(String name, String username, String avatarPath, Integer rating) {
        this.name = name;
        this.username = username;
        this.avatarPath = avatarPath;
        this.rating = rating;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public int getRating() {
        return rating;
    }

}