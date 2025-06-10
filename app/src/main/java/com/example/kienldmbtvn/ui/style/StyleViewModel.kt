package com.example.kienldmbtvn.ui.style

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kienldmbtvn.data.response.CategoryItem
import com.example.kienldmbtvn.data.response.StyleItem
import com.example.kienldmbtvn.data.style.StyleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StyleViewModel(
    private val styleRepository: StyleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<StyleUiState>(StyleUiState.Loading)
    val uiState: StateFlow<StyleUiState> = _uiState

    private val _categoryUiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState

    init {
        fetchStyles()
        fetchCategories()
    }

    fun fetchStyles() {
        viewModelScope.launch {
            _uiState.value = StyleUiState.Loading
            styleRepository.getStyles()
                .onSuccess { styles ->
                    _uiState.value = StyleUiState.Success(styles = styles)
                }
                .onFailure { error ->
                    _uiState.value = StyleUiState.Error("Network error: ${error.message ?: "Unknown error"}")
                    Log.d("StyleViewModel", "Network error: ${error.message}")
                }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _categoryUiState.value = CategoryUiState.Loading
            styleRepository.getItems()
                .onSuccess { categories ->
                    _categoryUiState.value = CategoryUiState.Success(categories = categories)
                }
                .onFailure { error ->
                    _categoryUiState.value =
                        CategoryUiState.Error("Network error: ${error.message ?: "Unknown error"}")
                    Log.d("StyleViewModel", "Network error: ${error.message}")
                }
        }
    }

    fun selectStyle(style: StyleItem) {
        val currentState = _uiState.value
        if (currentState is StyleUiState.Success) {
            _uiState.value = currentState.copy(selectedStyle = style)
        }
    }

    fun selectCategory(category: CategoryItem) {
        val currentState = _categoryUiState.value
        if (currentState is CategoryUiState.Success) {
            _categoryUiState.value = currentState.copy(selectedCategory = category)
        }
    }
}

sealed class StyleUiState {
    object Loading : StyleUiState()
    data class Success(val styles: List<StyleItem>, val selectedStyle: StyleItem? = null) : StyleUiState()
    data class Error(val message: String) : StyleUiState()
}

sealed class CategoryUiState {
    object Loading : CategoryUiState()
    data class Success(
        val categories: List<CategoryItem>,
        val selectedCategory: CategoryItem? = null
    ) : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}