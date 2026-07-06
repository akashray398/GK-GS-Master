package com.akash.gkgsmaster.data.api.dto

import com.akash.gkgsmaster.data.model.Question
import com.google.gson.annotations.SerializedName

data class TheTriviaQuestionDto(
    @SerializedName("id") val id: String,
    @SerializedName("question") val question: TheTriviaQuestionTextDto,
    @SerializedName("correctAnswer") val correctAnswer: String,
    @SerializedName("incorrectAnswers") val incorrectAnswers: List<String>,
    @SerializedName("category") val category: String,
    @SerializedName("difficulty") val difficulty: String
)

data class TheTriviaQuestionTextDto(
    @SerializedName("text") val text: String
)

fun TheTriviaQuestionDto.toEntity(): Question {
    val allOptions = (incorrectAnswers + correctAnswer).shuffled()
    return Question(
        id = id,
        text = question.text,
        options = allOptions,
        correctOptionIndex = allOptions.indexOf(correctAnswer),
        category = category,
        difficulty = difficulty.replaceFirstChar { it.uppercase() },
        explanation = "Category: $category, Difficulty: $difficulty"
    )
}
