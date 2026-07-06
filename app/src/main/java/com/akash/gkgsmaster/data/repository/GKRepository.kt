package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.api.GKApiService
import com.akash.gkgsmaster.data.database.LearningTopicDao
import com.akash.gkgsmaster.data.model.GKCategory
import com.akash.gkgsmaster.data.model.GKArticle
import com.akash.gkgsmaster.data.model.LearningTopicEntity
import com.akash.gkgsmaster.data.api.dto.CountryDto
import com.akash.gkgsmaster.data.api.dto.DictionaryEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GKRepository @Inject constructor(
    private val gkApiService: GKApiService,
    private val learningTopicDao: LearningTopicDao
) {

    fun getCountries(): Flow<List<CountryDto>> = flow {
        try {
            val countries = gkApiService.getAllCountries()
            emit(countries)
        } catch (_: Exception) {
            emit(emptyList())
        }
    }

    suspend fun getWikipediaExtract(title: String): String {
        return try {
            val response = gkApiService.getWikipediaExtract(titles = title)
            val pages = response.query?.pages
            val extract = pages?.values?.firstOrNull()?.extract
            extract ?: "No content found for $title"
        } catch (e: Exception) {
            "Error loading content: ${e.message}"
        }
    }

    fun getArticles(categoryId: String): Flow<List<GKArticle>> = flow {
        // Map category ID to Name
        val categoryName = when(categoryId) {
            "1" -> "World GK"
            "2" -> "India GK"
            "3" -> "States & Capitals"
            "6" -> "History"
            "8" -> "Polity"
            "9" -> "Constitution"
            "10" -> "Economics"
            "11" -> "Environment"
            "13" -> "Physics"
            "14" -> "Chemistry"
            "15" -> "Biology"
            "16" -> "Computer"
            "17" -> "Science & Technology"
            "18" -> "Space"
            "19" -> "Government Schemes"
            "20" -> "Awards"
            "21" -> "Books & Authors"
            "22" -> "Important Days"
            "23" -> "Rivers"
            "24" -> "Mountains"
            "25" -> "National Parks"
            "26" -> "UNESCO Sites"
            "27" -> "International Organizations"
            "28" -> "Indian Freedom Movement"
            "29" -> "Ancient India"
            "30" -> "Medieval India"
            "31" -> "Modern India"
            else -> "General"
        }

        learningTopicDao.getTopicsByCategory(categoryName).collect { topics ->
            if (topics.isNotEmpty()) {
                emit(topics.map { GKArticle(it.id, it.title, it.description, it.category) })
            } else {
                // Fetch from Wikipedia for other categories
                val extract = getWikipediaExtract(categoryName)
                emit(listOf(GKArticle(categoryId, categoryName, extract, "GK")))
            }
        }
    }

    fun getCategories(): Flow<List<GKCategory>> = flow {
        emit(
            listOf(
                GKCategory("1", "World GK", R.drawable.ic_placeholder, R.color.primary),
                GKCategory("2", "India GK", R.drawable.ic_placeholder, R.color.secondary),
                GKCategory("3", "States & Capitals", R.drawable.ic_placeholder, R.color.accent),
                GKCategory("4", "Indian Geography", R.drawable.ic_placeholder, R.color.glow),
                GKCategory("5", "World Geography", R.drawable.ic_placeholder, R.color.primary),
                GKCategory("6", "Indian History", R.drawable.ic_placeholder, R.color.secondary),
                GKCategory("7", "World History", R.drawable.ic_placeholder, R.color.accent),
                GKCategory("8", "Indian Polity", R.drawable.ic_placeholder, R.color.glow),
                GKCategory("9", "Constitution", R.drawable.ic_placeholder, R.color.primary),
                GKCategory("10", "Economics", R.drawable.ic_placeholder, R.color.secondary),
                GKCategory("11", "Environment", R.drawable.ic_placeholder, R.color.accent),
                GKCategory("12", "Ecology", R.drawable.ic_placeholder, R.color.glow),
                GKCategory("13", "Physics", R.drawable.ic_placeholder, R.color.primary),
                GKCategory("14", "Chemistry", R.drawable.ic_placeholder, R.color.secondary),
                GKCategory("15", "Biology", R.drawable.ic_placeholder, R.color.accent),
                GKCategory("16", "Computer", R.drawable.ic_placeholder, R.color.glow),
                GKCategory("17", "Science & Technology", R.drawable.ic_placeholder, R.color.primary),
                GKCategory("18", "Space", R.drawable.ic_placeholder, R.color.secondary),
                GKCategory("19", "Government Schemes", R.drawable.ic_placeholder, R.color.accent),
                GKCategory("20", "Awards", R.drawable.ic_placeholder, R.color.glow),
                GKCategory("21", "Books & Authors", R.drawable.ic_placeholder, R.color.primary),
                GKCategory("22", "Important Days", R.drawable.ic_placeholder, R.color.secondary),
                GKCategory("23", "Rivers", R.drawable.ic_placeholder, R.color.accent),
                GKCategory("24", "Mountains", R.drawable.ic_placeholder, R.color.glow),
                GKCategory("25", "National Parks", R.drawable.ic_placeholder, R.color.primary),
                GKCategory("26", "UNESCO Sites", R.drawable.ic_placeholder, R.color.secondary),
                GKCategory("27", "International Organizations", R.drawable.ic_placeholder, R.color.accent),
                GKCategory("28", "Indian Freedom Movement", R.drawable.ic_placeholder, R.color.glow),
                GKCategory("29", "Ancient India", R.drawable.ic_placeholder, R.color.primary),
                GKCategory("30", "Medieval India", R.drawable.ic_placeholder, R.color.secondary),
                GKCategory("31", "Modern India", R.drawable.ic_placeholder, R.color.accent)
            )
        )
    }

    fun searchArticles(query: String): List<GKArticle> {
        println("Searching for: $query")
        return emptyList()
    }

    fun getContinueLearning(): Flow<LearningTopicEntity?> = flow {
        learningTopicDao.getTopicsByCategory("General").collect { list ->
            emit(list.maxByOrNull { it.lastReadTimestamp })
        }
    }

    fun getRecentlyRead(): Flow<List<LearningTopicEntity>> = 
        learningTopicDao.getRecentlyRead(limit = 10)

    fun getBookmarkedTopics(): Flow<List<LearningTopicEntity>> = 
        learningTopicDao.getBookmarkedTopics()

    suspend fun getTopicById(id: String): LearningTopicEntity? {
        val topic = learningTopicDao.getTopicById(id)
        if (topic != null) {
            learningTopicDao.insertTopics(listOf(topic.copy(lastReadTimestamp = System.currentTimeMillis())))
            println("Marked topic as read: ${topic.title}")
        }
        return topic
    }

    suspend fun toggleTopicBookmark(id: String, isBookmarked: Boolean) {
        learningTopicDao.updateBookmark(id, isBookmarked)
        println("Topic $id bookmark status: $isBookmarked")
    }

    suspend fun searchTopics(query: String): List<LearningTopicEntity> {
        val results = learningTopicDao.searchTopics(query)
        println("Found ${results.size} topics for query: $query")
        return results
    }

    suspend fun getWordInfo(word: String): DictionaryEntry? {
        return try {
            val response = gkApiService.getWordInfo("https://api.dictionaryapi.dev/api/v2/entries/en/$word")
            response.firstOrNull()
        } catch (_: Exception) {
            null
        }
    }
}
