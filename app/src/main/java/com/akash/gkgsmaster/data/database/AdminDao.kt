package com.akash.gkgsmaster.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akash.gkgsmaster.data.model.AdminActivity
import com.akash.gkgsmaster.data.model.AdminUser
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminDao {
    @Query("SELECT * FROM admin_users WHERE username = :username")
    suspend fun getAdminByUsername(username: String): AdminUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun logActivity(activity: AdminActivity)

    @Query("SELECT * FROM admin_activity_log ORDER BY timestamp DESC LIMIT 100")
    fun getActivityLog(): Flow<List<AdminActivity>>
}
