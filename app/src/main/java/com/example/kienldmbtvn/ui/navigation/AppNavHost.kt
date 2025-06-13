package com.example.kienldmbtvn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kienldmbtvn.ui.photopicker.PhotoPickerScreen
import com.example.kienldmbtvn.ui.style.StyleScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppNavRoutes.Style.route,
    ) {
        composable(AppNavRoutes.Style.route) {
            StyleScreen(navController = navController)
        }

        composable(AppNavRoutes.PhotoPicker.route) {
            PhotoPickerScreen(navController = navController)
        }
    }
}