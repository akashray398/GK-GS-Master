package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.data.api.ApiService
import com.akash.gkgsmaster.data.api.dto.toEntity
import com.akash.gkgsmaster.data.database.CurrentAffairDao
import com.akash.gkgsmaster.data.model.CurrentAffairEntity
import com.akash.gkgsmaster.utils.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentAffairRepository @Inject constructor(
    private val apiService: ApiService,
    private val currentAffairDao: CurrentAffairDao
) {
    fun getCurrentAffairs(category: String? = null): Flow<Resource<List<CurrentAffairEntity>>> = flow {
        emit(Resource.Loading())
        
        val cachedFlow = if (category == null) {
            currentAffairDao.getAllCurrentAffairs()
        } else {
            currentAffairDao.getCurrentAffairsByCategory(category)
        }
        
        val cached = cachedFlow.first()
        emit(Resource.Loading(cached))
        
        try {
            val remote = apiService.getCurrentAffairs(category)
            currentAffairDao.insertCurrentAffairs(remote.map { it.toEntity() })
            
            val newCached = cachedFlow.first()
            emit(Resource.Success(newCached))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch current affairs", cached))
        }
    }

    fun getBookmarkedCurrentAffairs(): Flow<List<CurrentAffairEntity>> = 
        currentAffairDao.getBookmarkedCurrentAffairs()

    suspend fun toggleBookmark(id: String, isBookmarked: Boolean) {
        currentAffairDao.updateBookmark(id, isBookmarked)
    }

    suspend fun searchAffairs(query: String): List<CurrentAffairEntity> {
        return if (query.isEmpty()) emptyList() else currentAffairDao.searchCurrentAffairs(query)
    }
}
