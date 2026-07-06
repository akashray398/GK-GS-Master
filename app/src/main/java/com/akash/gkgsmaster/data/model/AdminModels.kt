package com.akash.gkgsmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_users")
data class AdminUser(
    @PrimaryKey
    val username: String,
    val passwordHash: String,
    val role: String = "SUPER_ADMIN"
)

@Entity(tableName = "admin_activity_log")
data class AdminActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val adminUsername: String,
    val action: String, // "ADD_BOOKLET", "DELETE_QUESTION", etc.
    val timestamp: Long = System.currentTimeMillis()
)
