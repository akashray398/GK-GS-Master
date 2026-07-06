package com.akash.gkgsmaster.data.api.dto

import com.akash.gkgsmaster.data.model.CurrentAffairEntity
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<NewsArticleDto>
)

data class NewsArticleDto(
    @SerializedName("source") val source: NewsSourceDto,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("content") val content: String?
)

data class NewsSourceDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String
)

fun NewsArticleDto.toEntity(category: String) = CurrentAffairEntity(
    id = url, // Using URL as ID for uniqueness
    title = title,
    content = content ?: description ?: "",
    date = publishedAt,
    imageUrl = urlToImage,
    category = category,
    isBookmarked = false
)
