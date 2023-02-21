package com.teamc11.MovieApp.domain;

import java.io.Serializable;

public class Genre implements Serializable {
    // Attributes
    private int id;
    private String name;

    // Constructor
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}