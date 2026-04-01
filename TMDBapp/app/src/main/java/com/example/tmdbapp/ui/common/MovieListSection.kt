package com.example.tmdbapp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbapp.data.model.MovieBrief
import com.example.tmdbapp.ui.theme.TMDBappTheme

@Composable
fun MovieListSection(
    title: String,
    movies: List<MovieBrief>,
    isLoading: Boolean,
    errorMessage: String?,
    onMovieClick: (MovieBrief) -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        } else if (movies.isEmpty()) {
            Text(
                text = "No items to display.",
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movies, key = { it.id }) { movie ->
                    MovieListItem(movie = movie, onMovieClick = onMovieClick)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListSectionPreview() {
    TMDBappTheme {
        Surface {
            MovieListSection(
                title = "Trending Movies",
                movies = listOf(
                    MovieBrief(1, "Interstellar", null, "/gEU2QniE6E77NI6lCU6MxlSaba7.jpg", 8.4, "Overview"),
                    MovieBrief(2, "Inception", null, "/edvXYv793S87lynU0WzCbsDHrrL.jpg", 8.3, "Overview")
                ),
                isLoading = false,
                errorMessage = null,
                onMovieClick = {},
                onRetry = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListSectionLoadingPreview() {
    TMDBappTheme {
        Surface {
            MovieListSection(
                title = "Trending Movies",
                movies = emptyList(),
                isLoading = true,
                errorMessage = null,
                onMovieClick = {},
                onRetry = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListSectionErrorPreview() {
    TMDBappTheme {
        Surface {
            MovieListSection(
                title = "Trending Movies",
                movies = emptyList(),
                isLoading = false,
                errorMessage = "Failed to load movies",
                onMovieClick = {},
                onRetry = {}
            )
        }
    }
}
