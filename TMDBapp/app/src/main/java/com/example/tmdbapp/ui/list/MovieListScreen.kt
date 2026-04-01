package com.example.tmdbapp.ui.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbapp.data.model.MovieBrief
import com.example.tmdbapp.ui.common.MovieListSection
import com.example.tmdbapp.ui.theme.TMDBappTheme
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = koinViewModel(),
    onMovieClick: (movieId: Int, itemType: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    MovieListContent(
        uiState = uiState,
        onMovieClick = onMovieClick,
        onRetry = { type -> viewModel.fetchMovieList(type) }
    )
}

@Composable
fun MovieListContent(
    uiState: MovieListUiState,
    onMovieClick: (movieId: Int, itemType: String) -> Unit,
    onRetry: (MovieListType) -> Unit
) {
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
                onRetry = { onRetry(MovieListType.PopularMovies) }
            )
        }
        item {
            MovieListSection(
                title = MovieListType.PopularTvSeries.title,
                movies = uiState.popularTvSeries,
                isLoading = uiState.isLoadingPopularTvSeries,
                errorMessage = uiState.errorMessages[MovieListType.PopularTvSeries],
                onMovieClick = { series -> onMovieClick(series.id, "tv") },
                onRetry = { onRetry(MovieListType.PopularTvSeries) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListScreenPreview() {
    TMDBappTheme {
        Surface {
            MovieListContent(
                uiState = MovieListUiState(
                    popularMovies = listOf(
                        MovieBrief(1, "Interstellar", null, "/gEU2QniE6E77NI6lCU6MxlSaba7.jpg", 8.4, "Overview"),
                        MovieBrief(2, "Inception", null, "/edvXYv793S87lynU0WzCbsDHrrL.jpg", 8.3, "Overview")
                    ),
                    popularTvSeries = listOf(
                        MovieBrief(3, null, "Breaking Bad", "/ggm8bb1S969493YYTSBQpC69p02.jpg", 9.5, "Overview")
                    )
                ),
                onMovieClick = { _, _ -> },
                onRetry = {}
            )
        }
    }
}
