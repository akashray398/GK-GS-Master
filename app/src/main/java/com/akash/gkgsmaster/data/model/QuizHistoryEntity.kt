package com.akash.gkgsmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_history")
data class QuizHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val difficulty: String,
    val score: Int,
    val totalQuestions: Int,
    val timeTakenSeconds: Int,
    val accuracy: Float,
    val xpEarned: Int,
    val coinsEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)
