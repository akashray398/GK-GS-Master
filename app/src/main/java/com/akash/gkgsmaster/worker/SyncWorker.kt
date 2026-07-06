package com.akash.gkgsmaster.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.akash.gkgsmaster.data.repository.CurrentAffairRepository
import com.akash.gkgsmaster.data.repository.QuestionRepository
import com.akash.gkgsmaster.data.repository.BookletRepository
import com.akash.gkgsmaster.data.repository.NoteRepository
import com.akash.gkgsmaster.data.repository.GKRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val currentAffairRepository: CurrentAffairRepository,
    private val questionRepository: QuestionRepository,
    private val bookletRepository: BookletRepository,
    private val gkRepository: GKRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        println("SyncWorker: Starting sync...")
        return try {
            // 1. Sync Current Affairs
            currentAffairRepository.getCurrentAffairs().collect {}
            
            // 2. Refresh Questions (fetch initial 20)
            questionRepository.getQuestions(amount = 20).collect {}
            
            // 3. Sync Booklets
            bookletRepository.getBooklets().collect {}
            
            // 4. Sync Learning Topics
            gkRepository.getCategories().collect {}
            
            Result.success()
        } catch (_: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
