package com.example.parkour.data.database.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.PerformanceObstacle

interface PerformanceObstacleDao {
    @Insert
    suspend fun insertPerformanceObstacle(performanceObstacle: PerformanceObstacle)

    @Query("SELECT * FROM PerformanceObstacle WHERE performanceId = :performanceId")
    suspend fun getPerformanceObstaclesByPerformanceId(performanceId: Int): List<PerformanceObstacle>
}