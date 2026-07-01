package com.akash.gkgsmaster.data.api.dto

import com.akash.gkgsmaster.data.model.CurrentAffairEntity
import com.google.gson.annotations.SerializedName

data class CurrentAffairDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("date") val date: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("category") val category: String?
)

fun CurrentAffairDto.toEntity() = CurrentAffairEntity(
    id = id,
    title = title,
    content = content,
    date = date,
    imageUrl = imageUrl,
    category = category ?: "National",
    isBookmarked = false
)
