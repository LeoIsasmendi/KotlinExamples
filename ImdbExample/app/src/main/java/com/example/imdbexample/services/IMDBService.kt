package com.example.imdbexample.services

import com.example.imdbexample.models.MovieDetailsResponse
import com.example.imdbexample.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMDBService {

    @GET("/3/movie/popular")
    fun getPopularMovies( @Query("page") page: Int): Call<MovieResponse>

    @GET("/3/discover/movie")
    fun getDiscoverMovies(@Query("page") page: Int): Call<MovieResponse>

    @GET("/3/movie/{movie_id}")
    fun getMovieDetails(
        @Path(
            value = "movie_id",
            encoded = true
        ) movieId: Int
    ): Call<MovieDetailsResponse>

    @GET("/3/search/movie")
    fun searchForMovie( @Query("query") query: String): Call<MovieResponse>
}