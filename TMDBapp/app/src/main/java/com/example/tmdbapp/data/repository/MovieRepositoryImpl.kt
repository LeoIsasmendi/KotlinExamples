package com.example.tmdbapp.data.repository

import com.example.tmdbapp.data.model.MovieDetail
import com.example.tmdbapp.data.model.MovieListResponse
import com.example.tmdbapp.data.remote.TmdbApiService
import com.example.tmdbapp.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(private val apiService: TmdbApiService) : MovieRepository {

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(apiCall())
            } catch (e: Exception) {
                // Log e
                Result.failure(e) // Propagate the exception
            }
        }
    }

    override suspend fun getPopularMovies(page: Int): Result<MovieListResponse> = safeApiCall {
        apiService.getPopularMovies(page)
    }

    override suspend fun getPopularTvSeries(page: Int): Result<MovieListResponse> = safeApiCall {
        apiService.getPopularTvSeries(page)
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetail> = safeApiCall {
        apiService.getMovieDetails(movieId)
    }

    override suspend fun getTvSeriesDetails(tvId: Int): Result<MovieDetail> = safeApiCall {
        apiService.getTvSeriesDetails(tvId)
    }

    override suspend fun searchMovie(query: String, page: Int): Result<MovieListResponse> =
        safeApiCall {
            apiService.searchMovie(query, page)
        }
}