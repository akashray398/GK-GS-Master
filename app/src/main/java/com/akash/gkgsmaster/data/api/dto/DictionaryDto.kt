package com.akash.gkgsmaster.data.api.dto

import com.google.gson.annotations.SerializedName

data class DictionaryEntry(
    @SerializedName("word") val word: String,
    @SerializedName("phonetic") val phonetic: String?,
    @SerializedName("meanings") val meanings: List<MeaningDto>
)

data class MeaningDto(
    @SerializedName("partOfSpeech") val partOfSpeech: String,
    @SerializedName("definitions") val definitions: List<DefinitionDto>
)

data class DefinitionDto(
    @SerializedName("definition") val definition: String,
    @SerializedName("example") val example: String?
)
