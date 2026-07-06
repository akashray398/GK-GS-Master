package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.data.api.NewsApiService
import com.akash.gkgsmaster.data.api.dto.toEntity
import com.akash.gkgsmaster.data.database.CurrentAffairDao
import com.akash.gkgsmaster.data.model.CurrentAffairEntity
import com.akash.gkgsmaster.utils.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentAffairRepository @Inject constructor(
    private val newsApiService: NewsApiService,
    private val currentAffairDao: CurrentAffairDao,
) {
    // Production API Key for NewsAPI (Ideally should be in BuildConfig)
    private val apiKey = "4a1d8f7b7e8d4a6f8a0e9c2b4d1e2f3a" 

    fun getCurrentAffairs(category: String? = null): Flow<Resource<List<CurrentAffairEntity>>> = flow {
        emit(Resource.Loading())
        
        val cached = currentAffairDao.getAllCurrentAffairs().first()
        emit(Resource.Loading(cached))

        try {
            val response = newsApiService.getTopHeadlines(
                category = category,
                country = "in",
                apiKey = apiKey,
            )
            val entities = response.articles.map { it.toEntity(category ?: "General") }
            currentAffairDao.insertCurrentAffairs(entities)
            
            emitAll(currentAffairDao.getAllCurrentAffairs().map { Resource.Success(it) })
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch news", cached))
        }
    }

    fun getBookmarkedCurrentAffairs(): Flow<List<CurrentAffairEntity>> = 
        currentAffairDao.getBookmarkedCurrentAffairs()

    suspend fun toggleBookmark(id: String, isBookmarked: Boolean) {
        currentAffairDao.updateBookmark(id, isBookmarked)
    }

    suspend fun searchAffairs(query: String): List<CurrentAffairEntity> {
        return currentAffairDao.searchCurrentAffairs(query)
    }
}
