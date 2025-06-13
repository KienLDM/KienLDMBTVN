package com.example.kienldmbtvn.di

import com.example.kienldmbtvn.data.style.StyleApiClient
import com.example.kienldmbtvn.data.style.StyleApiService
import com.example.kienldmbtvn.data.style.StyleRepository
import com.example.kienldmbtvn.data.style.StyleRepositoryImpl
import com.example.kienldmbtvn.ui.style.StyleViewModel
import com.example.kienldmbtvn.ui.photopicker.PhotoPickerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<StyleApiService> { StyleApiClient.api }
    single<StyleRepository> { StyleRepositoryImpl(get()) }
    viewModelOf(::StyleViewModel)

    viewModelOf(::PhotoPickerViewModel)
}