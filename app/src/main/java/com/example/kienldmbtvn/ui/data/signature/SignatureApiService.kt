package com.example.kienldmbtvn.ui.data.signature

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SignatureApi {
    @GET("api/image-ai/unprotected")
    suspend fun getSignature(@Query("apiKey") apiKey: String): Response<Void>
}