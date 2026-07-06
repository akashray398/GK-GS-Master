package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.api.GKApiService
import com.akash.gkgsmaster.data.model.GKCategory
import com.akash.gkgsmaster.data.model.GKArticle
import com.akash.gkgsmaster.data.api.dto.CountryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GKRepository @Inject constructor(
    private val gkApiService: GKApiService
) {

    fun getCountries(): Flow<List<CountryDto>> = flow {
        try {
            val countries = gkApiService.getAllCountries()
            emit(countries)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    suspend fun getWikipediaExtract(title: String): String {
        return try {
            val response = gkApiService.getWikipediaExtract(titles = title)
            // Wikipedia's response is deep: query -> pages -> (pageId) -> extract
            // For now we'll just return a simplified version or the raw response
            response.toString()
        } catch (e: Exception) {
            "Content not available"
        }
    }

    fun getCategories(): Flow<List<GKCategory>> = flow {
        emit(listOf(
            GKCategory("1", "World GK", R.drawable.ic_placeholder, R.color.primary),
            GKCategory("2", "India GK", R.drawable.ic_placeholder, R.color.secondary),
            GKCategory("3", "States", R.drawable.ic_placeholder, R.color.accent),
            GKCategory("4", "Capitals", R.drawable.ic_placeholder, R.color.glow),
            GKCategory("5", "History", R.drawable.ic_placeholder, R.color.primary),
            GKCategory("6", "Geography", R.drawable.ic_placeholder, R.color.secondary),
            GKCategory("7", "Awards", R.drawable.ic_placeholder, R.color.accent),
            GKCategory("8", "Books", R.drawable.ic_placeholder, R.color.glow),
            GKCategory("9", "Important Days", R.drawable.ic_placeholder, R.color.primary),
            GKCategory("10", "Rivers", R.drawable.ic_placeholder, R.color.secondary),
            GKCategory("11", "Mountains", R.drawable.ic_placeholder, R.color.accent)
        ))
    }
}
