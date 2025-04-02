package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.PerformanceObstacle

@Dao
interface PerformanceObstacleDao {
    @Insert
    fun insertPerformanceObstacle(performanceObstacle: PerformanceObstacle)

    @Query("SELECT * FROM PerformanceObstacle WHERE performanceId = :performanceId")
    fun getPerformanceObstaclesByPerformanceId(performanceId: Int): List<PerformanceObstacle>
}