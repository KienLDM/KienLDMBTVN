package com.example.kienldmbtvn.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kienldmbtvn.ext.hideSystemBar
import com.example.kienldmbtvn.ui.style.StyleScreen
import com.example.kienldmbtvn.ui.theme.KienLDMBTVNTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemBar(
            hideStatusBar = true,
            hideNavigationBar = true,
            isLightStatusBar = false
        )
        enableEdgeToEdge()
        setContent {
            KienLDMBTVNTheme {
                StyleScreen()
            }
        }
    }
}