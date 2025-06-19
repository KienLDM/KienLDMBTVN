package com.example.kienldmbtvn.ui.photopicker.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.kienldmbtvn.ui.navigation.AppNavigationHandler

@Composable
fun PhotoPickerScreen(navController: NavHostController) {
    PhotoPickerContents(
        onImageSelected = { selectedUri ->
            AppNavigationHandler.setImageUriAndNavigateBack(navController, selectedUri)
        },
        onCancel = {
            AppNavigationHandler.goBack(navController)
        }
    )
}