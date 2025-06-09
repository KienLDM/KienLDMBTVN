package com.example.kienldmbtvn.data.style

import com.example.kienldmbtvn.data.ApiConst
import com.example.kienldmbtvn.data.response.StyleResponse
import retrofit2.Response
import retrofit2.http.GET

interface StyleApiService {
    @GET(ApiConst.LIST_STYLE)
    suspend fun getStyles(): Response<StyleResponse>
}