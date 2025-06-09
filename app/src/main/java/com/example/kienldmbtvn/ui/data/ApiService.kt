package com.example.kienldmbtvn.ui.data

import com.example.kienldmbtvn.ui.data.response.StyleResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(ApiConst.LIST_STYLE)
    suspend fun getStyles(): Response<StyleResponse>
}