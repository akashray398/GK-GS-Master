package com.akash.gkgsmaster.data.model

data class AnalyticsData(
    val weeklyProgress: List<ProgressEntry>,
    val monthlyProgress: List<ProgressEntry>,
    val quizAccuracy: Float, // Percentage
    val topicPerformance: List<TopicPerformance>,
    val studyTime: List<StudyTimeEntry>,
    val completedLessons: Int,
    val totalLessons: Int
)

data class ProgressEntry(
    val label: String,
    val value: Float
)

data class TopicPerformance(
    val topicName: String,
    val accuracy: Float
)

data class StudyTimeEntry(
    val day: String,
    val minutes: Int
)
