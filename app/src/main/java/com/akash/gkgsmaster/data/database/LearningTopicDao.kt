package com.akash.gkgsmaster.data.database

import androidx.room.*
import com.akash.gkgsmaster.data.model.LearningTopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningTopicDao {
    @Query("SELECT * FROM learning_topics WHERE category = :category")
    fun getTopicsByCategory(category: String): Flow<List<LearningTopicEntity>>

    @Query("SELECT * FROM learning_topics WHERE id = :id")
    suspend fun getTopicById(id: String): LearningTopicEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<LearningTopicEntity>)

    @Query("UPDATE learning_topics SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: String, isBookmarked: Boolean)

    @Query("SELECT * FROM learning_topics WHERE isBookmarked = 1")
    fun getBookmarkedTopics(): Flow<List<LearningTopicEntity>>

    @Query("SELECT * FROM learning_topics WHERE lastReadTimestamp > 0 ORDER BY lastReadTimestamp DESC LIMIT :limit")
    fun getRecentlyRead(limit: Int): Flow<List<LearningTopicEntity>>

    @Query("SELECT * FROM learning_topics WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    suspend fun searchTopics(query: String): List<LearningTopicEntity>
}
