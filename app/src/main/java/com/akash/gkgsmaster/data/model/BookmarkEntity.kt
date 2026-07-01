package com.akash.gkgsmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: String,
    val type: String, // "QUESTION", "BOOKLET", "AFFAIR"
    val timestamp: Long = System.currentTimeMillis()
)
