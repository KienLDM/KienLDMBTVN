package com.example.common.di

import com.example.common.data.photo.PhotoRepository
import com.example.kienldmbtvn.data.style.StyleApiClient
import com.example.kienldmbtvn.data.style.StyleApiService
import com.example.kienldmbtvn.data.style.StyleRepository
import com.example.kienldmbtvn.data.style.StyleRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<StyleApiService> { StyleApiClient.api }
    single<StyleRepository> { StyleRepositoryImpl(get()) }
    viewModelOf(::StyleViewModel)

    single { PhotoRepository(get()) }
    viewModelOf(::PhotoPickerViewModel)
}