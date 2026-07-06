package com.akash.gkgsmaster.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akash.gkgsmaster.data.model.QuizHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(history: QuizHistory)

    @Query("SELECT * FROM quiz_history ORDER BY timestamp DESC")
    fun getAllQuizHistory(): Flow<List<QuizHistory>>

    @Query("SELECT SUM(score) FROM quiz_history")
    suspend fun getTotalScore(): Int?

    @Query("SELECT SUM(xpEarned) FROM quiz_history")
    suspend fun getTotalXp(): Int?

    @Query("SELECT AVG(accuracy) FROM quiz_history")
    suspend fun getAverageAccuracy(): Float?
}
