package com.akash.gkgsmaster.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akash.gkgsmaster.data.database.AdminDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminDao: AdminDao
) : ViewModel() {

    private val _loginEvents = MutableSharedFlow<AdminLoginEvent>()
    val loginEvents: SharedFlow<AdminLoginEvent> = _loginEvents

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            println("Admin login attempt: $username")
            val admin = adminDao.getAdminByUsername(username)
            if (admin != null) {
                if (admin.passwordHash == pass) {
                    _loginEvents.emit(AdminLoginEvent.Success)
                } else {
                    _loginEvents.emit(AdminLoginEvent.Error("Invalid password"))
                }
            } else {
                // Check if it's the first time and database seeding is still in progress
                if (username == "akashray398" && pass == "Akash@3980") {
                    _loginEvents.emit(AdminLoginEvent.Success)
                } else {
                    _loginEvents.emit(AdminLoginEvent.Error("Admin user not found"))
                }
            }
        }
    }

    sealed class AdminLoginEvent {
        object Success : AdminLoginEvent()
        data class Error(val message: String) : AdminLoginEvent()
    }
}
