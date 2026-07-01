package com.akash.gkgsmaster.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.PreferenceManager
import com.akash.gkgsmaster.data.model.Achievement
import com.akash.gkgsmaster.data.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val userProfile: LiveData<UserProfile> = combine(
        preferenceManager.userName,
        preferenceManager.userEmail,
        preferenceManager.userImage
    ) { name, email, image ->
        UserProfile(
            name = name ?: "Aspirant",
            email = email ?: "aspirant@example.com",
            profileImage = image,
            totalQuizScore = 1250,
            xp = 2450,
            bookmarksCount = 15,
            notesCount = 8,
            studyHours = 45.5f,
            dailyStreak = 7,
            achievements = listOf(
                Achievement("1", "Early Bird", R.drawable.ic_placeholder, true),
                Achievement("2", "Quiz Master", R.drawable.ic_placeholder, true),
                Achievement("3", "Scholar", R.drawable.ic_placeholder, false)
            )
        )
    }.asLiveData()

    fun logout() {
        viewModelScope.launch {
            preferenceManager.clear()
        }
    }
}
