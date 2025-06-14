package com.example.kienldmbtvn.ui.style

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel

@Composable
fun StyleScreen(
    imageUri: Uri? = null,
    imageUrl: String = " ",
    navController: NavHostController,
    viewModel: StyleViewModel = koinViewModel()
) {
    StyleContents(
        imageUri = imageUri ?: Uri.parse(imageUrl),
        imageUrl = imageUrl,
        navController = navController,
        viewModel = viewModel
    )
}

//@Preview
//@Composable
//private fun StyleScreenPreview() {
//    StyleScreen()
//}