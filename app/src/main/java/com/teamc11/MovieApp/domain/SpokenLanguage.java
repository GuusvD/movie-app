package com.teamc11.MovieApp.domain;

import java.io.Serializable;

public class SpokenLanguage implements Serializable {
    // Attributes
    private String englishName;
    private String isoLanguage;
    private String name;

    // Constructor
    public SpokenLanguage(String englishName, String isoLanguage, String name) {
        this.englishName = englishName;
        this.isoLanguage = isoLanguage;
        this.name = name;
    }

    // Getters
    public String getEnglishName() { return englishName; }

    public String getIsoLanguage() {
        return isoLanguage;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getEnglishName();
    }
}