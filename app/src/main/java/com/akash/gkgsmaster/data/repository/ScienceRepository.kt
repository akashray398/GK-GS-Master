package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.api.GKApiService
import com.akash.gkgsmaster.data.model.ScienceCategory
import com.akash.gkgsmaster.data.model.ScienceTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScienceRepository @Inject constructor(
    private val gkApiService: GKApiService
) {

    fun getCategories(): Flow<List<ScienceCategory>> = flow {
        emit(listOf(
            ScienceCategory("13", "Physics", R.drawable.ic_placeholder, R.color.primary),
            ScienceCategory("14", "Chemistry", R.drawable.ic_placeholder, R.color.secondary),
            ScienceCategory("15", "Biology", R.drawable.ic_placeholder, R.color.accent),
            ScienceCategory("4", "Human Body", R.drawable.ic_placeholder, R.color.glow),
            ScienceCategory("11", "Environment", R.drawable.ic_placeholder, R.color.primary),
            ScienceCategory("18", "Space", R.drawable.ic_placeholder, R.color.secondary),
            ScienceCategory("7", "Scientific Names", R.drawable.ic_placeholder, R.color.accent),
            ScienceCategory("8", "Inventions", R.drawable.ic_placeholder, R.color.glow)
        ))
    }

    fun getTopics(categoryId: String): Flow<List<ScienceTopic>> = flow {
        val topicTitle = when(categoryId) {
            "1" -> "Physics"
            "2" -> "Chemistry"
            "3" -> "Biology"
            "4" -> "Human body"
            "5" -> "Environmental science"
            "6" -> "Outer space"
            "7" -> "Binomial nomenclature"
            "8" -> "Invention"
            else -> "Science"
        }
        
        try {
            val response = gkApiService.getWikipediaExtract(titles = topicTitle)
            val extract = response.query?.pages?.values?.firstOrNull()?.extract ?: "No content"
            emit(listOf(
                ScienceTopic(
                    id = categoryId,
                    title = topicTitle,
                    content = extract,
                    category = topicTitle
                )
            ))
        } catch (_: Exception) {
            emit(emptyList())
        }
    }

    suspend fun searchTopics(query: String): List<ScienceTopic> {
        println("Searching science: $query")
        return emptyList()
    }
}
