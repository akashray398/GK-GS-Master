package com.akash.gkgsmaster.data.api.dto

import com.akash.gkgsmaster.data.model.Question
import com.google.gson.annotations.SerializedName

data class QuestionDto(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("options") val options: List<String>,
    @SerializedName("correct_option_index") val correctOptionIndex: Int,
    @SerializedName("category") val category: String,
    @SerializedName("explanation") val explanation: String?
)

fun QuestionDto.toEntity() = Question(
    id = id,
    text = text,
    options = options,
    correctOptionIndex = correctOptionIndex,
    category = category,
    explanation = explanation
)
