package com.example.kienldmbtvn.ui.style

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kienldmbtvn.data.response.CategoryItem
import com.example.kienldmbtvn.data.response.StyleItem
import com.example.kienldmbtvn.data.style.StyleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StyleViewModel(
    private val styleRepository: StyleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StyleUiState())
    val uiState: StateFlow<StyleUiState> = _uiState

    init {
        fetchStyles()
        fetchCategories()
    }

    fun fetchStyles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isStyleLoading = true) }
            styleRepository.getStyles()
                .onSuccess { styles ->
                    _uiState.update {
                        it.copy(
                            styles = styles,
                            isStyleLoading = false,
                            styleError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isStyleLoading = false,
                            styleError = "Network error: ${error.message ?: "Unknown error"}"
                        )
                    }
                    Log.d("StyleViewModel", "Network error: ${error.message}")
                }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCategoryLoading = true) }
            styleRepository.getItems()
                .onSuccess { categories ->
                    _uiState.update {
                        it.copy(
                            categories = categories,
                            isCategoryLoading = false,
                            categoryError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isCategoryLoading = false,
                            categoryError = "Network error: ${error.message ?: "Unknown error"}"
                        )
                    }
                    Log.d("StyleViewModel", "Network error: ${error.message}")
                }
        }
    }

    fun selectStyle(style: StyleItem) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun selectCategory(category: CategoryItem) {
        _uiState.update { it.copy(selectedCategory = category) }
    }
}