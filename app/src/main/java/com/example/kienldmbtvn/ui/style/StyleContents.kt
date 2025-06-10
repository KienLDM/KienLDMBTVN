package com.example.kienldmbtvn.ui.style

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.kienldmbtvn.R
import com.example.kienldmbtvn.data.response.CategoryItem
import com.example.kienldmbtvn.data.response.StyleItem
import com.example.kienldmbtvn.ui.theme.LocalCustomColors
import org.koin.androidx.compose.koinViewModel

@Composable
fun StyleContents(
    modifier: Modifier = Modifier,
    imageUri: Uri,
    imageUrl: String,
    viewModel: StyleViewModel = koinViewModel(),
    onGenerate: (StyleItem, String) -> Unit = { _, _ -> }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(27.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val categoryUiState by viewModel.categoryUiState.collectAsStateWithLifecycle()
        var text by remember { mutableStateOf("") }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(stringResource(id = R.string.style_prompt_entry)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Box(
            modifier = Modifier
                .size(380.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, LocalCustomColors.current.primaryBorderColor)
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
            )
        }

        Text(
            text = "Categories",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
        ) {
            when (categoryUiState) {
                is CategoryUiState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                is CategoryUiState.Error -> {
                    item {
                        Text(
                            text = "Error: ${(categoryUiState as CategoryUiState.Error).message}",
                            color = Color.Red
                        )
                    }
                }

                is CategoryUiState.Success -> {
                    val categories = (categoryUiState as CategoryUiState.Success).categories
                    val selectedCategory = (categoryUiState as CategoryUiState.Success).selectedCategory

                    items(categories) { category ->
                        Text(category.name, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }

        Text(
            text = stringResource(id = R.string.style_choose),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
        ) {
            when (uiState) {
                is StyleUiState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                is StyleUiState.Error -> {
                    item {
                        Text(
                            text = "Error: ${(uiState as StyleUiState.Error).message}",
                            color = Color.Red
                        )
                    }
                }

                is StyleUiState.Success -> {
                    val styles = (uiState as StyleUiState.Success).styles
                    val selectedStyle = (uiState as StyleUiState.Success).selectedStyle

                    items(styles) { style ->
                        StyleCard(
                            imageUrl = style.key,
                            styleName = style.name,
                            isSelected = selectedStyle?.id == style.id,
                            onClick = { viewModel.selectStyle(style) }
                        )

                        Log.d("StyleDebug", "StyleContents: ${style.key}")
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = {
                val currentState = uiState
                if (currentState is StyleUiState.Success && currentState.selectedStyle != null) {
                    onGenerate(currentState.selectedStyle, text)
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalCustomColors.current.primaryBorderColor,
                contentColor = Color.White
            ),
            enabled = uiState is StyleUiState.Success && (uiState as? StyleUiState.Success)?.selectedStyle != null
        ) {
            Text(stringResource(R.string.style_generate))
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
        modifier = modifier
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) LocalCustomColors.current.primaryBorderColor else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = styleName,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = styleName,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}