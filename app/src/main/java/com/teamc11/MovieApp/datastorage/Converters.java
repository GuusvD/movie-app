package com.teamc11.MovieApp.datastorage;

import androidx.room.TypeConverter;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.teamc11.MovieApp.domain.Genre;
import com.teamc11.MovieApp.domain.ProductionCompany;
import com.teamc11.MovieApp.domain.ProductionCountry;
import com.teamc11.MovieApp.domain.Review;
import com.teamc11.MovieApp.domain.SpokenLanguage;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public ArrayList<Integer> fromInteger(String string) {
        Type listType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public String fromIntegerArrayList(ArrayList<Integer> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public ArrayList<Genre> fromGenre(String string) {
        Type listType = new TypeToken<ArrayList<Genre>>() {
        }.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public String fromGenreArrayList(ArrayList<Genre> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public ArrayList<ProductionCompany> fromProductionCompany(String string) {
        Type listType = new TypeToken<ArrayList<ProductionCompany>>() {
        }.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public String fromProductionCompanyArrayList(ArrayList<ProductionCompany> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public ArrayList<ProductionCountry> fromProductionCountry(String string) {
        Type listType = new TypeToken<ArrayList<ProductionCountry>>() {
        }.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public String fromProductionCountryArrayList(ArrayList<ProductionCountry> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public ArrayList<SpokenLanguage> fromSpokenLanguage(String string) {
        Type listType = new TypeToken<ArrayList<SpokenLanguage>>() {
        }.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public String fromSpokenLanguageArrayList(ArrayList<SpokenLanguage> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public ArrayList<Review> fromReview(String string) {
        Type listType = new TypeToken<ArrayList<Review>>() {
        }.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public String fromReviewArrayList(ArrayList<Review> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}