package com.akash.gkgsmaster.data.api

import com.akash.gkgsmaster.data.api.dto.TriviaResponse
import com.akash.gkgsmaster.data.api.dto.TheTriviaQuestionDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface QuizApiService {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String = "multiple"
    ): TriviaResponse

    @GET
    suspend fun getTheTriviaQuestions(
        @Url url: String = "https://the-trivia-api.com/v2/questions",
        @Query("limit") limit: Int = 10,
        @Query("categories") categories: String? = null,
        @Query("difficulties") difficulties: String? = null,
        @Query("tags") tags: String? = null
    ): List<TheTriviaQuestionDto>
}
