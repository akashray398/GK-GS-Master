package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.data.model.Booklet
import com.akash.gkgsmaster.data.model.BookletPage
import com.akash.gkgsmaster.data.model.Chapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookletRepository @Inject constructor() {

    fun getBooklets(): Flow<List<Booklet>> = flow {
        emit(listOf(
            Booklet(
                id = "1",
                title = "Indian Polity - Quick Revision",
                author = "GK Master Team",
                totalChapters = 12,
                progress = 25,
                chapters = listOf(
                    Chapter("c1", "Historical Background", 1, listOf(
                        BookletPage("p1", 1, "The British came to India in 1600 as traders..."),
                        BookletPage("p2", 2, "Company Rule (1773-1858)...")
                    )),
                    Chapter("c2", "Making of the Constitution", 2, listOf(
                        BookletPage("p3", 3, "It was in 1934 that the idea of a Constituent Assembly for India was put forward for the first time by M.N. Roy...")
                    ))
                )
            ),
            Booklet(
                id = "2",
                title = "Modern History Handbook",
                author = "Akash Singh",
                totalChapters = 8,
                progress = 10
            )
        ))
    }

    suspend fun saveProgress(bookletId: String, pageNumber: Int) {
        // Save to Database
    }

    suspend fun toggleFavorite(pageId: String) {
        // Toggle in Database
    }

    suspend fun searchBooklets(query: String): List<Booklet> {
        val booklets = listOf(
            Booklet("1", "Indian Polity", "GK Master Team", totalChapters = 12),
            Booklet("2", "Modern History", "Akash Singh", totalChapters = 8)
        )
        return if (query.isEmpty()) emptyList() else booklets.filter {
            it.title.contains(query, ignoreCase = true) || it.author.contains(query, ignoreCase = true)
        }
    }
}
