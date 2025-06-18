package com.example.kienldmbtvn.ui.style

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kienldmbtvn.base.BaseUIState
import com.example.kienldmbtvn.data.network.response.CategoryItem
import com.example.kienldmbtvn.data.network.response.StyleItem
import com.example.kienldmbtvn.data.style.StyleRepository
import com.example.kienldmbtvn.data.AiArtRepository
import com.example.kienldmbtvn.data.exception.AiArtException
import com.example.kienldmbtvn.data.exception.ErrorReason
import com.example.kienldmbtvn.data.network.consts.ServiceConstants
import com.example.kienldmbtvn.data.params.AiArtParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StyleViewModel(
    private val styleRepository: StyleRepository,
    private val aiArtRepository: AiArtRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StyleUiState())
    val uiState: StateFlow<StyleUiState> = _uiState

    private var allStyles: List<StyleItem> = emptyList()

    init {
        fetchStyles()
        fetchCategories()
    }

    private fun updateState(update: (StyleUiState) -> StyleUiState) {
        _uiState.update(update)
    }

    fun updatePrompt(newPrompt: String) {
        updateState { it.copy(prompt = newPrompt) }
    }

    fun updateImageUrl(imageUri: Uri?) {
        updateState { it.copy(imageUrl = imageUri) }
    }

    fun fetchStyles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isStyleLoading = true) }
            styleRepository.getStyles()
                .onSuccess { styles ->
                    allStyles = styles
                    _uiState.update {
                        it.copy(
                            styles = filterStylesByCategory(it.selectedCategory, styles),
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

    fun filterStylesByCategory(category: CategoryItem?, styles: List<StyleItem>): List<StyleItem> {
        return if (category == null) {
            styles
        } else {
            styles.filter { style ->
                style.categories.contains(category.id)
            }
        }
    }

    fun selectStyle(style: StyleItem) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun selectCategory(category: CategoryItem) {
        _uiState.update { currentState ->
            val filteredStyles = filterStylesByCategory(category, allStyles)
            currentState.copy(
                selectedCategory = category,
                styles = filteredStyles,
                selectedStyle = if (filteredStyles.contains(currentState.selectedStyle)) {
                    currentState.selectedStyle
                } else {
                    null
                }
            )
        }
    }

    fun generateImage(context: Context, onSuccess: (resultUrl: String) -> Unit) {
        updateState {
            it.copy(generatingState = BaseUIState.Loading)
        }
        viewModelScope.launch {
            val uiStateValue = uiState.value
            if (uiStateValue.imageUrl == null) {
                updateState {
                    it.copy(generatingState = BaseUIState.Error("Image is required"))
                }
                return@launch
            }
            if (uiStateValue.selectedStyle == null) {
                updateState {
                    it.copy(generatingState = BaseUIState.Error("Style is required"))
                }
                return@launch
            }
            val genResult = aiArtRepository.genArtAi(
                params = AiArtParams(
                    prompt = uiStateValue.prompt,
                    styleId = uiStateValue.selectedStyle.id,
                    positivePrompt = uiStateValue.prompt,
                    negativePrompt = uiStateValue.prompt,
                    imageUri = uiStateValue.imageUrl
                )
            )
            genResult.fold(
                onSuccess = { fileUrl ->
                    updateState {
                        it.copy(generatingState = BaseUIState.Success(fileUrl))
                    }
                    onSuccess(fileUrl)
                    updateState {
                        it.copy(generatingState = BaseUIState.Idle)
                    }
                },
                onFailure = { error ->
                    val message = when (error) {
                        is AiArtException -> {
                            when (error.errorReason) {
                                ErrorReason.PresignedLinkError -> {
                                    "Network error: Failed to get upload link. Please check your internet connection and try again."
                                }
                                ErrorReason.NetworkError -> {
                                    "Network error: Please check your internet connection and try again."
                                }
                                ErrorReason.ImageTypeInvalid -> {
                                    "Invalid image format. Please select a valid image file."
                                }
                                ErrorReason.GenerateImageError -> {
                                    "Image generation failed. Please try again."
                                }
                                else -> context.getString(error.errorReason.resMessage)
                            }
                        }
                        else -> {
                            error.message ?: ServiceConstants.UNKNOWN_ERROR_MESSAGE
                        }
                    }
                    updateState {
                        it.copy(generatingState = BaseUIState.Error(message))
                    }
                }
            )
        }
    }
}