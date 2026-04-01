package com.example.tmdbapp.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbapp.data.model.MovieBrief
import com.example.tmdbapp.ui.common.MovieListSection
import com.example.tmdbapp.ui.list.MovieListType
import com.example.tmdbapp.ui.theme.TMDBappTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onMovieClick: (movieId: Int, itemType: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchContent(
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        uiState = uiState,
        onMovieClick = onMovieClick,
        onSearchAction = {
            if (searchQuery.isNotBlank()) {
                viewModel.searchMovie(searchQuery.trim())
                keyboardController?.hide()
            }
        },
        onRetry = { viewModel.searchMovie(searchQuery.trim()) }
    )
}

@Composable
fun SearchContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    uiState: SearchUiState,
    onMovieClick: (movieId: Int, itemType: String) -> Unit,
    onSearchAction: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            label = { Text("Search Movies or TV Shows", Modifier.padding(start = 8.dp)) },
            placeholder = { Text("e.g., Inception, Breaking Bad") },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search Icon"
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchAction() }
            )
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            item {
                MovieListSection(
                    title = "Search Results",
                    movies = uiState.searchMovies,
                    isLoading = uiState.isSearchMoviesLoading,
                    errorMessage = uiState.errorMessages,
                    onMovieClick = { movie -> onMovieClick(movie.id, "movie") },
                    onRetry = onRetry
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    TMDBappTheme {
        Surface {
            SearchContent(
                searchQuery = "Interstellar",
                onSearchQueryChange = {},
                uiState = SearchUiState(
                    searchMovies = listOf(
                        MovieBrief(1, "Interstellar", null, "/gEU2QniE6E77NI6lCU6MxlSaba7.jpg", 8.4, "Overview"),
                        MovieBrief(2, "Inception", null, "/edvXYv793S87lynU0WzCbsDHrrL.jpg", 8.3, "Overview")
                    )
                ),
                onMovieClick = { _, _ -> },
                onSearchAction = {},
                onRetry = {}
            )
        }
    }
}
