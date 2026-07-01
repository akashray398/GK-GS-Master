package com.akash.gkgsmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("options")
    val options: List<String>,
    @SerializedName("correct_option_index")
    val correctOptionIndex: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("explanation")
    val explanation: String? = null
)
