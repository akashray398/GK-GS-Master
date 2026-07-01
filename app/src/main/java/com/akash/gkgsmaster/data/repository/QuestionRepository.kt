package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.data.api.ApiService
import com.akash.gkgsmaster.data.api.dto.toEntity
import com.akash.gkgsmaster.data.database.QuestionDao
import com.akash.gkgsmaster.data.model.Question
import com.akash.gkgsmaster.utils.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val apiService: ApiService,
    private val questionDao: QuestionDao
) {
    fun getQuestions(category: String? = null, difficulty: String? = null): Flow<Resource<List<Question>>> = flow {
        emit(Resource.Loading())
        
        val cached = if (category != null) {
            questionDao.getQuestionsByCategory(category).first()
        } else {
            questionDao.getAllQuestions().first()
        }
        
        emit(Resource.Loading(cached))
        
        try {
            val remote = apiService.getQuestions(category, difficulty)
            questionDao.insertQuestions(remote.map { it.toEntity() })
            
            val newCached = if (category != null) {
                questionDao.getQuestionsByCategory(category).first()
            } else {
                questionDao.getAllQuestions().first()
            }
            emit(Resource.Success(newCached))
            
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred", cached))
        }
    }

    suspend fun refreshQuestions(category: String? = null) {
        try {
            val remoteQuestions = apiService.getQuestions(category)
            questionDao.insertQuestions(remoteQuestions.map { it.toEntity() })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun searchQuestions(query: String): List<Question> {
        return if (query.isEmpty()) emptyList() else questionDao.searchQuestions(query)
    }
}
