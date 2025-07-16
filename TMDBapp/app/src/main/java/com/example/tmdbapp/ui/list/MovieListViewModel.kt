package com.example.tmdbapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.data.model.MovieBrief
import com.example.tmdbapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class MovieListType(val title: String) {
    data object PopularMovies : MovieListType("Popular Movies")
    data object PopularTvSeries : MovieListType("Popular TV Series")
}

data class MovieListUiState(
    val popularMovies: List<MovieBrief> = emptyList(),
    val popularTvSeries: List<MovieBrief> = emptyList(),
    val isLoadingPopularMovies: Boolean = false,
    val isLoadingPopularTvSeries: Boolean = false,
    val errorMessages: Map<MovieListType, String?> = emptyMap()
)

class MovieListViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    init {
        loadAllLists()
    }

    fun loadAllLists() {
        fetchMovieList(MovieListType.PopularMovies)
        fetchMovieList(MovieListType.PopularTvSeries)
    }

    fun fetchMovieList(listType: MovieListType, page: Int = 1) {
        viewModelScope.launch {
            setLoadingState(listType, true)
            setErrorState(listType, null)

            val result = when (listType) {
                MovieListType.PopularMovies -> repository.getPopularMovies(page)
                MovieListType.PopularTvSeries -> repository.getPopularTvSeries(page)
            }

            result.fold(
                onSuccess = { response ->
                    _uiState.update { currentState ->
                        when (listType) {
                            MovieListType.PopularMovies -> currentState.copy(popularMovies = response.results)
                            MovieListType.PopularTvSeries -> currentState.copy(popularTvSeries = response.results)
                        }
                    }
                },
                onFailure = { error ->
                    setErrorState(listType, error.message ?: "An unknown error occurred")
                }
            )
            // Reset loading state for the specific list type
            setLoadingState(listType, false)
        }
    }

    private fun setLoadingState(listType: MovieListType, isLoading: Boolean) {
        _uiState.update { currentState ->
            when (listType) {
                MovieListType.PopularMovies -> currentState.copy(isLoadingPopularMovies = isLoading)
                MovieListType.PopularTvSeries -> currentState.copy(isLoadingPopularTvSeries = isLoading)
            }
        }
    }

    private fun setErrorState(listType: MovieListType, errorMessage: String?) {
        _uiState.update { currentState ->
            val newErrorMessages = currentState.errorMessages.toMutableMap()
            newErrorMessages[listType] = errorMessage
            currentState.copy(errorMessages = newErrorMessages)
        }
    }
}