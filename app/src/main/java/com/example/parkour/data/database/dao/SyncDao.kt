package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.SyncEntity

@Dao
interface SyncDao {
    @Insert
    suspend fun insertSyncEntity(syncEntity: SyncEntity)

    @Query ("SELECT * FROM SyncEntity")
    suspend fun getAllSyncActions(): List<SyncEntity>

    @Delete
    suspend fun deleteSyncEntity(syncEntity: SyncEntity)
}