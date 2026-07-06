package com.akash.gkgsmaster.ui.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.akash.gkgsmaster.data.database.QuizHistoryDao
import com.akash.gkgsmaster.data.model.AnalyticsData
import com.akash.gkgsmaster.data.model.ProgressEntry
import com.akash.gkgsmaster.data.model.StudyTimeEntry
import com.akash.gkgsmaster.data.model.TopicPerformance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val quizHistoryDao: QuizHistoryDao
) : ViewModel() {

    val analyticsData: LiveData<AnalyticsData> = quizHistoryDao.getAllQuizHistory().map { history ->
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        val weeklyProgress = history.take(7).map { 
            ProgressEntry(sdf.format(Date(it.timestamp)), it.score.toFloat())
        }.reversed()

        val monthlySdf = SimpleDateFormat("MMM", Locale.getDefault())
        val monthlyProgress = history.groupBy { monthlySdf.format(Date(it.timestamp)) }
            .map { (month, list) -> ProgressEntry(month, list.sumOf { it.score }.toFloat()) }

        val topicPerformance = history.groupBy { it.category }
            .map { (category, list) -> 
                TopicPerformance(category, list.map { it.accuracy }.average().toFloat()) 
            }

        AnalyticsData(
            weeklyProgress = weeklyProgress.ifEmpty { listOf(ProgressEntry("None", 0f)) },
            monthlyProgress = monthlyProgress.ifEmpty { listOf(ProgressEntry("None", 0f)) },
            quizAccuracy = if (history.isNotEmpty()) history.map { it.accuracy }.average().toFloat() else 0f,
            topicPerformance = topicPerformance.ifEmpty { listOf(TopicPerformance("None", 0f)) },
            studyTime = history.take(7).map { 
                StudyTimeEntry(sdf.format(Date(it.timestamp)), it.timeTakenSeconds / 60) 
            }.reversed(),
            completedLessons = history.size,
            totalLessons = 500 // Target
        )
    }.asLiveData()
}
