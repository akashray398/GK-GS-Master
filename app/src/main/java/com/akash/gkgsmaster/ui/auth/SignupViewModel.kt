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
class SignupViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signupState = MutableLiveData<SignupState>()
    val signupState: LiveData<SignupState> = _signupState

    fun signup(name: String, email: String, pass: String, confirmPass: String) {
        if (!validate(name, email, pass, confirmPass)) return

        viewModelScope.launch {
            _signupState.value = SignupState.Loading
            val result = repository.signup(name, email, pass)
            if (result.isSuccess) {
                _signupState.value = SignupState.Success
            } else {
                _signupState.value = SignupState.Error(result.exceptionOrNull()?.message ?: "Signup Failed")
            }
        }
    }

    private fun validate(name: String, email: String, pass: String, confirmPass: String): Boolean {
        if (name.isBlank()) {
            _signupState.value = SignupState.InvalidInput("Please enter your name")
            return false
        }
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _signupState.value = SignupState.InvalidInput("Please enter a valid email")
            return false
        }
        if (pass.length < 6) {
            _signupState.value = SignupState.InvalidInput("Password must be at least 6 characters")
            return false
        }
        if (pass != confirmPass) {
            _signupState.value = SignupState.InvalidInput("Passwords do not match")
            return false
        }
        return true
    }

    sealed class SignupState {
        object Loading : SignupState()
        object Success : SignupState()
        data class Error(val message: String) : SignupState()
        data class InvalidInput(val message: String) : SignupState()
    }
}
