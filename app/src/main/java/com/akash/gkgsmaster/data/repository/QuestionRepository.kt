package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.data.api.QuizApiService
import com.akash.gkgsmaster.data.api.dto.toEntity
import com.akash.gkgsmaster.data.database.QuestionDao
import com.akash.gkgsmaster.data.model.Question
import com.akash.gkgsmaster.utils.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val quizApiService: QuizApiService,
    private val questionDao: QuestionDao
) {
    fun getQuestions(
        amount: Int = 10,
        category: String? = null,
        difficulty: String? = null,
        language: String = "english"
    ): Flow<Resource<List<Question>>> = flow {
        emit(Resource.Loading())
        
        if (language == "hindi") {
            val hindiQuestions = getHindiSampleQuestions(amount, category)
            emit(Resource.Success(hindiQuestions))
            return@flow
        }
        
        try {
            val categoryInt = category?.toIntOrNull()
            // Map common names to The Trivia API categories
            val triviaCategory = when(category?.lowercase()) {
                "history" -> "history"
                "science" -> "science"
                "geography" -> "geography"
                "politics" -> "society_and_culture"
                "people" -> "society_and_culture"
                "indiahistory" -> "history"
                "indiageo" -> "geography"
                "indiapolity" -> "society_and_culture"
                "indiamyth" -> "history"
                "indiaeco" -> "society_and_culture"
                "modernindia" -> "history"
                else -> null
            }

            // Try to get enough questions by combining sources if needed
            val allFetchedQuestions = mutableListOf<Question>()
            
            // Try "The Trivia API"
            try {
                val theTriviaResponse = quizApiService.getTheTriviaQuestions(
                    limit = amount,
                    categories = triviaCategory,
                    difficulties = difficulty?.lowercase(),
                    tags = if (category?.lowercase()?.contains("india") == true) "india" else null
                )
                allFetchedQuestions.addAll(theTriviaResponse.map { it.toEntity() })
            } catch (e: Exception) {
                println("Repository: The Trivia API failed: ${e.message}")
            }

            // If we don't have enough, try OpenTDB
            if (allFetchedQuestions.size < amount) {
                try {
                    val fetchCategory = categoryInt ?: 9
                    val remainingAmount = amount - allFetchedQuestions.size
                    val openTdbResponse = quizApiService.getQuestions(
                        amount = remainingAmount, 
                        category = fetchCategory, 
                        difficulty = difficulty?.lowercase()
                    )
                    allFetchedQuestions.addAll(openTdbResponse.results.map { it.toEntity() })
                } catch (e: Exception) {
                    println("Repository: OpenTDB failed: ${e.message}")
                }
            }
            
            if (allFetchedQuestions.isNotEmpty()) {
                val finalQuestions = allFetchedQuestions.distinctBy { it.text }.take(amount)
                questionDao.insertQuestions(finalQuestions)
                emit(Resource.Success(finalQuestions))
            } else {
                throw Exception("No questions found in any online source.")
            }
        } catch (e: Exception) {
            val cached = questionDao.getAllQuestions().firstOrNull() ?: emptyList()
            // Filter cache by category if provided
            val filteredCache = if (category != null) {
                cached.filter { it.category.contains(category, ignoreCase = true) }
            } else cached

            if (filteredCache.isNotEmpty()) {
                emit(Resource.Success(filteredCache.shuffled().take(amount)))
            } else if (cached.isNotEmpty()) {
                // Fallback to any cached questions if filtered ones aren't available
                emit(Resource.Success(cached.shuffled().take(amount)))
            } else {
                emit(Resource.Error("Offline: No questions in database. ${e.message}", emptyList()))
            }
        }
    }

    private fun getHindiSampleQuestions(amount: Int, category: String?): List<Question> {
        val questions = listOf(
            Question("h1", "भारत के प्रथम प्रधानमंत्री कौन थे?", listOf("जवाहरलाल नेहरू", "महात्मा गांधी", "सरदार पटेल", "राजेंद्र प्रसाद"), 0, "history", "medium"),
            Question("h2", "भारत का राष्ट्रीय फूल क्या है?", listOf("गुलाब", "कमल", "सूरजमुखी", "गेंदा"), 1, "gk", "easy"),
            Question("h3", "ताजमहल कहाँ स्थित है?", listOf("दिल्ली", "आगरा", "मुंबई", "जयपुर"), 1, "history", "easy"),
            Question("h4", "स्वतंत्रता दिवस कब मनाया जाता है?", listOf("26 जनवरी", "15 अगस्त", "2 अक्टूबर", "14 नवंबर"), 1, "history", "easy"),
            Question("h5", "भारत की राजधानी क्या है?", listOf("मुंबई", "कोलकाता", "नई दिल्ली", "चेन्नई"), 2, "gk", "easy"),
            Question("h6", "महात्मा गांधी को और किस नाम से जाना जाता है?", listOf("बापू", "चाचा", "नेताजी", "लौह पुरुष"), 0, "history", "easy"),
            Question("h7", "भारत का सबसे बड़ा राज्य क्षेत्रफल के हिसाब से कौन सा है?", listOf("उत्तर प्रदेश", "महाराष्ट्र", "राजस्थान", "मध्य प्रदेश"), 2, "geography", "medium"),
            Question("h8", "गंगा नदी कहाँ से निकलती है?", listOf("यमुनोत्री", "गंगोत्री", "अमरकंटक", "मानसरोवर"), 1, "geography", "medium"),
            Question("h9", "संविधान दिवस कब मनाया जाता है?", listOf("15 अगस्त", "26 जनवरी", "26 नवंबर", "2 अक्टूबर"), 2, "politics", "medium"),
            Question("h10", "भारत का राष्ट्रीय खेल कौन सा है?", listOf("क्रिकेट", "फुटबॉल", "हॉकी", "कबड्डी"), 2, "gk", "easy")
        )
        return questions.shuffled().take(amount)
    }

    suspend fun searchQuestions(query: String): List<Question> {
        return if (query.isEmpty()) emptyList() else questionDao.searchQuestions(query)
    }

    suspend fun insertQuestion(question: Question) {
        questionDao.insertQuestions(listOf(question))
        println("Repository: Inserted question ${question.id}")
    }

    fun getTodayQuiz(): Flow<Question?> = flow {
        val questions = questionDao.getAllQuestions().first()
        emit(questions.randomOrNull())
    }
}
