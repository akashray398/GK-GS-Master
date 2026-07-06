package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.PreferenceManager
import com.akash.gkgsmaster.data.database.RecentActivityDao
import com.akash.gkgsmaster.data.model.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val recentActivityDao: RecentActivityDao,
    private val preferenceManager: PreferenceManager
) {

    fun getDailyQuote(): Flow<String> = flow {
        val quotes = listOf(
            "Success is not final, failure is not fatal: it is the courage to continue that counts.",
            "The only way to do great work is to love what you do.",
            "Believe you can and you're halfway there.",
            "Education is the most powerful weapon which you can use to change the world."
        )
        emit(quotes.random())
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

    fun getUserProgress(): Flow<UserProgress> {
        return combine(
            preferenceManager.totalXp,
            preferenceManager.streakCount
        ) { xp, streak ->
            val level = (xp / 1000) + 1
            val nextLevelXp = level * 1000
            UserProgress(level = level, xp = xp, nextLevelXp = nextLevelXp, dailyStreak = streak)
        }
    }

    fun getRecentActivity(): Flow<List<RecentActivity>> {
        return recentActivityDao.getRecentActivities().map { list ->
            list.map { entity ->
                RecentActivity(
                    id = entity.id.toString(),
                    title = entity.title,
                    timestamp = "Just now",
                    score = if (entity.type == "QUIZ") entity.description else null
                )
            }
        }
    }

    suspend fun addActivity(title: String, description: String, type: String) {
        println("Adding activity: $title ($type)")
        recentActivityDao.insertActivity(
            RecentActivityEntity(title = title, description = description, type = type)
        )
    }
}
