package com.akash.gkgsmaster.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.akash.gkgsmaster.utils.NotificationHelper

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val type = inputData.getString("type") ?: "DAILY_QUIZ"
        println("ReminderWorker: Triggered for type $type")
        val notificationHelper = NotificationHelper(applicationContext)

        when (type) {
            "DAILY_QUIZ" -> {
                notificationHelper.showNotification(
                    "Time for Daily Quiz!",
                    "Test your knowledge and earn XP/Coins now.",
                    101
                )
            }
            "CURRENT_AFFAIRS" -> {
                notificationHelper.showNotification(
                    "Fresh Current Affairs Available",
                    "Stay updated with today's top headlines.",
                    102
                )
            }
            "STUDY_REMINDER" -> {
                notificationHelper.showNotification(
                    "Goal Reminder",
                    "Consistency is key! Complete your daily learning goal.",
                    103
                )
            }
            "BOOKLET_REMINDER" -> {
                notificationHelper.showNotification(
                    "Continue Reading",
                    "Pick up where you left off in your UPSC booklet.",
                    104
                )
            }
        }
        
        return Result.success()
    }
}
