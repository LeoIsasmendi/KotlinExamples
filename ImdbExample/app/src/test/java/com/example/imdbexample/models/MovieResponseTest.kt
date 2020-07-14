package com.example.imdbexample.models

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MovieResponseTest {

    private var page = 1
    private var results = emptyList<Movie>()
    private var totalResults = 1
    private var totalPages = 1

    private lateinit var model: MovieResponse

    @Before
    fun setUp() {
        model = MovieResponse(page, results, totalResults, totalPages)
    }

    @Test
    fun getPage() {
        Assert.assertEquals(page, model.page)
    }

    @Test
    fun getResults() {
        Assert.assertEquals(results, model.results)
    }

    @Test
    fun getTotal_results() {
        Assert.assertEquals(totalResults, model.total_results)
    }

    @Test
    fun getTotal_pages() {
        Assert.assertEquals(totalPages, model.total_pages)
    }
}