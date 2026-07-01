package com.akash.gkgsmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_affairs")
data class CurrentAffairEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val date: String,
    val imageUrl: String? = null,
    val category: String,
    val isBookmarked: Boolean = false
)
