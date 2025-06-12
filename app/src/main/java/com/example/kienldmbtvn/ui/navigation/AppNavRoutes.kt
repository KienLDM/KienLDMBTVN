package com.example.kienldmbtvn.ui.navigation

sealed class AppNavRoutes(val route: String) {
    object Style : AppNavRoutes("style")
    object PhotoPicker : AppNavRoutes("photo_picker")
    object Result : AppNavRoutes("result")

    data class Detail(val id: String) : AppNavRoutes("detail/$id") {
        companion object {
            const val routePattern = "detail/{id}"
        }
    }
}