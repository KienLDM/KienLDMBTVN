package com.example.kienldmbtvn.ui.photopicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhotoPickerViewModel(private val repository: PhotoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<PhotoUiState>(PhotoUiState.Loading)
    val uiState: StateFlow<PhotoUiState> = _uiState

    fun loadPhotos() {
        viewModelScope.launch {
            _uiState.value = PhotoUiState.Loading
            try {
                val photos = repository.getPhotos()
                _uiState.value = PhotoUiState.Success(photos)
            } catch (e: Exception) {
                _uiState.value = PhotoUiState.Error("Failed to load photos: ${e.message}")
            }
        }
    }
}

sealed class PhotoUiState {
    object Loading : PhotoUiState()
    data class Success(val photos: List<Photo>) : PhotoUiState()
    data class Error(val message: String) : PhotoUiState()
}