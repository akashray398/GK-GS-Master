package com.akash.gkgsmaster.data.api

import com.akash.gkgsmaster.data.api.dto.CountryDto
import com.akash.gkgsmaster.data.api.dto.DictionaryEntry
import com.akash.gkgsmaster.data.api.dto.WikipediaResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GKApiService {
    @GET
    suspend fun getAllCountries(@Url url: String = "https://restcountries.com/v3.1/all"): List<CountryDto>

    @GET("https://en.wikipedia.org/w/api.php")
    suspend fun getWikipediaExtract(
        @Query("action") action: String = "query",
        @Query("format") format: String = "json",
        @Query("prop") prop: String = "extracts",
        @Query("exintro") exintro: Int = 1,
        @Query("explaintext") explaintext: Int = 1,
        @Query("titles") titles: String
    ): WikipediaResponse

    @GET
    suspend fun getWordInfo(@Url url: String): List<DictionaryEntry>
}
