package com.teamc11.MovieApp.applicationlogic.loaders.movie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.ByteArrayBuffer;
import com.teamc11.MovieApp.datastorage.NetworkUtils;
import com.teamc11.MovieApp.domain.Author;
import com.teamc11.MovieApp.domain.Genre;
import com.teamc11.MovieApp.domain.Movie;
import com.teamc11.MovieApp.domain.MovieDetailed;
import com.teamc11.MovieApp.domain.ProductionCompany;
import com.teamc11.MovieApp.domain.ProductionCountry;
import com.teamc11.MovieApp.domain.Review;
import com.teamc11.MovieApp.domain.SpokenLanguage;
import com.teamc11.MovieApp.domain.Watchable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MovieLoader extends AsyncTaskLoader<ArrayList<ArrayList<Watchable>>> {
    private Context context;
    private static final String TAG = MovieLoader.class.getSimpleName();
    private String requestType;
    private String searchString;

    // Constructor to create a MovieLoader
    public MovieLoader(Context context, String requestType, String searchString) {
        super(context);
        this.context = context;
        this.requestType = requestType;
        this.searchString = searchString;
        // Create a Log that the method is finished
        Log.i(TAG, "MovieLoader constructor finished!");
    }

    // Load everything before the start
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // Force the loader to load
        forceLoad();
        // Create a Log that the method is finished
        Log.i(TAG, "onStartLoading method finished!");
    }

    // Processing the retrieveNeededJSON in the background to get the results
    @Nullable
    @Override
    public ArrayList<ArrayList<Watchable>> loadInBackground() {
        ArrayList<ArrayList<Watchable>> moviesAndDetailedList = new ArrayList<>();

        //list with json strings
        ArrayList<String> jsonArray = new ArrayList<>();

        //retrieve all movies from the selected page
        switch (this.requestType) {
            case "discover":
                jsonArray.add(NetworkUtils.retrieveNeededJSON("discover", null, null, null));
                break;
            case "search":
                jsonArray.add(NetworkUtils.retrieveNeededJSON("search", null, null, this.searchString));
                break;
        }

        //retrieve list of all genres
        jsonArray.add(NetworkUtils.retrieveNeededJSON("genres", null, null, null));

        // return list of movies id's
        ArrayList<Movie> moviesTemp = getAllNormalMovies(jsonArray.get(0));

        //add list with genre names to each movie using the genre id's
        alterMoviesGenres(moviesTemp, parseGenres(jsonArray.get(1)));

        Log.d(TAG, "Normal movies filled");

        //transfer all movies to watchable list
        ArrayList<Watchable> movies = new ArrayList<>();

        for (Movie m : moviesTemp) {
            movies.add(m);
        }

        //add movies array to array of arrays<movie>
        moviesAndDetailedList.add(movies);

        ArrayList<MovieDetailed> detailedMoviesTemp = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            int id = movies.get(i).getId();
            //fil movie with information
            MovieDetailed newMovie = getMovieWithDetailedInformation(NetworkUtils.retrieveNeededJSON("details", id, null, null));
            //add trailer
            newMovie.setTrailer(parseTrailer(NetworkUtils.retrieveNeededJSON("trailer", id, null, null)));
            //add reviews to movie
            newMovie.setReviews(getAllReviews(NetworkUtils.retrieveNeededJSON("review", id, null, null)));
            //add detailedMovie to list
            detailedMoviesTemp.add(newMovie);
        }

        //transfer all movies to watchable list
        ArrayList<Watchable> detailedMovies = new ArrayList<>();

        for (MovieDetailed m : detailedMoviesTemp) {
            detailedMovies.add(m);
        }

        //add detailedMovies to list
        moviesAndDetailedList.add(detailedMovies);

        // Create a Log that the method is finished
        Log.i(TAG, "loadInBackground method finished!");

        // Return list of movies
        return moviesAndDetailedList;
    }

    public static String parseTrailer(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject tempObj = resultsArray.getJSONObject(i);

                if ("Trailer".equals(tempObj.getString("type"))) {
                    return tempObj.getString("key");
                }
            }

            if (!resultsArray.isNull(0)) {
                return resultsArray.getJSONObject(0).getString("key");
            } else {
                return "dQw4w9WgXcQ";
            }

        } catch (Exception e) {
            // Create a Log that something went wrong
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] imageUrlToBytes(String data) {
        try {
            URL imageUrl = new URL(data);
            URLConnection urlConnection = imageUrl.openConnection();

            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(500);

            int current = 0;
            while ((current = bufferedInputStream.read()) != -1) {
                byteArrayBuffer.append((byte) current);
            }

            return byteArrayBuffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Review> getAllReviews(String data) {
        ArrayList<Review> reviewsTemp = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject review = resultsArray.getJSONObject(i);

                String authorName = review.getString("author");

                //author
                JSONObject authorDetailsObject = review.getJSONObject("author_details");

                //get values from author details
                String name = authorDetailsObject.getString("name");
                String username = authorDetailsObject.getString("username");
                String avatarPath = authorDetailsObject.getString("avatar_path");
                Integer rating = authorDetailsObject.optInt("rating");

                String content = review.getString("content");
                String createdAt = review.getString("created_at");
                String id = review.getString("id");
                String updatedAt = review.getString("updated_at");
                String url = review.getString("url");

                //add review to temporary list
                if (rating > 0.5) {
                    reviewsTemp.add(new Review(id, authorName, new Author(name, username, avatarPath, rating), content, createdAt, updatedAt, url));
                }
            }

            //return all reviews for movie
            return reviewsTemp;

        } catch (Exception e) {
            // Create a Log that something went wrong
            Log.e(TAG, "Something went wrong! (getMovieWithDetailedInformation)");
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Movie> getAllNormalMovies(String data) {
        //array for id's
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            // Get the results array
            JSONObject jsonObject = new JSONObject(data);
            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject movieInfo = resultsArray.getJSONObject(i);

                boolean adult = movieInfo.getBoolean("adult");

                JSONArray idArray = movieInfo.getJSONArray("genre_ids");

                ArrayList<Integer> genreIDs = new ArrayList<>();

                for (int j = 0; j < idArray.length(); j++) {
                    genreIDs.add(idArray.getInt(j));
                }

                int id = movieInfo.getInt("id");
                String originalLanguage = movieInfo.getString("original_language");
                String originalTitle = movieInfo.getString("original_title");
                String overview = movieInfo.getString("overview");
                Double popularity = movieInfo.getDouble("popularity");
                String posterPath = "https://image.tmdb.org/t/p/original" + movieInfo.getString("poster_path");
                String backdropPath = "https://image.tmdb.org/t/p/original" + movieInfo.getString("backdrop_path");

                String releaseDate = movieInfo.getString("release_date");
                String title = movieInfo.getString("title");
                boolean video = movieInfo.getBoolean("video");
                double voteAverage = movieInfo.getDouble("vote_average");
                int voteCount = movieInfo.getInt("vote_count");

                if (posterPath.equals("https://image.tmdb.org/t/p/original/null")) {
                    posterPath = "https://wallup.net/wp-content/uploads/2015/12/260850-landscape-nature-path-bamboo-trees-forest-748x499.jpg";
                }

                if (backdropPath.equals("https://image.tmdb.org/t/p/original/null")) {
                    backdropPath = "https://wallup.net/wp-content/uploads/2015/12/260850-landscape-nature-path-bamboo-trees-forest-748x499.jpg";
                }

                byte[] posterPathByte = imageUrlToBytes(posterPath);

                //add id to list
                movies.add(new Movie(adult, backdropPath, id, genreIDs, originalLanguage,
                        originalTitle, overview, popularity, posterPath, releaseDate, title,
                        video, voteAverage, voteCount, posterPathByte));
            }

            return movies;

        } catch (Exception e) {
            // Create a Log that something went wrong
            Log.e(TAG, "Something went wrong!");
            e.printStackTrace();
        }
        // Return the Movie ArrayList
        return null;
    }

    // Assign JSON String to a Movie and store them in the Movie ArrayList
    public static MovieDetailed getMovieWithDetailedInformation(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            boolean adult = jsonObject.getBoolean("adult");
            int budget = jsonObject.getInt("budget");

            //list with genres
            ArrayList<Genre> genres = new ArrayList<>();
            ArrayList<Integer> genreIDs = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();

            //get genresArray
            JSONArray genresArray = jsonObject.getJSONArray("genres");

            //for all objects in genres array
            for (int l = 0; l < genresArray.length(); l++) {
                //get current object
                JSONObject genre = genresArray.getJSONObject(l);

                //get objects values
                int genreID = genre.getInt("id");
                String genreName = genre.getString("name");

                //add genre to genres list
                genres.add(new Genre(genreID, genreName));
                genreIDs.add(genreID);

                if (l == genresArray.length() - 1) {
                    stringBuilder.append(genreName + ".");
                } else {
                    stringBuilder.append(genreName + ", ");
                }
            }

            String homepage = jsonObject.getString("homepage");
            int id = jsonObject.getInt("id");
            String imdb_id = jsonObject.getString("imdb_id");
            String originalLanguage = jsonObject.getString("original_language");
            String originalTitle = jsonObject.getString("original_title");
            String overview = jsonObject.getString("overview");
            Double popularity = jsonObject.getDouble("popularity");
            String posterPath = "https://image.tmdb.org/t/p/original" + jsonObject.getString("poster_path");
            String backdropPath = "https://image.tmdb.org/t/p/original" + jsonObject.getString("backdrop_path");

            //list with production companies
            ArrayList<ProductionCompany> productionCompanies = new ArrayList<>();

            //get genresArray
            JSONArray productionCompaniesArray = jsonObject.getJSONArray("production_companies");

            //for all objects in genres array
            for (int k = 0; k < productionCompaniesArray.length(); k++) {
                //get current object
                JSONObject productionCompany = productionCompaniesArray.getJSONObject(k);

                //get objects values
                int productionCompanyID = productionCompany.getInt("id");
                String logoPath = productionCompany.getString("logo_path");
                String productionCompanyName = productionCompany.getString("name");
                String originCountry = productionCompany.getString("origin_country");

                //add genre to genres list
                productionCompanies.add(new ProductionCompany(productionCompanyName, productionCompanyID, logoPath, originCountry));
            }

            //list with production countries
            ArrayList<ProductionCountry> productionCountries = new ArrayList<>();

            //get genresArray
            JSONArray productionCountriesArray = jsonObject.getJSONArray("production_countries");

            //for all objects in genres array
            for (int j = 0; j < productionCountriesArray.length(); j++) {
                //get current object
                JSONObject productionCountry = productionCountriesArray.getJSONObject(j);

                //get objects values
                String productionCountryISO = productionCountry.getString("iso_3166_1");
                String productionCompanyName = productionCountry.getString("name");

                //add genre to genres list
                productionCountries.add(new ProductionCountry(productionCountryISO, productionCompanyName));
            }

            String releaseDate = jsonObject.getString("release_date");
            int revenue = jsonObject.getInt("revenue");
            int runtime = jsonObject.getInt("runtime");

            //list with spoken languages
            ArrayList<SpokenLanguage> spokenLanguages = new ArrayList<>();

            //get genresArray
            JSONArray spokenLanguagesArray = jsonObject.getJSONArray("spoken_languages");

            //for all objects in genres array
            for (int m = 0; m < spokenLanguagesArray.length(); m++) {
                //get current object
                JSONObject spokenLanguage = spokenLanguagesArray.getJSONObject(m);

                //get objects values
                String englishName = spokenLanguage.getString("english_name");
                String languageISO = spokenLanguage.getString("iso_639_1");
                String spokenLanguageName = spokenLanguage.getString("name");

                //add genre to genres list
                spokenLanguages.add(new SpokenLanguage(englishName, languageISO, spokenLanguageName));
            }

            String status = jsonObject.getString("status");
            String tagline = jsonObject.getString("tagline");
            String title = jsonObject.getString("title");
            boolean video = jsonObject.getBoolean("video");
            double voteAverage = jsonObject.getDouble("vote_average");
            int voteCount = jsonObject.getInt("vote_count");

            byte[] posterPathByte = imageUrlToBytes(posterPath);

            // Create detailed Movie
            MovieDetailed detailedMovie = new MovieDetailed(adult, budget, backdropPath, genres, homepage, id, genreIDs, imdb_id,
                    originalLanguage, originalTitle, overview, popularity, posterPath, productionCompanies,
                    productionCountries, releaseDate, revenue, runtime, spokenLanguages, status, tagline,
                    title, video, voteAverage, voteCount, posterPathByte, stringBuilder.toString());

            //return more detailed movie
            return detailedMovie;

        } catch (Exception e) {
            // Create a Log that something went wrong
            Log.e(TAG, "Something went wrong! (getMovieWithDetailedInformation)");
            e.printStackTrace();
        }
        return null;
    }

    public static void alterMoviesGenres(ArrayList<Movie> movies, ArrayList<Genre> genresList) {
        for (Movie currentMovie : movies) {
            ArrayList<Genre> genresTemp = new ArrayList<>();
            for (Integer genreID : currentMovie.getGenreIDs()) {
                for (Genre genre : genresList) {
                    if (genre.getId() == genreID) {
                        genresTemp.add(genre);
                    }
                }
            }
            currentMovie.setGenres(genresTemp);
        }
    }

    public static ArrayList<Genre> parseGenres(String data) {
        ArrayList<Genre> genresTemp = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray resultsArray = jsonObject.getJSONArray("genres");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject genre = resultsArray.getJSONObject(i);

                int id = genre.getInt("id");
                String name = genre.getString("name");

                genresTemp.add(new Genre(id, name));
            }

            //return list of genres
            return genresTemp;

        } catch (Exception e) {
            // Create a Log that something went wrong
            Log.e(TAG, "Something went wrong!");
            e.printStackTrace();
        }
        // return null
        return null;
    }
}