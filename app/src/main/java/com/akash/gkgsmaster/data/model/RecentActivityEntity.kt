package com.akash.gkgsmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_activity")
data class RecentActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val type: String, // "QUIZ", "LEARN", "NOTE"
    val timestamp: Long = System.currentTimeMillis()
)
