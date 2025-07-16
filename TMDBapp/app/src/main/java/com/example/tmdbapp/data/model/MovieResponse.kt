package com.example.tmdbapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    val page: Int,
    val results: List<MovieBrief>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class MovieBrief(
    val id: Int,
    val title: String?,
    val name: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("overview")
    val overview: String?
)

data class MovieDetail(
    val id: Int,
    val title: String?,
    val name: String?,
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    val genres: List<Genre>?,
    val runtime: Int?
)

data class Genre(
    val id: Int,
    val name: String
)