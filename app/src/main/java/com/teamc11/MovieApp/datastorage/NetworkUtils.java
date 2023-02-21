package com.teamc11.MovieApp.datastorage;

import android.net.Uri;
import android.util.Log;

import com.teamc11.MovieApp.presentation.list.ListCreateActivity;
import com.teamc11.MovieApp.presentation.LoginActivity;
import com.teamc11.MovieApp.presentation.movie.MovieDetailActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//API token=
//b12f4ce69c08c0214bf447763d5cf7ec
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static String MOVIE_BASE_URL =  "https://api.themoviedb.org/3/";

    public static final String TOKEN_URL = "?api_key=b12f4ce69c08c0214bf447763d5cf7ec";

    //can change number later on with savedPreferences
    public static int paginationNumber = 1;

    // Make an API request with the given query and store the results in a String
    public static String retrieveNeededJSON(String requestOption, Integer movieID, String sessionID, String searchString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJSONString = null;
        StringBuilder urlString = new StringBuilder();

        //string used for http request
        String customModifiedURL = null;

        switch (requestOption) {
            case "discover":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("discover/movie");
                urlString.append(TOKEN_URL);
                urlString.append("&page=");
                urlString.append(paginationNumber);
                customModifiedURL = urlString.toString();
                break;
            case "details":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("movie/");
                urlString.append(movieID);
                urlString.append(TOKEN_URL);
                customModifiedURL = urlString.toString();
                break;
            case "trailer":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("movie/");
                urlString.append(movieID);
                urlString.append("/videos");
                urlString.append(TOKEN_URL);
                urlString.append("&language=en-US&page=1");
                urlString.append("&official=true");
                customModifiedURL = urlString.toString();
                break;
            case "review":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("movie/");
                urlString.append(movieID);
                urlString.append("/reviews");
                urlString.append(TOKEN_URL);
                urlString.append("&language=en-US&page=1");
                customModifiedURL = urlString.toString();
                break;
            case "rating":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("account/");
                urlString.append(movieID);
                urlString.append("/rated");
                urlString.append("/movies");
                urlString.append(TOKEN_URL);
                urlString.append("&language=en-US");
                urlString.append("&session_id=");
                urlString.append(sessionID);
                urlString.append("&sort_by=created_at.asc&page=1");
                customModifiedURL = urlString.toString();
                break;
            case "genres":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("genre/list");
                urlString.append(TOKEN_URL);
                urlString.append("&language=en-US");
                customModifiedURL = urlString.toString();
                break;
            case "request_token":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("authentication/token/new");
                urlString.append(TOKEN_URL);
                customModifiedURL = urlString.toString();
                break;
            case "user":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("account");
                urlString.append(TOKEN_URL);
                urlString.append("&session_id=");
                urlString.append(sessionID);
                customModifiedURL = urlString.toString();
                break;
            case "list":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("list/");
                urlString.append(movieID);
                urlString.append(TOKEN_URL);
                urlString.append("&language=en-US");
                customModifiedURL = urlString.toString();
                break;
            case "created_lists":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("account/");
                urlString.append(movieID);
                urlString.append("/lists");
                urlString.append(TOKEN_URL);
                urlString.append("&language=en-US");
                urlString.append("&session_id=");
                urlString.append(sessionID);
                urlString.append("&page=1");
                customModifiedURL = urlString.toString();
                break;
            case "search":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("search/movie");
                urlString.append(TOKEN_URL);
                urlString.append("&language=en-US&query=");
                urlString.append(searchString);
                urlString.append("&page=1&include_adult=false");
                customModifiedURL = urlString.toString();
                break;
        }

        try {
            Uri builtURI = Uri.parse(customModifiedURL).buildUpon().build();

            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            movieJSONString = builder.toString();
        } catch (IOException e) {
            // Create a Log that something went wrong
            Log.e(TAG, "Something went wrong!");
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Create a Log that something went wrong
                    Log.e(TAG, "Something went wrong!");
                    e.printStackTrace();
                }
            }
        }

        urlConnection.disconnect();

        // Create a Log with the JSONString
        Log.d(TAG, "JSON RESPONSE: " + movieJSONString);
        // Return JSON String
        return movieJSONString;
    }

    public static String createList(String requestOption, String sessionID) {
        StringBuilder urlString = new StringBuilder();
        String customModifiedURL = null;

        switch (requestOption) {
            case "list":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("list");
                urlString.append(TOKEN_URL);
                urlString.append("&session_id=");
                urlString.append(sessionID);
                customModifiedURL = urlString.toString();
                break;
        }

        try {
            URL url = new URL(customModifiedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonObject = new JSONObject()
                    .put("name", ListCreateActivity.nameInput)
                    .put("description", ListCreateActivity.descriptionInput)
                    .put("language", LoginActivity.user.getIso_639_1());

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());

            os.flush();
            os.close();

            String response = String.valueOf(conn.getResponseCode());

            conn.disconnect();

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }

    public static String deleteList(String listID, String sessionID) {
        StringBuilder urlString = new StringBuilder();
        String customModifiedURL = null;

        urlString.append(MOVIE_BASE_URL);
        urlString.append("list/");
        urlString.append(listID);
        urlString.append(TOKEN_URL);
        urlString.append("&session_id=");
        urlString.append(sessionID);
        customModifiedURL = urlString.toString();

        try {
            URL url = new URL(customModifiedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String response = String.valueOf(conn.getResponseCode());

            conn.disconnect();

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }

    public static String controlMovieFromList(String requestMethod, String listID, int movieID) {
        StringBuilder urlString = new StringBuilder();
        String customModifiedURL = null;
        BufferedReader reader = null;

        switch (requestMethod) {
            case "delete":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("list/");
                urlString.append(listID);
                urlString.append("/remove_item");
                urlString.append(TOKEN_URL);
                urlString.append("&session_id=");
                urlString.append(LoginActivity.user.getSessionID());
                customModifiedURL = urlString.toString();
                break;
            case "add":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("list/");
                urlString.append(listID);
                urlString.append("/add_item");
                urlString.append(TOKEN_URL);
                urlString.append("&session_id=");
                urlString.append(LoginActivity.user.getSessionID());
                customModifiedURL = urlString.toString();
                break;
        }

        try {
            URL url = new URL(customModifiedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonObject = new JSONObject()
                    .put("media_id", movieID);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());

            InputStream inputStream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            os.flush();
            os.close();

            conn.disconnect();

            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }

    public static String createSession(String token) {
        StringBuilder urlString = new StringBuilder();
        String customModifiedURL = null;

        urlString.append("https://api.themoviedb.org/3/authentication/session/new?api_key=b12f4ce69c08c0214bf447763d5cf7ec");
        customModifiedURL = urlString.toString();

        try {
            URL url = new URL(customModifiedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonObject = new JSONObject()
                    .put("request_token", token);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());

            os.flush();
            os.close();

            int status = conn.getResponseCode();

            switch (status) {
                case 200:
                case 401:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    return sb.toString();
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "request token not authorized for session id";
    }

    public static String loginRequest(String username, String password, String token) {
        StringBuilder urlString = new StringBuilder();
        String customModifiedURL = null;

            urlString.append("https://api.themoviedb.org/3/authentication/token/validate_with_login?api_key=b12f4ce69c08c0214bf447763d5cf7ec");
            customModifiedURL = urlString.toString();

        try {
            URL url = new URL(customModifiedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonObject = new JSONObject()
                    .put("username", username)
                    .put("password", password)
                    .put("request_token", token);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());

            String status = String.valueOf(conn.getResponseCode());

            os.flush();
            os.close();

            conn.disconnect();

            return status;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "login post request failed";
    }

    public static String postRating(String requestOption, int movieID, String sessionID) {
        StringBuilder urlString = new StringBuilder();
        String customModifiedURL = null;

        switch (requestOption) {
            case "rating":
                urlString.append(MOVIE_BASE_URL);
                urlString.append("movie/");
                urlString.append(movieID);
                urlString.append("/rating");
                urlString.append(TOKEN_URL);
                urlString.append("&session_id=");
                urlString.append(sessionID);
                customModifiedURL = urlString.toString();
                break;
        }

        try {
            URL url = new URL(customModifiedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            int value = Integer.parseInt(MovieDetailActivity.ratingInput);

            JSONObject jsonObject = new JSONObject()
                    .put("value", value);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());

            os.flush();
            os.close();

            String response = String.valueOf(conn.getResponseCode());

            conn.disconnect();

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }
}