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
    val publisher: String? = null,
    val edition: String? = null,
    val subject: String? = null,
    val description: String? = null,
    val upscRelevance: String? = null,
    val topicsCovered: List<String> = emptyList(),
    val difficulty: String? = null, // Easy, Medium, Hard
    val coverImageUrl: String? = null,
    val totalChapters: Int = 0,
    val progress: Int = 0, // Percentage
    val lastReadPage: Int = 0,
    val language: String = "English", // English or Hindi
    val pdfUrl: String? = null,
    val isDownloaded: Boolean = false,
    val isFavourite: Boolean = false,
    val isBookmarked: Boolean = false,
    val category: String = "General",
    val type: BookletType = BookletType.ORIGINAL_BOOKLET,
    val purchaseUrl: String? = null,
    val officialSourceUrl: String? = null,
    val chapters: List<Chapter> = emptyList()
) : Parcelable

@Parcelize
enum class BookletType : Parcelable {
    ORIGINAL_BOOKLET, RECOMMENDED_BOOK
}

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
    val highlights: List<String> = emptyList(),
    val bookmarks: List<Int> = emptyList()
) : Parcelable
