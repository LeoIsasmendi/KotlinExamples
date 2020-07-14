package com.example.imdbexample.models

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.math.abs

class MovieTest {
    private val posterPath = "poster_path"
    private val overview = "overview"
    private val releaseDate = "release_date"
    private val title = "title"
    private val adult = false
    private val id = 0
    private val originalTitle = "original_title"
    private val originalLanguage = "original_language"
    private val backdropPath = "backdrop_path"
    private val popularity = 1.0
    private val voteCount = 0
    private val video = false
    private val voteAverage = 0.0
    private val favorite = false
    private val genreIds = emptyList<Int>()

    private lateinit var model: Movie

    @Before
    fun setUp() {
        model = Movie(
            posterPath,
            adult,
            overview,
            releaseDate,
            genreIds,
            id,
            originalTitle,
            originalLanguage,
            title,
            backdropPath,
            popularity,
            voteCount,
            video,
            voteAverage,
            favorite
        )
    }

    @Test
    fun getPoster_path() {
        Assert.assertEquals(posterPath, model.poster_path)
    }

    @Test
    fun getAdult() {
        Assert.assertFalse(model.adult)
    }

    @Test
    fun getOverview() {
        Assert.assertEquals(overview, model.overview)
    }

    @Test
    fun getRelease_date() {
        Assert.assertEquals(releaseDate, model.release_date)
    }

    @Test
    fun getGenre_ids() {
        Assert.assertEquals(genreIds, model.genre_ids)
    }

    @Test
    fun getId() {
        Assert.assertEquals(id, model.id)
    }

    @Test
    fun getOriginal_title() {
        Assert.assertEquals(originalTitle, model.original_title)
    }

    @Test
    fun getOriginal_language() {
        Assert.assertEquals(originalLanguage, model.original_language)
    }

    @Test
    fun getTitle() {
        Assert.assertEquals(title, model.title)
    }

    @Test
    fun getBackdrop_path() {
        Assert.assertEquals(backdropPath, model.backdrop_path)
    }

    @Test
    fun getPopularity() {
        Assert.assertTrue(abs(popularity - model.popularity) == 0.0)
    }

    @Test
    fun getVote_count() {
        Assert.assertEquals(voteCount, model.vote_count)
    }

    @Test
    fun getVideo() {
        Assert.assertEquals(video, model.video)
    }

    @Test
    fun getVote_average() {
        Assert.assertTrue(abs(voteAverage - model.vote_average) == 0.0)
    }

    @Test
    fun getFavorite() {
        Assert.assertEquals(favorite, model.favorite)
    }
}