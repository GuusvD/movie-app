package com.teamc11.MovieApp.applicationlogic.loaders;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.teamc11.MovieApp.applicationlogic.loaders.movie.MovieLoader;
import com.teamc11.MovieApp.datastorage.NetworkUtils;
import com.teamc11.MovieApp.domain.User;

import org.json.JSONObject;

public class LoginLoader extends AsyncTaskLoader<User> {
    private final String TAG = MovieLoader.class.getSimpleName();
    private String username;
    private String password;

    // Constructor to create a LoginLoader
    public LoginLoader(Context context, String username, String password) {
        super(context);
        this.username = username;
        this.password = password;

        // Create a Log that the method is finished
        Log.i(TAG, "Loginloader constructor finished!");
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

    @Nullable
    @Override
    public User loadInBackground() {
        //get a token
        String token = parseRequestToken(NetworkUtils.retrieveNeededJSON("request_token", null, null, null));
        //authorize token through login
        String loginResponse = NetworkUtils.loginRequest(this.username, this.password,token);

        //if the login was successful
        if (loginResponse.equals("200")) {
           String jsonSessionIDResponse = NetworkUtils.createSession(token);
           //and the token was successful
           if(!"invalid".equals(parseSessionID(jsonSessionIDResponse))) {
               //store the session id
               String sessionID = parseSessionID(jsonSessionIDResponse);

               String jsonUserString = NetworkUtils.retrieveNeededJSON("user", null, sessionID, null);

               return parseUser(jsonUserString, sessionID);
           }
        }
        return null;
    }

    public String parseRequestToken(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            String token = jsonObject.getString("request_token");

            return token;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String parseSessionID(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            boolean success = jsonObject.getBoolean("success");

            if (success) {
                return jsonObject.getString("session_id");
            }

            return "invalid";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public User parseUser(String dataE, String sessionID) {
        try {
            JSONObject jsonObject = new JSONObject(dataE);

            JSONObject avatar = jsonObject.getJSONObject("avatar");

            JSONObject gravatar = avatar.getJSONObject("gravatar");
            String hash = gravatar.getString("hash");

            JSONObject tmdb = avatar.getJSONObject("tmdb");
            String avatarPath = tmdb.getString("avatar_path");

            int id = jsonObject.getInt("id");
            String iso_639_1 = jsonObject.getString("iso_639_1");
            String iso_3166_1 = jsonObject.getString("iso_3166_1");
            String name = jsonObject.getString("name");
            boolean includeAdult = jsonObject.getBoolean("include_adult");

            return new User(hash, avatarPath, id, iso_639_1, iso_3166_1, name, includeAdult, username, sessionID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}