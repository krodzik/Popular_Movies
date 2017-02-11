package com.rodzik.kamil.popularmovies.network;


import com.rodzik.kamil.popularmovies.model.MovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheMovieDbService {
    @GET("movie/popular")
    Call<MovieResults> getMoviesSortByPopularity(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieResults> getMoviesSortByRates(@Query("api_key") String apiKey, @Query("page") int page);
}
