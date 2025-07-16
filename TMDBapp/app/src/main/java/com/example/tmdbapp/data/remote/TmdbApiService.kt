package com.example.tmdbapp.data.remote

import com.example.tmdbapp.data.model.MovieDetail
import com.example.tmdbapp.data.model.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
        const val POSTER_SIZE_W500 = "w500"
        const val BACKDROP_SIZE_W780 = "w780" // Or other sizes like w1280
    }

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): MovieListResponse

    @GET("tv/popular")
    suspend fun getPopularTvSeries(@Query("page") page: Int = 1): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieDetail

    @GET("tv/{tv_id}")
    suspend fun getTvSeriesDetails(@Path("tv_id") tvId: Int): MovieDetail

    @GET("search/multi")
    suspend fun searchMovie(
        @Query("query") query: String = "",
        @Query("page") page: Int = 1
    ): MovieListResponse
}