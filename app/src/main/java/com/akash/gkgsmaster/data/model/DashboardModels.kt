package com.akash.gkgsmaster.data.model

data class CurrentAffair(
    val id: String,
    val title: String,
    val date: String,
    val imageUrl: String? = null
)

data class HomeCategory(
    val id: String,
    val title: String,
    val iconRes: Int,
    val colorRes: Int
)

data class RecentActivity(
    val id: String,
    val title: String,
    val timestamp: String,
    val score: String? = null
)

data class UserProgress(
    val level: Int,
    val xp: Int,
    val nextLevelXp: Int,
    val dailyStreak: Int
)
