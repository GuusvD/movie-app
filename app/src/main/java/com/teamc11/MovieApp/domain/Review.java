package com.teamc11.MovieApp.domain;

import java.io.Serializable;

public class Review implements Serializable {
    // Attributes
    private String id;
    private String authorName;
    private Author author;
    private String content;
    private String createdAt;
    private String updatedAt;
    private String url;

    // Constructor
    public Review(String id, String authorName, Author author, String content, String createdAt, String updatedAt, String url) {
        this.id = id;
        this.authorName = authorName;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.url = url;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Author getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getAuthorName() + " - " + getAuthor().getRating();
    }
}