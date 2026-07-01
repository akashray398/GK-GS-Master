package com.akash.gkgsmaster.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class SearchResult : Parcelable {
    abstract val id: String
    abstract val title: String
    abstract val type: SearchResultType

    @Parcelize
    data class GKArticleResult(
        override val id: String,
        override val title: String,
        val category: String,
        override val type: SearchResultType = SearchResultType.GK
    ) : SearchResult()

    @Parcelize
    data class ScienceTopicResult(
        override val id: String,
        override val title: String,
        val category: String,
        override val type: SearchResultType = SearchResultType.SCIENCE
    ) : SearchResult()

    @Parcelize
    data class BookletResult(
        override val id: String,
        override val title: String,
        val author: String,
        override val type: SearchResultType = SearchResultType.BOOKLET
    ) : SearchResult()

    @Parcelize
    data class CurrentAffairResult(
        override val id: String,
        override val title: String,
        val date: String,
        override val type: SearchResultType = SearchResultType.AFFAIR
    ) : SearchResult()

    @Parcelize
    data class QuestionResult(
        override val id: String,
        override val title: String, // The question text
        val category: String,
        override val type: SearchResultType = SearchResultType.QUIZ
    ) : SearchResult()

    @Parcelize
    data class NoteResult(
        override val id: String,
        override val title: String,
        override val type: SearchResultType = SearchResultType.NOTE
    ) : SearchResult()
}

enum class SearchResultType {
    GK, SCIENCE, BOOKLET, AFFAIR, QUIZ, NOTE
}
