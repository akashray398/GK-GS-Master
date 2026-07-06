package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.data.database.BookletDao
import com.akash.gkgsmaster.data.model.Booklet
import com.akash.gkgsmaster.data.model.Chapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookletRepository @Inject constructor(
    private val bookletDao: BookletDao
) {

    fun getBooklets(): Flow<List<Booklet>> = bookletDao.getAllBooklets()

    fun getTodayBooklet(): Flow<Booklet?> = flow {
        val booklets = bookletDao.getAllBooklets().first()
        emit(booklets.randomOrNull())
    }

    suspend fun saveProgress(bookletId: String, pageNumber: Int) {
        bookletDao.getBookletById(bookletId)?.let {
            bookletDao.updateBooklet(it.copy(progress = pageNumber, lastReadPage = pageNumber))
        }
    }

    suspend fun toggleFavorite(bookletId: String) {
        bookletDao.getBookletById(bookletId)?.let {
            bookletDao.updateBooklet(it.copy(isFavourite = !it.isFavourite))
        }
    }

    suspend fun insertBooklet(booklet: Booklet) {
        bookletDao.insertBooklets(listOf(booklet))
        println("Repository: Inserted booklet ${booklet.title}")
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
