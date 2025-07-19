package com.example.tmdbapp.ui.detail

import app.cash.turbine.test
import com.example.tmdbapp.data.model.Genre
import com.example.tmdbapp.data.model.MovieDetail
import com.example.tmdbapp.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class DetailViewModelTest {

    @MockK
    private lateinit var repository: MovieRepository
    private val mockMovieDetail = MovieDetail(
        id = 550,
        title = "Fight Club",
        overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground fight clubs forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
        posterPath = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
        releaseDate = "1999-10-15",
        voteAverage = 8.433,
        genres = listOf(Genre(id = 1, name = "Drama")),
        name = "",
        backdropPath = "",
        firstAirDate = "",
        runtime = 0
    )

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given invalid item id, when viewmodel is initialized, then error message is set`() =
        runTest {
            // Given
            val itemId = 0
            val itemType = "movie"
            // When
            val viewModel = DetailViewModel(itemId, itemType, repository)
            // Then
            val state = viewModel.uiState.value
            Assertions.assertEquals("Invalid Item ID.", state.errorMessage)
            Assertions.assertNull(state.movieDetail)
        }

    @Test
    fun `given movie type, when initialized, then movie details are loaded successfully`() =
        runTest {
            // Given
            val itemId = 550
            val itemType = "movie"
            coEvery { repository.getMovieDetails(itemId) } returns Result.success(
                mockMovieDetail
            )

            // When
            val viewModel = DetailViewModel(itemId, itemType, repository)

            // Then
            // Success state
            viewModel.uiState.test {
                val state = awaitItem()
                Assertions.assertEquals(mockMovieDetail, state.movieDetail)
                Assertions.assertEquals(false, state.isLoading)
                Assertions.assertNull(state.errorMessage)
            }
        }

    @Test
    fun `given tv type, when initialized, then tv series details are loaded successfully`() =
        runTest {
            // Given
            val itemId = 1399
            val itemType = "tv"
            coEvery { repository.getTvSeriesDetails(itemId) } returns Result.success(
                mockMovieDetail
            ) // Assuming same model for simplicity

            // When
            val viewModel = DetailViewModel(itemId, itemType, repository)

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                Assertions.assertEquals(mockMovieDetail, state.movieDetail)
                Assertions.assertEquals(false, state.isLoading)
                Assertions.assertNull(state.errorMessage)
            }
        }

    @Test
    fun `given movie type, when repository fails, then error message is set`() = runTest {
        // Given
        val itemId = 550
        val itemType = "movie"
        val errorMessage = "Network Error"
        coEvery { repository.getMovieDetails(itemId) } returns Result.failure(
            Exception(
                errorMessage
            )
        )

        // When
        val viewModel = DetailViewModel(itemId, itemType, repository)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            Assertions.assertEquals(errorMessage, state.errorMessage)
            Assertions.assertEquals(false, state.isLoading)
            Assertions.assertNull(state.movieDetail)
        }

    }

    @Test
    fun `given tv type, when repository fails, then error message is set`() = runTest {
        // Given
        val itemId = 1399
        val itemType = "tv"
        val errorMessage = "Network Error"
        coEvery { repository.getTvSeriesDetails(itemId) } returns Result.failure(
            Exception(
                errorMessage
            )
        )

        // When
        val viewModel = DetailViewModel(itemId, itemType, repository)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            Assertions.assertEquals(errorMessage, state.errorMessage)
            Assertions.assertEquals(false, state.isLoading)
            Assertions.assertNull(state.movieDetail)
        }
    }

    @Test
    fun `given invalid item type, when initialized, then error message is set`() = runTest {
        // Given
        val itemId = 1
        val itemType = "invalid_type"

        // When
        val viewModel = DetailViewModel(itemId, itemType, repository)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            Assertions.assertEquals("Invalid item type: $itemType", state.errorMessage)
            Assertions.assertNull(state.movieDetail)
            Assertions.assertEquals(false, state.isLoading)
        }
    }
}
