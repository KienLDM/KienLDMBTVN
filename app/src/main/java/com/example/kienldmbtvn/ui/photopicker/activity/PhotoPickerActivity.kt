package com.example.kienldmbtvn.ui.photopicker.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import com.example.kienldmbtvn.base.BaseComposeActivity
import com.example.kienldmbtvn.ui.photopicker.screen.PhotoPickerScreen

class PhotoPickerActivity : BaseComposeActivity() {
    
    companion object {
        const val EXTRA_SELECTED_IMAGE_URI = "selected_image_uri"
    }

    @Composable
    override fun ScreenContent() {
        PhotoPickerScreen(
            onImageSelected = { selectedUri ->
                setResultAndFinish(selectedUri)
            },
            onCancel = {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        )
    }
    
    private fun setResultAndFinish(selectedUri: Uri?) {
        if (selectedUri != null) {
            val resultIntent = Intent().apply {
                putExtra(EXTRA_SELECTED_IMAGE_URI, selectedUri.toString())
            }
            setResult(Activity.RESULT_OK, resultIntent)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
        finish()
    }
}