package com.example.tmdbapp.domain.repository

import com.example.tmdbapp.data.model.MovieDetail
import com.example.tmdbapp.data.model.MovieListResponse
interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<MovieListResponse>
    suspend fun getPopularTvSeries(page: Int): Result<MovieListResponse>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetail>
    suspend fun getTvSeriesDetails(tvId: Int): Result<MovieDetail>
    suspend fun searchMovie(query: String, page: Int): Result<MovieListResponse>
}