package com.example.kienldmbtvn.ui.photopicker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel

@Composable
fun PhotoPickerScreen(
    onPhotoSelected: (Photo) -> Unit,
    onNextClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: PhotoPickerViewModel = koinViewModel()
    var selectedPhotos by remember { mutableStateOf(listOf<Photo>()) }

    LaunchedEffect(Unit) {
        viewModel.checkAndFetchPhotos(context)
    }
}