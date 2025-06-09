package com.example.kienldmbtvn.data.response

import com.google.gson.annotations.SerializedName

data class StyleResponse(
    val data: Data
)

data class Data(
    val items: List<StyleItem>
)

data class StyleItem(
    @SerializedName("_id") val id: String,
    val project: String,
    val name: String,
    val key: String? = null,
    val config: Config? = null,
    val mode: String? = null,
    val version: String? = null,
    val metadata: List<Any> = emptyList(),
    val priority: Int,
    val thumbnailApp: List<Thumbnail> = emptyList(),
    val categories: List<String> = emptyList(),
    @SerializedName("segment") val segmentId: String? = null,
    val subscriptionType: String? = null,
    val aiFamily: String? = null,
    val styleType: String? = null,
    val imageSize: String? = null,
    val baseModel: String? = null,
    val shouldCollectImage: Boolean? = null,
    @SerializedName("__v") val documentVersion: Int,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val pricing: Int? = null,
    val styles: List<StyleItem> = emptyList(),
    val thumbnail: ThumbnailSet? = null,
    val domain: Domain? = null
)

data class Config(
    val mode: Int,
    val baseModel: String,
    val style: String,
    val imageSize: String? = null,
    val positivePrompt: String? = null,
    val negativePrompt: String? = null,
    val fixOpenpose: Boolean? = null
)

data class Thumbnail(
    val thumbnail: String,
    @SerializedName("thumbnail_type") val thumbnailType: String,
    @SerializedName("_id") val id: String
)

data class ThumbnailSet(
    val before: String? = null,
    val after: String? = null,
    @SerializedName("preview_style") val previewStyle: String? = null,
    val key: String? = null,
    @SerializedName("reminder_after") val reminderAfter: String? = null,
    @SerializedName("reminder_before") val reminderBefore: String? = null,
    val noti: String? = null
)

data class Domain(
    @SerializedName("_id") val id: String,
    val displayName: String,
    val name: String,
    val thumbnail: String,
    val baseUrl: String,
    val priority: Int,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("__v") val documentVersion: Int
)