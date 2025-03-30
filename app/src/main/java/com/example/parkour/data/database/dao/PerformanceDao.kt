package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Performance


@Dao
interface PerformanceDao {
    @Insert
    suspend fun insertPerformance(performance: Performance)

    @Query("SELECT * FROM Performance WHERE courseId = :courseId")
    suspend fun getPerformanceByCourse(courseId: Int): List<Performance>

}