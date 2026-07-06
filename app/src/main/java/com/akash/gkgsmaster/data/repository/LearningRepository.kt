package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.data.api.GKApiService
import com.akash.gkgsmaster.data.api.dto.toEntity
import com.akash.gkgsmaster.data.database.LearningTopicDao
import com.akash.gkgsmaster.data.model.LearningTopicEntity
import com.akash.gkgsmaster.utils.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningRepository @Inject constructor(
    private val gkApiService: GKApiService,
    private val learningTopicDao: LearningTopicDao
) {
    fun getTopicsByCategory(category: String): Flow<Resource<List<LearningTopicEntity>>> = flow {
        emit(Resource.Loading())
        
        val cached = learningTopicDao.getTopicsByCategory(category).first()
        emit(Resource.Loading(cached))
        
        try {
            // Wikipedia integration could go here
            // For now, we rely on local content or specific API calls
            // emit(Resource.Success(cached))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error fetching topics", cached))
        }
    }

    suspend fun toggleBookmark(id: String, isBookmarked: Boolean) {
        learningTopicDao.updateBookmark(id, isBookmarked)
    }

    suspend fun searchTopics(query: String): List<LearningTopicEntity> {
        return learningTopicDao.searchTopics(query)
    }
}
