package com.example.kienldmbtvn.ui.style

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@Composable
fun StyleScreen(
    imageUri: Uri? = null,
    imageUrl: String = "https://images.unsplash.com/photo-1575936123452-b67c3203c357?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8aW1hZ2V8ZW58MHx8MHx8fDA%3D",
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