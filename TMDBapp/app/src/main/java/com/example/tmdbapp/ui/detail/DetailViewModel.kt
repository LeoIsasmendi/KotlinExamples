package com.example.tmdbapp.ui.detail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.data.model.MovieDetail
import com.example.tmdbapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val movieDetail: MovieDetail? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class DetailViewModel(
    private val itemId: Int, // Injected by Koin from Nav arguments
    private val itemType: String, // "movie" or "tv", injected by Koin
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadItemDetails()
    }

    fun loadItemDetails() {
        if (itemId == 0) {
            _uiState.update { it.copy(errorMessage = "Invalid Item ID.", isLoading = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = if (itemType.equals("movie", ignoreCase = true)) {
                repository.getMovieDetails(itemId)
            } else if (itemType.equals("tv", ignoreCase = true)) {
                repository.getTvSeriesDetails(itemId)
            } else {
                Result.failure(IllegalArgumentException("Invalid item type: $itemType"))
            }

            result.fold(
                onSuccess = { detail ->
                    _uiState.update {
                        it.copy(movieDetail = detail, isLoading = false)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            errorMessage = error.message ?: "Failed to load details.",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }
}