package com.example.imdbexample.models

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.math.abs

class MovieDetailsResponseTest {

    private val adult = true
    private val backdropPath = "backdrop_path"
    private val belongsToCollection = "belongs_to_collection"
    private val budget = 0
    private val homepage = "homepage"
    private val id = 0
    private val imdbId = "imdb_id"
    private val originalLanguage = "original_language"
    private val originalTitle = "original_title"
    private val overview = "overview"
    private val popularity = 0.0
    private val posterPath = "poster_path"
    private val releaseDate = "release_date"
    private val revenue = 0
    private val runtime = 0
    private val status = "status"
    private val tagline = "tagline"
    private val title = "title"
    private val video = false
    private val voteAverage = 0.0


    private lateinit var model: MovieDetailsResponse

    @Before
    fun setUp() {
        model = MovieDetailsResponse(
            adult,
            backdropPath,
            belongsToCollection,
            budget,
            homepage,
            id,
            imdbId,
            originalLanguage,
            originalTitle,
            overview,
            popularity,
            posterPath,
            releaseDate,
            revenue,
            runtime,
            status,
            tagline,
            title,
            video,
            voteAverage
        )
    }

    @Test
    fun getAdult() {
        Assert.assertEquals(adult, model.adult)
    }

    @Test
    fun getBackdrop_path() {
        Assert.assertEquals(backdropPath, model.backdrop_path)
    }

    @Test
    fun getBelongs_to_collection() {
        Assert.assertEquals(belongsToCollection, model.belongs_to_collection)
    }

    @Test
    fun getBudget() {
        Assert.assertEquals(budget, model.budget)
    }

    @Test
    fun getHomepage() {
        Assert.assertEquals(homepage, model.homepage)
    }

    @Test
    fun getId() {
        Assert.assertEquals(id, model.id)
    }

    @Test
    fun getImdb_id() {
        Assert.assertEquals(imdbId, model.imdb_id)
    }

    @Test
    fun getOriginal_language() {
        Assert.assertEquals(originalLanguage, model.original_language)
    }

    @Test
    fun getOriginal_title() {
        Assert.assertEquals(title, model.title)
    }

    @Test
    fun getOverview() {
        Assert.assertEquals(overview, model.overview)
    }

    @Test
    fun getPopularity() {
        Assert.assertTrue(abs(popularity - model.popularity) == 0.0)
    }

    @Test
    fun getPoster_path() {
        Assert.assertEquals(posterPath, model.poster_path)
    }

    @Test
    fun getRelease_date() {
        Assert.assertEquals(releaseDate, model.release_date)
    }

    @Test
    fun getRevenue() {
        Assert.assertEquals(revenue, model.revenue)
    }

    @Test
    fun getRuntime() {
        Assert.assertEquals(runtime, model.runtime)
    }

    @Test
    fun getStatus() {
        Assert.assertEquals(status, model.status)
    }

    @Test
    fun getTagline() {
        Assert.assertEquals(tagline, model.tagline)
    }

    @Test
    fun getTitle() {
        Assert.assertEquals(title, model.title)
    }

    @Test
    fun getVideo() {
        Assert.assertFalse(model.video)
    }

    @Test
    fun getVote_average() {
        Assert.assertTrue(abs(voteAverage - model.vote_average) == 0.0)
    }
}