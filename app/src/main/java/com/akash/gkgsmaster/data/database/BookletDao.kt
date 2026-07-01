package com.akash.gkgsmaster.data.database

import androidx.room.*
import com.akash.gkgsmaster.data.model.Booklet
import kotlinx.coroutines.flow.Flow

@Dao
interface BookletDao {
    @Query("SELECT * FROM booklets")
    fun getAllBooklets(): Flow<List<Booklet>>

    @Query("SELECT * FROM booklets WHERE id = :id")
    suspend fun getBookletById(id: String): Booklet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooklets(booklets: List<Booklet>)

    @Update
    suspend fun updateBooklet(booklet: Booklet)
}
