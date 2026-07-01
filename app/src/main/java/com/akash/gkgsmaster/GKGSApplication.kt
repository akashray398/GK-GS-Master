package com.akash.gkgsmaster

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.akash.gkgsmaster.worker.DailyReminderWorker
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
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            "SyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )

        // Daily Reminder - Once a day
        val reminderRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(12, TimeUnit.HOURS) // Start after 12 hours
            .build()

        workManager.enqueueUniquePeriodicWork(
            "DailyReminderWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            reminderRequest
        )
    }
}
