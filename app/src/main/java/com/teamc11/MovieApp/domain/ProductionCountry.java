package com.teamc11.MovieApp.domain;

import java.io.Serializable;

public class ProductionCountry implements Serializable {
     // Attributes
    private String isoCountry;
    private String name;

    // Constructor
    public ProductionCountry(String isoCountry, String name) {
        this.isoCountry = isoCountry;
        this.name = name;
    }

    // Getters
    public String getIsoCountry() {
        return isoCountry;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}