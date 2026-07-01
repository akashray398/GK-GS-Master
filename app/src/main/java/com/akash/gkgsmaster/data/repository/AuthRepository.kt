package com.akash.gkgsmaster.data.repository

import com.akash.gkgsmaster.data.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    suspend fun login(email: String, password: String): Result<Unit> {
        // Placeholder for API call
        return try {
            preferenceManager.setLoggedIn(true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signup(name: String, email: String, password: String): Result<Unit> {
        // Placeholder for API call
        return try {
            preferenceManager.updateUserName(name)
            preferenceManager.setLoggedIn(true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginAsGuest(): Result<Unit> {
        return try {
            preferenceManager.updateUserName("Guest")
            preferenceManager.setLoggedIn(true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun forgotPassword(email: String): Result<Unit> {
        // Placeholder for API call
        return Result.success(Unit)
    }
}
