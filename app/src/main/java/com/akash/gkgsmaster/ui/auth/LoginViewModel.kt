package com.akash.gkgsmaster.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akash.gkgsmaster.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(email: String, pass: String) {
        if (!validate(email, pass)) return

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = repository.login(email, pass)
            if (result.isSuccess) {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error(result.exceptionOrNull()?.message ?: "Login Failed")
            }
        }
    }

    fun loginAsGuest() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = repository.loginAsGuest()
            if (result.isSuccess) {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("Guest login failed")
            }
        }
    }

    fun forgotPassword(email: String) {
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = LoginState.InvalidInput("Please enter a valid email to reset password")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = repository.forgotPassword(email)
            if (result.isSuccess) {
                _loginState.value = LoginState.Error("Password reset link sent to $email") // Using error state to show message for simplicity
            } else {
                _loginState.value = LoginState.Error("Failed to send reset link")
            }
        }
    }

    private fun validate(email: String, pass: String): Boolean {
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = LoginState.InvalidInput("Please enter a valid email")
            return false
        }
        if (pass.length < 6) {
            _loginState.value = LoginState.InvalidInput("Password must be at least 6 characters")
            return false
        }
        return true
    }

    sealed class LoginState {
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
        data class InvalidInput(val message: String) : LoginState()
    }
}
