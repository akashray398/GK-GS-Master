package com.akash.gkgsmaster.data.model

data class UserProfile(
    val name: String,
    val email: String,
    val profileImage: String? = null,
    val totalQuizScore: Int,
    val xp: Int,
    val coins: Int = 0,
    val bookmarksCount: Int,
    val notesCount: Int,
    val bookletsRead: Int = 0,
    val studyHours: Float,
    val dailyStreak: Int,
    val longestStreak: Int = 0,
    val achievements: List<Achievement>
)

data class Achievement(
    val id: String,
    val title: String,
    val iconRes: Int,
    val isUnlocked: Boolean = false
)
