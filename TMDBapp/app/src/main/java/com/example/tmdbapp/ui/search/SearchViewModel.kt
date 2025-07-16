package com.example.tmdbapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.data.model.MovieBrief
import com.example.tmdbapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class MovieListUiState(
    val searchMovies: List<MovieBrief> = emptyList(),
    val isSearchMoviesLoading: Boolean = false,
    val errorMessages: String? = "",
    val inputQuery: String = ""
)

open class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()


    init {
        loadInput()
    }

    private fun loadInput() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(inputQuery = "")
            }
        }
    }

    fun searchMovie(query: String, page: Int = 1) {
        viewModelScope.launch {
            setLoadingState(true)
            setErrorState(null)

            val result = repository.searchMovie(query, page)
            result.fold(
                onSuccess = { response ->
                    _uiState.update { currentState ->
                        currentState.copy(searchMovies = response.results)
                    }
                },
                onFailure = { error ->
                    setErrorState(error.message ?: "An unknown error occurred")
                }
            )
            setLoadingState(false)
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isSearchMoviesLoading = isLoading)
        }
    }

    private fun setErrorState(errorMessage: String?) {
        _uiState.update { currentState ->
            currentState.copy(errorMessages = errorMessage)
        }
    }
}