package com.akash.gkgsmaster.ui.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akash.gkgsmaster.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor() : ViewModel() {

    private val _analyticsData = MutableLiveData<AnalyticsData>()
    val analyticsData: LiveData<AnalyticsData> = _analyticsData

    init {
        loadMockData()
    }

    private fun loadMockData() {
        _analyticsData.value = AnalyticsData(
            weeklyProgress = listOf(
                ProgressEntry("Mon", 45f),
                ProgressEntry("Tue", 60f),
                ProgressEntry("Wed", 30f),
                ProgressEntry("Thu", 80f),
                ProgressEntry("Fri", 50f),
                ProgressEntry("Sat", 90f),
                ProgressEntry("Sun", 70f)
            ),
            monthlyProgress = listOf(
                ProgressEntry("Week 1", 300f),
                ProgressEntry("Week 2", 450f),
                ProgressEntry("Week 3", 380f),
                ProgressEntry("Week 4", 520f)
            ),
            quizAccuracy = 78.5f,
            topicPerformance = listOf(
                TopicPerformance("Polity", 85f),
                TopicPerformance("History", 70f),
                TopicPerformance("Geography", 75f),
                TopicPerformance("Science", 82f),
                TopicPerformance("Economy", 65f)
            ),
            studyTime = listOf(
                StudyTimeEntry("Mon", 120),
                StudyTimeEntry("Tue", 150),
                StudyTimeEntry("Wed", 90),
                StudyTimeEntry("Thu", 180),
                StudyTimeEntry("Fri", 130),
                StudyTimeEntry("Sat", 200),
                StudyTimeEntry("Sun", 160)
            ),
            completedLessons = 124,
            totalLessons = 250
        )
    }
}
