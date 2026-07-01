package com.akash.gkgsmaster.data.database

import androidx.room.*
import com.akash.gkgsmaster.data.model.CurrentAffairEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentAffairDao {
    @Query("SELECT * FROM current_affairs ORDER BY date DESC")
    fun getAllCurrentAffairs(): Flow<List<CurrentAffairEntity>>

    @Query("SELECT * FROM current_affairs WHERE category = :category ORDER BY date DESC")
    fun getCurrentAffairsByCategory(category: String): Flow<List<CurrentAffairEntity>>

    @Query("SELECT * FROM current_affairs WHERE isBookmarked = 1 ORDER BY date DESC")
    fun getBookmarkedCurrentAffairs(): Flow<List<CurrentAffairEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentAffairs(items: List<CurrentAffairEntity>)

    @Query("UPDATE current_affairs SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: String, isBookmarked: Boolean)

    @Query("SELECT * FROM current_affairs WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    suspend fun searchCurrentAffairs(query: String): List<CurrentAffairEntity>

    @Query("DELETE FROM current_affairs")
    suspend fun deleteAll()
}
