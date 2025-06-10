package com.example.kienldmbtvn.ui.style

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@Composable
fun StyleScreen(
    imageUri: Uri? = null,
    imageUrl: String = " ",
    viewModel: StyleViewModel = koinViewModel()
) {
    StyleContents(
        imageUri = imageUri ?: Uri.parse(imageUrl),
        imageUrl = imageUrl,
        viewModel = viewModel
    )
}

@Preview
@Composable
private fun StyleScreenPreview() {
    StyleScreen()
}