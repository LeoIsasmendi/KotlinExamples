package com.example.tmdbapp.ui.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tmdbapp.ui.common.MovieListSection
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = koinViewModel(),
    onMovieClick: (movieId: Int, itemType: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            MovieListSection(
                title = MovieListType.PopularMovies.title,
                movies = uiState.popularMovies,
                isLoading = uiState.isLoadingPopularMovies,
                errorMessage = uiState.errorMessages[MovieListType.PopularMovies],
                onMovieClick = { movie -> onMovieClick(movie.id, "movie") },
                onRetry = { viewModel.fetchMovieList(MovieListType.PopularMovies) }
            )
        }
        item {
            MovieListSection(
                title = MovieListType.PopularTvSeries.title,
                movies = uiState.popularTvSeries,
                isLoading = uiState.isLoadingPopularTvSeries,
                errorMessage = uiState.errorMessages[MovieListType.PopularTvSeries],
                onMovieClick = { series -> onMovieClick(series.id, "tv") },
                onRetry = { viewModel.fetchMovieList(MovieListType.PopularTvSeries) }
            )
        }
    }
}
