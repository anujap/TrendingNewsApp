package com.example.anuja.trendingnewsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This is the news model class.
 * This is the class used in the Headlines/News fragment
 */
public class NewsModel {

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private ArrayList<Articles> articlesArrayList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<Articles> getArticlesArrayList() {
        return articlesArrayList;
    }

    public void setArticlesArrayList(ArrayList<Articles> articlesArrayList) {
        this.articlesArrayList = articlesArrayList;
    }
}
