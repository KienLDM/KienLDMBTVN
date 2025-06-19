package com.example.kienldmbtvn.ui.photopicker

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.kienldmbtvn.base.BaseViewModel
import kotlinx.coroutines.launch

class PhotoPickerViewModel(private val repository: PhotoRepository) : BaseViewModel<PhotoUiState>(PhotoUiState.Loading) {

    fun loadPhotos() {
        viewModelScope.launch {
            setState(PhotoUiState.Loading)
            try {
                val photos = repository.getPhotos()
                setState(PhotoUiState.Success(photos))
            } catch (e: Exception) {
                setState(PhotoUiState.Error("Failed to load photos: ${e.message}"))
            }
        }
    }

    fun togglePhotoSelection(photo: Photo) {
        val current = currentState
        if (current is PhotoUiState.Success) {
            val newSelected = if (current.selectedPhoto == photo) {
                null
            } else {
                photo
            }
            setState(current.copy(selectedPhoto = newSelected))
        }
    }

    fun getSelectedPhotoUri(): Uri? {
        return when (val state = currentState) {
            is PhotoUiState.Success -> state.selectedPhoto?.uri
            else -> null
        }
    }
}

sealed class PhotoUiState {
    object Loading : PhotoUiState()
    data class Success(val photos: List<Photo>, val selectedPhoto: Photo? = null) : PhotoUiState()
    data class Error(val message: String) : PhotoUiState()
}