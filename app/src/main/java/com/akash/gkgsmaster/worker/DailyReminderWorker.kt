package com.akash.gkgsmaster.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.akash.gkgsmaster.utils.NotificationHelper
import kotlin.random.Random

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        println("DailyReminderWorker: Triggered")
        val notificationHelper = NotificationHelper(applicationContext)
        
        val reminders = listOf(
            "Ready for today's quiz? Challenge yourself now!" to "Today's Quiz",
            "Check out the latest current affairs updates." to "Current Affairs",
            "Time to study! Your goal is waiting." to "Study Reminder",
            "Keep your streak alive! Open the app now." to "Streak Reminder"
        )
        
        val randomReminder = reminders[Random.nextInt(reminders.size)]
        notificationHelper.showNotification(
            randomReminder.second,
            randomReminder.first,
            Random.nextInt()
        )
        
        return Result.success()
    }
}
