package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor() {

    fun getDailyQuote(): Flow<String> = flow {
        emit("Success is not final, failure is not fatal: it is the courage to continue that counts.")
    }

    fun getCurrentAffairs(): Flow<List<CurrentAffair>> = flow {
        emit(listOf(
            CurrentAffair("1", "New space mission launched by ISRO", "24 May 2024"),
            CurrentAffair("2", "Global Climate Summit 2024 updates", "23 May 2024")
        ))
    }

    fun getCategories(): Flow<List<HomeCategory>> = flow {
        emit(listOf(
            HomeCategory("1", "Daily Quiz", R.drawable.ic_placeholder, R.color.primary),
            HomeCategory("2", "GK Learning", R.drawable.ic_placeholder, R.color.secondary),
            HomeCategory("3", "GS Learning", R.drawable.ic_placeholder, R.color.accent),
            HomeCategory("4", "Notes", R.drawable.ic_placeholder, R.color.glow),
            HomeCategory("5", "Booklets", R.drawable.ic_placeholder, R.color.primary)
        ))
    }

    fun getUserProgress(): Flow<UserProgress> = flow {
        emit(UserProgress(level = 12, xp = 2450, nextLevelXp = 3000, dailyStreak = 7))
    }

    fun getRecentActivity(): Flow<List<RecentActivity>> = flow {
        emit(listOf(
            RecentActivity("1", "Polity Quiz", "2 hours ago", "8/10"),
            RecentActivity("2", "History Notes", "5 hours ago")
        ))
    }
}
