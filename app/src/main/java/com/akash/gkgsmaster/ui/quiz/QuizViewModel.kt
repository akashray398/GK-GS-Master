package com.akash.gkgsmaster.ui.quiz

import android.os.CountDownTimer
import androidx.lifecycle.*
import com.akash.gkgsmaster.data.PreferenceManager
import com.akash.gkgsmaster.data.database.QuizHistoryDao
import com.akash.gkgsmaster.data.model.Question
import com.akash.gkgsmaster.data.model.QuizResult
import com.akash.gkgsmaster.data.repository.HomeRepository
import com.akash.gkgsmaster.data.repository.QuestionRepository
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuestionRepository,
    private val homeRepository: HomeRepository,
    private val quizHistoryDao: QuizHistoryDao,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _questionsState = MutableLiveData<Resource<List<Question>>>()
    val questionsState: LiveData<Resource<List<Question>>> = _questionsState

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    private val _timerProgress = MutableLiveData<Int>()
    val timerProgress: LiveData<Int> = _timerProgress

    private val _isQuizFinished = MutableLiveData<QuizResult?>()
    val isQuizFinished: LiveData<QuizResult?> = _isQuizFinished

    private val userAnswers = mutableMapOf<Int, Int>() 
    private var timer: CountDownTimer? = null
    private var totalTimeSeconds = 0
    private var timeLimitPerQuestion = 30 
    private var hasNegativeMarking = false
    private var isDailyChallenge = false

    fun startQuiz(amount: Int = 10, category: String? = null, difficulty: String? = null, timerOption: Int = 30, negativeMarking: Boolean = false, dailyChallenge: Boolean = false, language: String = "english") {
        // Cancel any existing timer immediately
        timer?.cancel()
        timer = null
        
        // Reset state for new quiz
        timeLimitPerQuestion = if (timerOption > 0) timerOption else 30
        hasNegativeMarking = negativeMarking
        this.isDailyChallenge = dailyChallenge
        userAnswers.clear()
        _score.value = 0
        _currentQuestionIndex.value = 0
        totalTimeSeconds = 0
        _isQuizFinished.value = null
        _questionsState.value = Resource.Loading()
        
        viewModelScope.launch {
            repository.getQuestions(amount, category, difficulty, language).collect { resource ->
                _questionsState.postValue(resource)
                if (resource is Resource.Success && !resource.data.isNullOrEmpty()) {
                    _currentQuestionIndex.postValue(0)
                    startQuestionTimer()
                }
            }
        }
    }

    private fun startQuestionTimer() {
        timer?.cancel()
        val duration = timeLimitPerQuestion * 1000L
        if (duration <= 0) return

        timer = object : CountDownTimer(duration, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000)
                val decaseconds = (millisUntilFinished % 1000) / 100
                _timerText.postValue(String.format("%02d.%d", secondsLeft, decaseconds))
                _timerProgress.postValue(((millisUntilFinished.toFloat() / duration) * 100).toInt())
                
                if (millisUntilFinished % 1000 < 100) {
                    totalTimeSeconds++
                }
            }

            override fun onFinish() {
                _timerProgress.postValue(0)
                submitAnswer(-1) // Auto-submit with no selection
                moveToNextQuestion()
            }
        }.start()
    }

    fun submitAnswer(selectedIndex: Int) {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val currentQuestions = _questionsState.value?.data
        if (currentQuestions.isNullOrEmpty() || currentIndex >= currentQuestions.size) return

        userAnswers[currentIndex] = selectedIndex
        val currentQuestion = currentQuestions[currentIndex]
        
        if (selectedIndex == currentQuestion.correctOptionIndex) {
            _score.value = (_score.value ?: 0) + 1
        }
    }

    fun moveToNextQuestion() {
        val questions = _questionsState.value?.data ?: emptyList()
        val questionsSize = questions.size
        if (questionsSize == 0) return // Don't finish if no questions loaded yet

        val nextIndex = (_currentQuestionIndex.value ?: 0) + 1
        if (nextIndex < questionsSize) {
            _currentQuestionIndex.value = nextIndex
            startQuestionTimer()
        } else {
            finishQuiz()
        }
    }

    fun insertQuestion(question: Question) {
        viewModelScope.launch {
            repository.insertQuestion(question)
        }
    }

    private fun finishQuiz() {
        timer?.cancel()
        val questions = _questionsState.value?.data ?: emptyList()
        val correctCount = _score.value ?: 0
        val total = questions.size
        
        var xpEarned = correctCount * 10
        var coinsEarned = correctCount * 2

        if (isDailyChallenge && correctCount >= 7) {
            xpEarned += 50
            coinsEarned += 10
        }

        val answersList = List(total) { i -> userAnswers[i] ?: -1 }

        val result = QuizResult(
            totalQuestions = total,
            correctAnswers = correctCount,
            timeTakenSeconds = totalTimeSeconds,
            category = questions.firstOrNull()?.category ?: "General",
            questions = questions,
            userAnswers = answersList,
            xpEarned = xpEarned,
            coinsEarned = coinsEarned
        )
        
        viewModelScope.launch {
            quizHistoryDao.insertQuizResult(
                com.akash.gkgsmaster.data.model.QuizHistory(
                    category = result.category,
                    difficulty = if (isDailyChallenge) "Daily Challenge" else "Practice", 
                    score = correctCount,
                    totalQuestions = total,
                    timeTakenSeconds = totalTimeSeconds,
                    accuracy = if (total > 0) (correctCount.toFloat() / total) * 100 else 0f,
                    xpEarned = xpEarned,
                    coinsEarned = coinsEarned
                )
            )

            preferenceManager.addXp(xpEarned)
            preferenceManager.addCoins(coinsEarned)

            homeRepository.addActivity(
                title = if (isDailyChallenge) "Daily Challenge" else "Quiz: ${result.category}",
                description = "Scored $correctCount/$total (+${xpEarned} XP)",
                type = "QUIZ"
            )
        }

        _isQuizFinished.value = result
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
