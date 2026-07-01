package com.akash.gkgsmaster.data.api

import com.akash.gkgsmaster.data.api.dto.CurrentAffairDto
import com.akash.gkgsmaster.data.api.dto.QuestionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("questions")
    suspend fun getQuestions(
        @Query("category") category: String? = null,
        @Query("difficulty") difficulty: String? = null
    ): List<QuestionDto>

    @GET("current-affairs")
    suspend fun getCurrentAffairs(
        @Query("category") category: String? = null
    ): List<CurrentAffairDto>

    @GET("booklets")
    suspend fun getBooklets(): List<com.akash.gkgsmaster.data.model.Booklet>
}
