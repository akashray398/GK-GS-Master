package com.akash.gkgsmaster.data.database

import androidx.room.*
import com.akash.gkgsmaster.data.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions")
    fun getAllQuestions(): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("SELECT * FROM questions WHERE category = :category")
    fun getQuestionsByCategory(category: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE text LIKE '%' || :query || '%'")
    suspend fun searchQuestions(query: String): List<Question>

    @Query("DELETE FROM questions")
    suspend fun deleteAll()
}
