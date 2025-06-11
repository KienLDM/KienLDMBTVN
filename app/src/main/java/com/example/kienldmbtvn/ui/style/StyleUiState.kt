package com.example.kienldmbtvn.ui.style

import com.example.kienldmbtvn.data.response.CategoryItem
import com.example.kienldmbtvn.data.response.StyleItem

data class StyleUiState(
    val styles: List<StyleItem> = emptyList(),
    val selectedStyle: StyleItem? = null,
    val isStyleLoading: Boolean = false,
    val styleError: String? = null,

    val categories: List<CategoryItem> = emptyList(),
    val selectedCategory: CategoryItem? = null,
    val isCategoryLoading: Boolean = false,
    val categoryError: String? = null
)