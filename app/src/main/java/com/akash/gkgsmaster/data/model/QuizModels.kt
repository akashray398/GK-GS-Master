package com.akash.gkgsmaster.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val userOptionIndex: Int = -1,
    val explanation: String,
    val category: String,
    val difficulty: String
) : Parcelable

@Parcelize
data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val timeTakenSeconds: Int,
    val category: String,
    val date: Long = System.currentTimeMillis(),
    val questions: List<Question> = emptyList(),
    val userAnswers: List<Int> = emptyList(),
    val xpEarned: Int = 0,
    val coinsEarned: Int = 0
) : Parcelable

data class QuizCategory(
    val id: String,
    val name: String,
    val iconRes: Int
)

enum class Difficulty {
    EASY, MEDIUM, HARD
}
