package com.akash.gkgsmaster.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScienceTopic(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val imageUrl: String? = null,
    val tables: List<ScienceTable> = emptyList(),
    val importantPoints: List<String> = emptyList(),
    val examples: List<String> = emptyList()
) : Parcelable

@Parcelize
data class ScienceTable(
    val title: String? = null,
    val headers: List<String>,
    val rows: List<List<String>>
) : Parcelable

data class ScienceCategory(
    val id: String,
    val name: String,
    val iconRes: Int,
    val colorRes: Int
)
