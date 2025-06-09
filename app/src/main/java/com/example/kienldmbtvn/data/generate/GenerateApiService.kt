package com.example.kienldmbtvn.data.generate

import com.example.kienldmbtvn.data.response.GenerateResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface GenerateApiService {
    @Multipart
    @POST("api/v3.3/image-ai")
    suspend fun generateImage(
        @Header("Authorization") token: String,
        @Header("x-api-signature") signature: String,
        @Header("x-api-timestamp") timestamp: String,
        @Header("x-api-keyid") keyId: String,
        @Part file: MultipartBody.Part,
        @Query("styleId") styleId: String,
        @Query("negativePrompt") negativePrompt: String? = null
    ): Response<GenerateResponse>
}