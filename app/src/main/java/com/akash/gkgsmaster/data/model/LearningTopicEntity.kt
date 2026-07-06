package com.akash.gkgsmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learning_topics")
data class LearningTopicEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val content: String,
    val imageUrl: String? = null,
    val importantFacts: List<String> = emptyList(),
    val highlights: String? = null,
    val bulletPoints: List<String> = emptyList(),
    val tablesJson: String? = null, // Store formatted tables as JSON string
    val isBookmarked: Boolean = false,
    val lastReadTimestamp: Long = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)
