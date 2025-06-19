package com.example.kienldmbtvn.ui.navigation

import java.net.URLEncoder

sealed class AppNavRoutes(val route: String) {
    data object Style : AppNavRoutes("style")
    data object Result : AppNavRoutes("result/{imageUrl}") {
        fun createRoute(imageUrl: String): String {
            return "result/${URLEncoder.encode(imageUrl, "UTF-8")}"
        }
    }
}