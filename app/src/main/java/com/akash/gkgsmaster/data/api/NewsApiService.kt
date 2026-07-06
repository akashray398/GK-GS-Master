package com.akash.gkgsmaster.data.api

import com.akash.gkgsmaster.data.api.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String? = null,
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}
