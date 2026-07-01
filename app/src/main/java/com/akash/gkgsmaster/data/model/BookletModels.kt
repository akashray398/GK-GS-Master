package com.akash.gkgsmaster.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "booklets")
data class Booklet(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val coverImageUrl: String? = null,
    val totalChapters: Int,
    val progress: Int = 0, // Percentage
    val chapters: List<Chapter> = emptyList()
) : Parcelable

@Parcelize
data class Chapter(
    val id: String,
    val title: String,
    val order: Int,
    val pages: List<BookletPage> = emptyList()
) : Parcelable

@Parcelize
data class BookletPage(
    val id: String,
    val pageNumber: Int,
    val content: String,
    val isFavorite: Boolean = false,
    val bookmarks: List<Int> = emptyList() // Indices of highlighted text
) : Parcelable
