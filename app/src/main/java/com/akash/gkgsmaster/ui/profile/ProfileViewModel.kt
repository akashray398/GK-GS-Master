package com.akash.gkgsmaster.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.PreferenceManager
import com.akash.gkgsmaster.data.database.BookmarkDao
import com.akash.gkgsmaster.data.database.BookletDao
import com.akash.gkgsmaster.data.database.NoteDao
import com.akash.gkgsmaster.data.database.QuizHistoryDao
import com.akash.gkgsmaster.data.model.Achievement
import com.akash.gkgsmaster.data.model.UserProfile
import com.akash.gkgsmaster.data.model.Booklet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    bookmarkDao: BookmarkDao,
    bookletDao: BookletDao,
    noteDao: NoteDao,
    quizHistoryDao: QuizHistoryDao
) : ViewModel() {

    val userProfile: LiveData<UserProfile> = combine(
        preferenceManager.userName,
        preferenceManager.userEmail,
        preferenceManager.totalXp,
        preferenceManager.streakCount,
        preferenceManager.longestStreak,
        preferenceManager.coins,
        bookmarkDao.getAllBookmarks(),
        noteDao.getAllNotes(),
        bookletDao.getAllBooklets(),
        quizHistoryDao.getAllQuizHistory()
    ) { args ->
        val name = args[0] as String?
        val email = args[1] as String?
        val xp = args[2] as Int
        val streak = args[3] as Int
        val longest = args[4] as Int
        val coins = args[5] as Int
        val bookmarks = args[6] as List<*>
        val notes = args[7] as List<*>
        @Suppress("UNCHECKED_CAST")
        val booklets = args[8] as List<Booklet>
        @Suppress("UNCHECKED_CAST")
        val history = args[9] as List<com.akash.gkgsmaster.data.model.QuizHistory>
        
        val avgAccuracy = if (history.isNotEmpty()) history.map { it.accuracy }.average().toInt() else 0
        
        UserProfile(
            name = name ?: "Aspirant",
            email = email ?: "aspirant@example.com",
            totalQuizScore = history.sumOf { it.score },
            xp = xp,
            coins = coins,
            bookmarksCount = bookmarks.size,
            notesCount = notes.size,
            bookletsRead = booklets.count { it.progress > 0 },
            studyHours = (xp / 50f), // 1 hour per 50 XP
            dailyStreak = streak,
            longestStreak = longest,
            achievements = listOf(
                Achievement("1", "Early Bird", R.drawable.ic_placeholder, true),
                Achievement("2", "Quiz Master", R.drawable.ic_placeholder, history.size >= 10),
                Achievement("3", "Note Taker", R.drawable.ic_placeholder, notes.size >= 5),
                Achievement("4", "Scholar", R.drawable.ic_placeholder, booklets.count { it.progress == 100 } >= 1),
                Achievement("5", "Sharp Shooter", R.drawable.ic_placeholder, avgAccuracy >= 90 && history.size >= 5)
            )
        )
    }.asLiveData()

    fun logout() {
        viewModelScope.launch {
            preferenceManager.clear()
        }
    }

    fun updateProfile(newName: String, newEmail: String) {
        viewModelScope.launch {
            preferenceManager.updateUserName(newName)
            preferenceManager.setLoggedIn(true, newName, newEmail) // This updates email too
            println("ProfileVM: Updated profile to $newName / $newEmail")
        }
    }
}
