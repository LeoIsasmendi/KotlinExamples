package com.example.imdbexample.models

import com.google.gson.annotations.SerializedName

data class MovieResponse (

    @SerializedName("page") val page : Int,
    @SerializedName("results") val results : List<Movie>,
    @SerializedName("total_results") val total_results : Int,
    @SerializedName("total_pages") val total_pages : Int
)