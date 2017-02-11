package com.rodzik.kamil.popularmovies.utils;


import com.rodzik.kamil.popularmovies.network.RetrofitClient;
import com.rodzik.kamil.popularmovies.network.TheMovieDbService;

public class ApiUtils {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static TheMovieDbService getTheMovieDbService() {
        return RetrofitClient.getClient(BASE_URL).create(TheMovieDbService.class);
    }
}