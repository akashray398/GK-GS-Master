package com.akash.gkgsmaster.data.api.dto

import com.akash.gkgsmaster.data.model.Question
import com.google.gson.annotations.SerializedName
import android.text.Html

data class TriviaResponse(
    @SerializedName("response_code") val responseCode: Int,
    @SerializedName("results") val results: List<TriviaQuestionDto>
)

data class TriviaQuestionDto(
    @SerializedName("category") val category: String,
    @SerializedName("type") val type: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("question") val question: String,
    @SerializedName("correct_answer") val correct_answer: String,
    @SerializedName("incorrect_answers") val incorrect_answers: List<String>
)

fun TriviaQuestionDto.toEntity(): Question {
    val allOptions = (incorrect_answers + correct_answer).shuffled()
    return Question(
        id = question.hashCode().toString(),
        text = Html.fromHtml(question, Html.FROM_HTML_MODE_LEGACY).toString(),
        options = allOptions.map { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString() },
        correctOptionIndex = allOptions.indexOf(correct_answer),
        category = category,
        explanation = "Category: $category, Difficulty: $difficulty"
    )
}
