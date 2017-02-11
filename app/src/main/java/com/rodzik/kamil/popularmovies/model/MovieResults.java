package com.rodzik.kamil.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResults {
    public List<Movie> results;
    public int page;
    @SerializedName("total_pages")
    public int totalPages;
}
