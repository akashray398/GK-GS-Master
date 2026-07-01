package com.akash.gkgsmaster.data.database

import androidx.room.*
import com.akash.gkgsmaster.data.model.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE itemId = :itemId AND type = :type")
    suspend fun removeBookmark(itemId: String, type: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE itemId = :itemId AND type = :type)")
    fun isBookmarked(itemId: String, type: String): Flow<Boolean>
}
