package com.teamc11.MovieApp.domain;

import java.io.Serializable;

public class ProductionCompany implements Serializable {
    // Attributes
    private String name;
    private int id;
    private String logoPath;
    private String originCountry;

    // Constructor
    public ProductionCompany(String name, int id, String logoPath, String originCountry) {
        this.name = name;
        this.id = id;
        this.logoPath = logoPath;
        this.originCountry = originCountry;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    @Override
    public String toString() {
        return getName();
    }

}