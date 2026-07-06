package com.akash.gkgsmaster

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.akash.gkgsmaster.worker.ReminderWorker
import com.akash.gkgsmaster.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class GKGSApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupWorkers()
    }

    private fun setupWorkers() {
        val workManager = WorkManager.getInstance(this)

        // Sync Worker - Every 6 hours when internet is available
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(6, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            "SyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )

        // Daily Reminders
        scheduleReminder(workManager, "DAILY_QUIZ", 10, 0) // 10 AM
        scheduleReminder(workManager, "CURRENT_AFFAIRS", 14, 0) // 2 PM
        scheduleReminder(workManager, "BOOKLET_REMINDER", 17, 0) // 5 PM
        scheduleReminder(workManager, "STUDY_REMINDER", 19, 0) // 7 PM
    }

    private fun scheduleReminder(workManager: WorkManager, type: String, hour: Int, minute: Int) {
        val now = java.util.Calendar.getInstance()
        val target = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
            if (before(now)) add(java.util.Calendar.DAY_OF_YEAR, 1)
        }

        val delay = target.timeInMillis - now.timeInMillis

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("type" to type))
            .build()

        workManager.enqueueUniquePeriodicWork(
            "Reminder_$type",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }
}
