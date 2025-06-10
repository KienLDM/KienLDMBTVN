package com.example.kienldmbtvn.ui.style

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kienldmbtvn.R
import com.example.kienldmbtvn.data.response.CategoryItem
import com.example.kienldmbtvn.data.response.StyleItem
import com.example.kienldmbtvn.ui.theme.LocalCustomColors
import com.example.kienldmbtvn.ui.theme.LocalCustomTypography

@Composable
fun GenerateButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onGenerate: () -> Unit,
) {
    val backgroundModifier = if (isEnabled) {
        Modifier.background(
            brush = LocalCustomColors.current.buttonBackground,
            shape = RoundedCornerShape(8.dp)
        )
    } else {
        Modifier.background(
//            brush = LocalCustomColors.current.buttonBackground.copy(alpha = 0.2f),
            color = Color.White.copy(alpha = 0.3f),
            shape = RoundedCornerShape(8.dp)
        )
    }

    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .then(backgroundModifier)
            .clip(RoundedCornerShape(8.dp)),
        onClick = onGenerate,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
        ),
        enabled = isEnabled
    ) {
        Text(stringResource(R.string.style_generate))
    }
}

@Composable
fun CategoryLazyRow(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    categoryError: String? = null,
    categories: List<CategoryItem> = emptyList(),
    selectedCategoryId: String? = null,
    onCategorySelected: (CategoryItem) -> Unit = {}
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        if (isLoading) {
            /*                item {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp)
                                )
                            }*/
        } else if (categoryError != null) {
            /*                item {
                                Text(
                                    text = "Error: $categoryError",
                                    color = Color.Red
                                )
                            }*/
        } else {
            items(categories) { category ->
                val isCategorySelected = selectedCategoryId == category.id
                Text(
                    text = category.name,
                    color = if (isCategorySelected) {
                        LocalCustomColors.current.primaryTextColor
                    } else {
                        LocalCustomColors.current.normalTextColor
                    },
                    style = LocalCustomTypography.current.CategoryTypography.bold,
                    modifier = Modifier
                        .clickable { onCategorySelected(category) },
                )
            }
        }
    }
}

@Composable
fun StyleLazyRow(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    styleError: String? = null,
    styles: List<StyleItem> = emptyList(),
    selectedStyle: StyleItem? = null,
    onStyleSelected: (StyleItem) -> Unit = {},
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        if (isLoading) {
            /*                item {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp)
                                )
                            }*/
        } else if (styleError != null) {
            /*                item {
                                Text(
                                    text = "Error: ${uiState.styleError}",
                                    color = Color.Red
                                )
                            }*/
        } else {
            items(styles) { style ->
                StyleCard(
                    imageUrl = style.key,
                    styleName = style.name,
                    isSelected = selectedStyle?.id == style.id,
                    onClick = { onStyleSelected(style) }
                )
            }
        }
    }
}

@Composable
fun StyleCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    styleName: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) LocalCustomColors.current.chosenTextColor else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = styleName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(LocalCustomColors.current.chosenTextColor.copy(alpha = 0.3f))
                )
            }
        }

        Text(
            text = styleName,
            color = if (isSelected) LocalCustomColors.current.chosenTextColor else LocalCustomColors.current.normalTextColor,
            style = LocalCustomTypography.current.StyleTypoGraphy.semiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun NoInternetBanner(
    modifier: Modifier = Modifier,
    message: String = stringResource(R.string.no_internet)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(LocalCustomColors.current.errorBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = LocalCustomColors.current.secondaryTextColor,
            style = LocalCustomTypography.current.ErrorTypoGraphy.semiBold
        )
    }
}