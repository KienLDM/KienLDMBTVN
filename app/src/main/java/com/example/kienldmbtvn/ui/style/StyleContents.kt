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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import android.util.Log
import com.example.kienldmbtvn.R
import com.example.kienldmbtvn.base.BaseUIState
import com.example.kienldmbtvn.data.network.response.StyleItem
import com.example.kienldmbtvn.ui.navigation.AppNavigationHandler
import com.example.kienldmbtvn.ui.theme.LocalCustomColors
import com.example.kienldmbtvn.ui.theme.LocalCustomTypography
import org.koin.androidx.compose.koinViewModel
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kienldmbtvn.ui.photopicker.activity.PhotoPickerActivity
import com.example.kienldmbtvn.ui.style.StyleUiState
import com.example.kienldmbtvn.ui.style.StyleViewModel

@Composable
fun StyleContents(
    modifier: Modifier = Modifier,
    imageUri: Uri,
    imageUrl: String,
    isImageSelected: Boolean,
    navController: NavHostController,
    viewModel: StyleViewModel = koinViewModel(),
    onGenerate: (StyleItem, String) -> Unit = { _, _ -> }
) {
    // Update the imageUri in the ViewModel to ensure it's available for API calls
    LaunchedEffect(imageUri) {
        viewModel.updateImageUrl(imageUri)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var text by remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val noInternetMessage = stringResource(R.string.no_internet)
        val context = LocalContext.current

        val currentState = uiState
        
        LaunchedEffect(currentState) {
            if (currentState is BaseUIState.Success) {
                currentState.data.styleError?.let {
                    snackbarHostState.showSnackbar(
                        message = noInternetMessage,
                        withDismissAction = true
                    )
                }
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

        when (currentState) {
            is BaseUIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is BaseUIState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentState.message,
                        color = LocalCustomColors.current.errorBackgroundColor,
                        style = LocalCustomTypography.current.Title.bold
                    )
                }
            }
            is BaseUIState.Success -> {
                StyleSuccessContent(
                    modifier = modifier,
                    styleData = currentState.data,
                    text = text,
                    onTextChanged = { text = it },
                    imageUri = imageUri.toString(),
                    isImageSelected = isImageSelected,
                    navController = navController,
                    viewModel = viewModel,
                    context = context,
                    snackbarHostState = snackbarHostState
                )
            }
            is BaseUIState.Idle -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun StyleSuccessContent(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    imageUri: String?,
    isImageSelected: Boolean,
    styleData: StyleData,
    navController: NavHostController,
    viewModel: StyleViewModel,
    context: android.content.Context,
    snackbarHostState: SnackbarHostState
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri = result.data?.getStringExtra(PhotoPickerActivity.EXTRA_SELECTED_IMAGE_URI)
            selectedImageUri?.let { uri ->
                viewModel.setImageUri(uri)
            }
        }
    }

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
                    onValueChange = onTextChanged,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.style_prompt_entry),
                            style = LocalCustomTypography.current.PromptTypoGraphy.regular,
                            color = LocalCustomColors.current.promptTextColor
                        )
                    },
                    textStyle = LocalCustomTypography.current.PromptTypoGraphy.regular,
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
                    onClick = { onTextChanged("") },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete_prompt),
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
            ) {
                if (isImageSelected || styleData.imageUrl != null) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(
                            model = styleData.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Fit,
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(24.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .background(LocalCustomColors.current.primaryBorderColor.copy(0.7f))
                        ) {
                            IconButton(
                                onClick = { 
                                    val intent = Intent(context, PhotoPickerActivity::class.java)
                                    photoPickerLauncher.launch(intent)
                                },
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_rechoose_image),
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                val intent = Intent(context, PhotoPickerActivity::class.java)
                                photoPickerLauncher.launch(intent)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_add_photo),
                                contentDescription = "Add photo",
                                tint = LocalCustomColors.current.promptTextColor,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = stringResource(R.string.add_your_photo),
                                color = LocalCustomColors.current.promptTextColor,
                                style = LocalCustomTypography.current.PromptTypoGraphy.regular,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
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
                isLoading = styleData.isCategoryLoading,
                categoryError = styleData.categoryError,
                categories = styleData.categories,
                selectedCategoryId = styleData.selectedCategory?.id,
                onCategorySelected = { viewModel.selectCategory(it) }
            )

            StyleLazyRow(
                isLoading = styleData.isStyleLoading,
                styleError = styleData.styleError,
                styles = styleData.styles,
                selectedStyle = styleData.selectedStyle,
                onStyleSelected = { viewModel.selectStyle(it) }
            )

            CommonButton(
                textContent = R.string.style_generate,
                isEnabled = styleData.selectedStyle != null,
                isLoading = styleData.generatingState.isLoading(),
                onGenerate = {
                    styleData.selectedStyle?.let {
                        viewModel.updatePrompt(text)
                        viewModel.generateImage(context = context) { resultUrl ->
                            AppNavigationHandler.navigateToResult(navController, resultUrl)
                        }
                    }
                }
            )
        }
        
        if (styleData.generatingState.isLoading()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = LocalCustomColors.current.primaryTextColor,
                        strokeWidth = 4.dp
                    )
                    Text(
                        text = "Generating your AI art...",
                        color = Color.White,
                        style = LocalCustomTypography.current.PromptTypoGraphy.semiBold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
        
        if (styleData.generatingState.isError()) {
            LaunchedEffect(styleData.generatingState) {
                val errorMessage = (styleData.generatingState as BaseUIState.Error).message
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    withDismissAction = true
                )
            }
        }
}