package com.example.kienldmbtvn.data.response

import com.google.gson.annotations.SerializedName

data class StyleResponse (
    val data: Data
)

data class Data (
    val items: List<StyleItem>
)

data class StyleItem(
    @SerializedName("_id") val id: String,
    val project: String,
    val name: String,
    val key: String,
    val config: Config,
    val mode: String,
    val version: String,
    val metadata: List<Any>,
    val priority: Int,
    val thumbnailApp: List<Any>,
    val categories: List<Any>,
    val segmentId: String,
    val subscriptionType: String,
    val aiFamily: String,
    val styleType: String,
    val imageSize: String,
    val baseModel: String,
    val shouldCollectImage: Boolean,
    @SerializedName("__v") val versionCode: Int,
    val createdAt: String,
    val updatedAt: String,
    val pricing: Int
)

data class Config(
    val mode: Int,
    val baseModel: String,
    val style: String,
    val imageSize: String? = null
)
