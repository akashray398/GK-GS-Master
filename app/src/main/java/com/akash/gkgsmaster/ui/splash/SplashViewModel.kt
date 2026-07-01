package com.akash.gkgsmaster.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akash.gkgsmaster.data.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<SplashNavigation>()
    val navigationEvents: SharedFlow<SplashNavigation> = _navigationEvents

    fun checkNavigation() {
        viewModelScope.launch {
            val isFirstLaunch = preferenceManager.isFirstLaunch.first()
            val isLoggedIn = preferenceManager.isLoggedIn.first()

            if (isFirstLaunch) {
                _navigationEvents.emit(SplashNavigation.Onboarding)
            } else if (isLoggedIn) {
                _navigationEvents.emit(SplashNavigation.Main)
            } else {
                _navigationEvents.emit(SplashNavigation.Login)
            }
        }
    }

    sealed class SplashNavigation {
        object Onboarding : SplashNavigation()
        object Login : SplashNavigation()
        object Main : SplashNavigation()
    }
}
