package com.akash.gkgsmaster.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    private val STREAK_COUNT = intPreferencesKey("streak_count")
    private val LONGEST_STREAK = intPreferencesKey("longest_streak")
    private val LAST_LOGIN_DATE = longPreferencesKey("last_login_date")
    private val TOTAL_XP = intPreferencesKey("total_xp")
    private val COINS = intPreferencesKey("coins")
    private val LANGUAGE = stringPreferencesKey("app_language")

    val userName: Flow<String?> = context.dataStore.data.map { it[USER_NAME] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[USER_EMAIL] }
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { it[IS_FIRST_LAUNCH] ?: true }
    val streakCount: Flow<Int> = context.dataStore.data.map { it[STREAK_COUNT] ?: 0 }
    val longestStreak: Flow<Int> = context.dataStore.data.map { it[LONGEST_STREAK] ?: 0 }
    val totalXp: Flow<Int> = context.dataStore.data.map { it[TOTAL_XP] ?: 0 }
    val coins: Flow<Int> = context.dataStore.data.map { it[COINS] ?: 0 }
    val language: Flow<String> = context.dataStore.data.map { it[LANGUAGE] ?: "en" }

    suspend fun setLoggedIn(loggedIn: Boolean, name: String? = null, email: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
            name?.let { preferences[USER_NAME] = it }
            email?.let { preferences[USER_EMAIL] = it }
        }
    }

    suspend fun setFirstLaunchCompleted() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH] = false
        }
    }

    suspend fun updateUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    suspend fun updateStreak() {
        context.dataStore.edit { preferences ->
            val lastLogin = preferences[LAST_LOGIN_DATE] ?: 0L
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            
            val yesterday = today - 86400000L
            
            if (lastLogin < today) {
                if (lastLogin == yesterday) {
                    val newStreak = (preferences[STREAK_COUNT] ?: 0) + 1
                    preferences[STREAK_COUNT] = newStreak
                    val currentLongest = preferences[LONGEST_STREAK] ?: 0
                    if (newStreak > currentLongest) preferences[LONGEST_STREAK] = newStreak
                } else if (lastLogin < yesterday) {
                    preferences[STREAK_COUNT] = 1
                }
                preferences[LAST_LOGIN_DATE] = today
            }
        }
    }

    suspend fun addXp(xp: Int) {
        context.dataStore.edit { preferences ->
            preferences[TOTAL_XP] = (preferences[TOTAL_XP] ?: 0) + xp
        }
    }

    suspend fun addCoins(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[COINS] = (preferences[COINS] ?: 0) + count
        }
    }

    suspend fun setLanguage(langCode: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = langCode
            println("App language set to: $langCode")
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
