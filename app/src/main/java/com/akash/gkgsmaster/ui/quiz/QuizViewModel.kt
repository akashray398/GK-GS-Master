package com.akash.gkgsmaster.ui.quiz

import android.os.CountDownTimer
import androidx.lifecycle.*
import com.akash.gkgsmaster.data.model.Question
import com.akash.gkgsmaster.data.model.QuizQuestion
import com.akash.gkgsmaster.data.model.QuizResult
import com.akash.gkgsmaster.data.repository.QuestionRepository
import com.akash.gkgsmaster.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuestionRepository
) : ViewModel() {

    private val _questionsState = MutableLiveData<Resource<List<Question>>>()
    val questionsState: LiveData<Resource<List<Question>>> = _questionsState

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    private val _isQuizFinished = MutableLiveData<QuizResult?>()
    val isQuizFinished: LiveData<QuizResult?> = _isQuizFinished

    private var timer: CountDownTimer? = null
    private var totalTimeSeconds = 0

    fun startQuiz(category: String, difficulty: String) {
        viewModelScope.launch {
            repository.getQuestions(category, difficulty).collect { resource ->
                _questionsState.value = resource
                if (resource is Resource.Success || (resource is Resource.Loading && !resource.data.isNullOrEmpty())) {
                    if (timer == null) startTimer()
                }
            }
        }
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                totalTimeSeconds++
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                _timerText.value = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    fun submitAnswer(selectedIndex: Int) {
        val currentQuestions = _questionsState.value?.data
        val currentQuestion = currentQuestions?.get(_currentQuestionIndex.value ?: 0)
        if (selectedIndex == currentQuestion?.correctOptionIndex) {
            _score.value = (_score.value ?: 0) + 1
        }
        
        moveToNextQuestion()
    }

    fun moveToNextQuestion() {
        val questionsSize = _questionsState.value?.data?.size ?: 0
        val nextIndex = (_currentQuestionIndex.value ?: 0) + 1
        if (nextIndex < questionsSize) {
            _currentQuestionIndex.value = nextIndex
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        timer?.cancel()
        val questions = _questionsState.value?.data
        val result = QuizResult(
            totalQuestions = questions?.size ?: 0,
            correctAnswers = _score.value ?: 0,
            timeTakenSeconds = totalTimeSeconds,
            category = questions?.firstOrNull()?.category ?: "General"
        )
        _isQuizFinished.value = result
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
