package com.akash.gkgsmaster.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    @SerializedName("difficulty")
    val difficulty: String = "Medium",
    @SerializedName("explanation")
    val explanation: String? = null,
    @SerializedName("why_correct")
    val whyCorrect: String? = null,
    @SerializedName("why_incorrect")
    val whyOthersIncorrect: String? = null,
    @SerializedName("related_facts")
    val relatedFacts: List<String> = emptyList(),
    @SerializedName("upsc_tip")
    val upscTip: String? = null,
    @SerializedName("reference_topic")
    val referenceTopic: String? = null,
    @SerializedName("is_bookmarked")
    val isBookmarked: Boolean = false
) : Parcelable
