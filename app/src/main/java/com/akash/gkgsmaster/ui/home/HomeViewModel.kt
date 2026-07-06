package com.akash.gkgsmaster.ui.home

import androidx.lifecycle.*
import com.akash.gkgsmaster.data.PreferenceManager
import com.akash.gkgsmaster.data.model.*
import com.akash.gkgsmaster.data.repository.CurrentAffairRepository
import com.akash.gkgsmaster.data.database.QuizHistoryDao
import com.akash.gkgsmaster.data.repository.*
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val gkRepository: GKRepository,
    private val currentAffairRepository: CurrentAffairRepository,
    private val noteRepository: NoteRepository,
    private val questionRepository: QuestionRepository,
    private val bookletRepository: BookletRepository,
    private val quizHistoryDao: QuizHistoryDao,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val userName: LiveData<String> = preferenceManager.userName
        .map { it ?: "Aspirant" }
        .asLiveData()

    val dailyQuote: LiveData<String> = homeRepository.getDailyQuote().asLiveData()
    
    val currentAffairs: LiveData<Resource<List<CurrentAffairEntity>>> = 
        currentAffairRepository.getCurrentAffairs().asLiveData()

    val categories: LiveData<List<HomeCategory>> = homeRepository.getCategories().asLiveData()
    val userProgress: LiveData<UserProgress> = homeRepository.getUserProgress().asLiveData()
    val recentActivity: LiveData<List<RecentActivity>> = homeRepository.getRecentActivity().asLiveData()

    val recentNotes: LiveData<List<NoteEntity>> = noteRepository.getAllNotes()
        .map { it.take(3) }
        .asLiveData()

    private val _wordOfTheDay = MutableLiveData<com.akash.gkgsmaster.data.api.dto.DictionaryEntry>()
    val wordOfTheDay: LiveData<com.akash.gkgsmaster.data.api.dto.DictionaryEntry> = _wordOfTheDay

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val continueLearning: LiveData<LearningTopicEntity?> = gkRepository.getContinueLearning().asLiveData()
    val todayQuiz: LiveData<Question?> = questionRepository.getTodayQuiz().asLiveData()
    val todayBooklet: LiveData<Booklet?> = bookletRepository.getTodayBooklet().asLiveData()
    
    val recentlyRead: LiveData<List<LearningTopicEntity>> = gkRepository.getRecentlyRead().asLiveData()
    val bookmarkedTopics: LiveData<List<LearningTopicEntity>> = gkRepository.getBookmarkedTopics().asLiveData()
    
    val weeklyProgress: LiveData<List<QuizHistory>> = quizHistoryDao.getAllQuizHistory()
        .map { it.filter { h -> h.timestamp > (System.currentTimeMillis() - 7 * 86400000L) } }
        .asLiveData()

    init {
        refreshDashboard()
        loadWordOfTheDay()
    }

    private fun loadWordOfTheDay() {
        viewModelScope.launch {
            val words = listOf("Resilience", "Empower", "Integrity", "Innovation", "Ambition")
            val word = words.random()
            gkRepository.getWordInfo(word)?.let {
                _wordOfTheDay.value = it
            }
        }
    }

    fun refreshDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            println("Dashboard Refreshing...")
            try {
                // Trigger background sync if needed
                // Or just rely on the Flow objects to emit latest data
            } catch (_: Exception) {}
            _isLoading.value = false
        }
    }
}
