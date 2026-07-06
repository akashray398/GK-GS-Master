package com.akash.gkgsmaster.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akash.gkgsmaster.data.model.RecentActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentActivityDao {
    @Query("SELECT * FROM recent_activity ORDER BY timestamp DESC LIMIT 20")
    fun getRecentActivities(): Flow<List<RecentActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: RecentActivityEntity)

    @Query("DELETE FROM recent_activity")
    suspend fun clearAll()
}
