package com.teamc11.MovieApp.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_table")
public class User implements Serializable {

    @PrimaryKey
    private int id;

    private String hash;
    private String avatarPath;
    private String iso_639_1;
    private String iso_3166_1;
    private String name;
    private boolean includeAdult;
    private String username;
    private String sessionID;
    private String password;

    public User(String hash, String avatarPath, int id, String iso_639_1, String iso_3166_1,
                String name, boolean includeAdult, String username, String sessionID) {
        this.hash = hash;
        this.avatarPath = avatarPath;
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.name = name;
        this.includeAdult = includeAdult;
        this.username = username;
        this.sessionID = sessionID;
    }

    public String getHash() {
        return hash;
    }

    public String getAvatarPath() {
        return "https://www.themoviedb.org/t/p/w150_and_h150_face" + avatarPath;
        //https://www.themoviedb.org/t/p/w150_and_h150_face
    }

    public int getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getName() {
        return name;
    }

    public boolean isIncludeAdult() {
        return includeAdult;
    }

    public String getUsername() {
        return username;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return  "User ID: " + getId() + "\n" +
                "Username: " + getUsername() + "\n" +
                "Password: " + getPassword() + "\n" +
                "Name: " + getName() + "\n" +
                "Avatar Path: " + getAvatarPath() + "\n" +
                "iso_3166_1: " + getIso_3166_1() + "\n" +
                "iso_639_1: " + getIso_639_1() + "\n" +
                "Include Adult: " + isIncludeAdult() + "\n" +
                "Hash: " + getHash() + "\n" +
                "Session ID: " + getSessionID() ;

    }
}