package com.teamc11.MovieApp.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "movieDetailed_table")
public class MovieDetailed extends Movie implements Serializable, Watchable{
    private int budget;

    @ColumnInfo(name = "genresDetailed")
    private ArrayList<Genre> genres;
    private String homepage;
    private String imdbId; // Length: 9 && pattern: ^tt[0-9]{7}
    private ArrayList<ProductionCompany> productionCompaniesList;
    private ArrayList<ProductionCountry> productionCountriesList;
    private int revenue;
    private int runtime;
    private ArrayList<SpokenLanguage> spokenLanguagesList;
    private String status; // Allowed Values: Rumored, Planned, In Production, Post Production, Released, Canceled
    private String tagline;
    private String trailer;
    private ArrayList<Review> reviews;
    private String genresString;

    public MovieDetailed(boolean adult, int budget, String backdropPath, ArrayList<Genre> genres, String homepage,
                 int id, ArrayList<Integer> genreIDs,  String imdbId, String originalLanguage, String originalTitle,
                 String overview, double popularity, String posterPath, ArrayList<ProductionCompany> productionCompaniesList,
                 ArrayList<ProductionCountry> productionCountriesList, String releaseDate, int revenue, int runtime,
                 ArrayList<SpokenLanguage> spokenLanguagesList, String status, String tagline, String title, boolean video,
                 double voteAverage, int voteCount, byte[] posterPathByte, String genresString) {

        super(adult, backdropPath, id, genreIDs,  originalLanguage, originalTitle, overview,
                popularity, posterPath, releaseDate,title, video, voteAverage, voteCount, posterPathByte);

        //detailed
        this.budget = budget;
        this.genres = genres;
        this.homepage = homepage;
        this.imdbId = imdbId;
        this.productionCompaniesList = productionCompaniesList;
        this.productionCountriesList = productionCountriesList;
        this.revenue = revenue;
        this.runtime = runtime;
        this.spokenLanguagesList = spokenLanguagesList;
        this.status = status;
        this.tagline = tagline;
        this.reviews = new ArrayList<>();
        this.genresString = genresString;
    }

    // Getters
    public int getBudget() {
        return budget;
    }

    public ArrayList<Genre> getGenresList() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getTrailer() {
        return this.trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public ArrayList<ProductionCompany> getProductionCompaniesList() {
        return productionCompaniesList;
    }

    public ArrayList<ProductionCountry> getProductionCountriesList() {
        return productionCountriesList;
    }

    public String getGenresString() {
        return genresString;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public ArrayList<SpokenLanguage> getSpokenLanguagesList() {
        return spokenLanguagesList;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getAllProductionCompanies() {
        StringBuilder productionCompaniesTemp = new StringBuilder();
        int listSize = this.productionCompaniesList.size();

        if (listSize > 0) {
            for (int i = 0; i < listSize; i++) {
                productionCompaniesTemp.append(this.productionCompaniesList.get(i).toString());

                if (i != (listSize - 1)) {
                    productionCompaniesTemp.append(", ");
                }
            }
        }
        return productionCompaniesTemp.toString();
    }

    public String getAllProductionCountries() {
        StringBuilder productionCountriesTemp = new StringBuilder();
        int listSize = this.productionCountriesList.size();

        if (listSize > 0) {
            for (int i = 0; i < listSize; i++) {
                productionCountriesTemp.append(this.productionCountriesList.get(i).toString());

                if (i != (listSize - 1)) {
                    productionCountriesTemp.append(", ");
                }
            }
        }
        return productionCountriesTemp.toString();
    }

    public String getAllSpokenLanguages() {
        StringBuilder spokenLanguagesTemp = new StringBuilder();
        int listSize = this.spokenLanguagesList.size();

        if (listSize > 0) {
            for (int i = 0; i < listSize; i++) {
                spokenLanguagesTemp.append(this.spokenLanguagesList.get(i).toString());

                if (i != (listSize - 1)) {
                    spokenLanguagesTemp.append(", ");
                }
            }
        }
        return spokenLanguagesTemp.toString();
    }

    public String getRuntimeInHoursAndMinutes() {
        int runtime = getRuntime();
        int fullHours = (int) Math.floor(runtime / 60);
        int remainingMinutes = runtime % 60;

        return String.format("%sh %sm", fullHours, remainingMinutes);
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

    @Override
    public String toString() {
        return super.toString() + "\n" +  "Budget=" + getBudget() + "\n" +
                "Genres: " + getAllGenres() + "\n" +
                "Homepage: " + getHomepage() + "\n" +
                "IMDB site: " + getImdbId() + "\n" +
                "Production Companies: " + getAllProductionCompanies() + "\n" +
                "Production Countries: " + getAllProductionCountries() + "\n" +
                "Revenue: " + getRevenue() + "\n" +
                "Duration/Runtime: " + getRuntimeInHoursAndMinutes() + "\n" +
                "Spoken Languages: " + getAllSpokenLanguages() + "\n" +
                "Status: " + getStatus() + "\n" +
                "Tagline: " + getTagline() + "\n" +
                "Trailer: " + getTrailer() + "\n" +
                "Reviews: " + getReviews();
    }

}