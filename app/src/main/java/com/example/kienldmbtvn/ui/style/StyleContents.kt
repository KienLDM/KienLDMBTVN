package com.example.kienldmbtvn.ui.style

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.kienldmbtvn.R
import com.example.kienldmbtvn.data.response.StyleItem
import com.example.kienldmbtvn.ui.navigation.AppNavRoutes
import com.example.kienldmbtvn.ui.theme.LocalCustomColors
import com.example.kienldmbtvn.ui.theme.LocalCustomTypography
import org.koin.androidx.compose.koinViewModel

@Composable
fun StyleContents(
    modifier: Modifier = Modifier,
    imageUri: Uri,
    imageUrl: String,
    navController: NavHostController,
    viewModel: StyleViewModel = koinViewModel(),
    onGenerate: (StyleItem, String) -> Unit = { _, _ -> }
) {
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var text by remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val noInternetMessage = stringResource(R.string.no_internet)

        LaunchedEffect(uiState.styleError) {
            uiState.styleError?.let {
                snackbarHostState.showSnackbar(
                    message = noInternetMessage,
                    withDismissAction = true
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(0.dp),
            snackbar = { snackbarData ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(LocalCustomColors.current.errorBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        color = LocalCustomColors.current.secondaryTextColor,
                        style = LocalCustomTypography.current.ErrorTypoGraphy.semiBold
                    )
                }
            }
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(27.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 27.dp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.style_prompt_entry),
                            style = LocalCustomTypography.current.PromptTypoGraphy.regular,
                            color = LocalCustomColors.current.promptTextColor
                        )
                    },
                    textStyle = LocalCustomTypography.current.PromptTypoGraphy.regular.copy(
                        color = LocalCustomColors.current.normalTextColor
                    ),
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = LocalCustomColors.current.primaryBorderColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LocalCustomColors.current.primaryBorderColor,
                        unfocusedBorderColor = LocalCustomColors.current.primaryBorderColor,
                        cursorColor = LocalCustomColors.current.normalTextColor
                    ),
                )

                IconButton(
                    onClick = { text = "" },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Clear text",
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(380.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        2.dp,
                        LocalCustomColors.current.primaryBorderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                    navController.navigate(AppNavRoutes.PhotoPicker.route)
                }
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
                text = stringResource(R.string.style_choose),
                modifier = Modifier
                    .padding(top = 27.dp)
                    .align(Alignment.Start),
                color = LocalCustomColors.current.primaryTextColor,
                style = LocalCustomTypography.current.Title.bold
            )

            CategoryLazyRow(
                isLoading = uiState.isCategoryLoading,
                categoryError = uiState.categoryError,
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategory?.id,
                onCategorySelected = { viewModel.selectCategory(it) }
            )

            StyleLazyRow(
                isLoading = uiState.isStyleLoading,
                styleError = uiState.styleError,
                styles = uiState.styles,
                selectedStyle = uiState.selectedStyle,
                onStyleSelected = { viewModel.selectStyle(it) }
            )

            GenerateButton(
                isEnabled = uiState.selectedStyle != null,
                onGenerate = {
                    uiState.selectedStyle?.let { selectedStyle ->
                        onGenerate(selectedStyle, text)
                    }
                }
            )
        }
    }
}