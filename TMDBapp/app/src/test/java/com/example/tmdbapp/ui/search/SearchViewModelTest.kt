package com.example.tmdbapp.ui.search

import app.cash.turbine.test
import com.example.tmdbapp.data.model.MovieBrief
import com.example.tmdbapp.data.model.MovieListResponse
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class SearchViewModelTest {

    @MockK
    private lateinit var repository: MovieRepository

    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUiState initial state`() = runTest {
        val state = viewModel.uiState.value
        Assertions.assertEquals(state.inputQuery, "")
        Assertions.assertEquals(state.searchMovies, emptyList<MovieBrief>())
        Assertions.assertEquals(state.isSearchMoviesLoading, false)
        Assertions.assertEquals(state.errorMessages, "")
    }

    @Test
    fun `searchMovie successful search`() = runTest {
        val query = "matrix"
        val response = MovieListResponse(
            page = 1,
            results = listOf(
                MovieBrief(
                    id = 1,
                    title = "Matrix",
                    name = "Matrix",
                    posterPath = "poster_path",
                    voteAverage = 7.0,
                    overview = "overview"
                )
            ),
            totalPages = 1,
            totalResults = 0
        )
        coEvery { repository.searchMovie(query = query, page = 1) } returns Result.success(
            response
        )

        viewModel.searchMovie(query = query)

        viewModel.uiState.test {
            val state = awaitItem()
            Assertions.assertEquals(state.searchMovies, response.results)
            Assertions.assertEquals(state.isSearchMoviesLoading, false)
            Assertions.assertNull(state.errorMessages)
        }
    }

    @Test
    fun `searchMovie API failure`() = runTest {
        val query = "A Fictional Movie"
        val expectedErrorMessage = "Network Error: Could not fetch movies"
        val apiException = Exception(expectedErrorMessage)

        coEvery { repository.searchMovie(query = query, page = 1) } returns Result.failure(
            apiException
        )

        viewModel.searchMovie(query)
        viewModel.uiState.test {
            val errorState = awaitItem()
            println("State after repository failure (expected error): $errorState")
            assertEquals(
                false,
                errorState.isSearchMoviesLoading,
                "Loading should be false after error."
            )
            assertNotNull(errorState.errorMessages, "Error message should not be null.")
            assertEquals(
                expectedErrorMessage,
                errorState.errorMessages,
                "Error message should match the exception."
            )
            assertTrue(errorState.searchMovies.isEmpty(), "Search movies should be empty on error.")
        }
    }

    @Test
    fun `searchMovie API failure with null error message`() = runTest {
        val query = "A Fictional Movie"
        val apiException = Exception()

        coEvery { repository.searchMovie(query = query, page = 1) } returns Result.failure(
            apiException
        )

        viewModel.searchMovie(query)
        viewModel.uiState.test {
            val errorState = awaitItem()
            println("State after repository failure (expected error): $errorState")
            assertEquals(
                false,
                errorState.isSearchMoviesLoading,
                "Loading should be false after error."
            )
            assertNotNull(errorState.errorMessages, "Error message should not be null.")
            assertTrue(errorState.searchMovies.isEmpty(), "Search movies should be empty on error.")
        }
    }

    @Test
    fun `searchMovie empty query`() = runTest {
        val query = ""
        val response = MovieListResponse(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        )
        coEvery { repository.searchMovie(query = query, page = 1) } returns Result.success(
            response
        )

        viewModel.searchMovie(query = query)

        viewModel.uiState.test {
            val state = awaitItem()
            Assertions.assertEquals(state.searchMovies, response.results)
            Assertions.assertEquals(state.isSearchMoviesLoading, false)
            Assertions.assertNull(state.errorMessages)
        }
    }
}
