package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.api.ApiService
import com.akash.gkgsmaster.data.api.dto.toEntity
import com.akash.gkgsmaster.data.database.*
import com.akash.gkgsmaster.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GKRepository @Inject constructor(
    private val apiService: ApiService,
    private val questionDao: QuestionDao,
    private val currentAffairDao: CurrentAffairDao,
    private val bookletDao: BookletDao,
    private val noteDao: NoteDao,
    private val bookmarkDao: BookmarkDao
) {

    suspend fun syncData() {
        // Sync Current Affairs
        try {
            val affairs = apiService.getCurrentAffairs(null)
            currentAffairDao.insertCurrentAffairs(affairs.map { it.toEntity() })
        } catch (e: Exception) {}

        // Sync Booklets
        try {
            val booklets = apiService.getBooklets()
            bookletDao.insertBooklets(booklets)
        } catch (e: Exception) {}

        // Sync Unsynced Notes
        val unsyncedNotes = noteDao.getUnsyncedNotes()
        for (note in unsyncedNotes) {
            try {
                // apiService.uploadNote(note)
                // noteDao.updateNote(note.copy(isSynced = true))
            } catch (e: Exception) {}
        }
    }

    fun getCategories(): Flow<List<GKCategory>> = flow {
        emit(listOf(
            GKCategory("1", "World GK", R.drawable.ic_placeholder, R.color.primary),
            GKCategory("2", "India GK", R.drawable.ic_placeholder, R.color.secondary),
            GKCategory("3", "States", R.drawable.ic_placeholder, R.color.accent),
            GKCategory("4", "Capitals", R.drawable.ic_placeholder, R.color.glow),
            GKCategory("5", "History", R.drawable.ic_placeholder, R.color.primary),
            GKCategory("6", "Geography", R.drawable.ic_placeholder, R.color.secondary),
            GKCategory("7", "Awards", R.drawable.ic_placeholder, R.color.accent),
            GKCategory("8", "Books", R.drawable.ic_placeholder, R.color.glow),
            GKCategory("9", "Important Days", R.drawable.ic_placeholder, R.color.primary),
            GKCategory("10", "Rivers", R.drawable.ic_placeholder, R.color.secondary),
            GKCategory("11", "Mountains", R.drawable.ic_placeholder, R.color.accent)
        ))
    }

    fun getArticles(categoryId: String): Flow<List<GKArticle>> = flow {
        // Placeholder articles
        val articles = listOf(
            GKArticle("1", "Ancient History of India", "Detailed content about ancient India...", "History"),
            GKArticle("2", "Medieval History of India", "Detailed content about medieval India...", "History"),
            GKArticle("3", "Modern History of India", "Detailed content about modern India...", "History")
        )
        emit(articles)
    }

    suspend fun searchArticles(query: String): List<GKArticle> {
        val allArticles = listOf(
            GKArticle("1", "Ancient History of India", "Detailed content about ancient India...", "History"),
            GKArticle("2", "Medieval History of India", "Detailed content about medieval India...", "History"),
            GKArticle("3", "Modern History of India", "Detailed content about modern India...", "History"),
            GKArticle("4", "World Geography Overview", "Global geography details...", "Geography"),
            GKArticle("5", "Indian Rivers System", "Major rivers in India...", "Rivers")
        )
        return if (query.isEmpty()) emptyList() else allArticles.filter { 
            it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
        }
    }
}
