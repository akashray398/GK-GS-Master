package com.akash.gkgsmaster.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GKArticle(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val highlights: String? = null,
    val importantFacts: List<String> = emptyList(),
    val bulletPoints: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val isBookmarked: Boolean = false,
    val imageUrl: String? = null
) : Parcelable

data class GKCategory(
    val id: String,
    val name: String,
    val iconRes: Int,
    val colorRes: Int
)
