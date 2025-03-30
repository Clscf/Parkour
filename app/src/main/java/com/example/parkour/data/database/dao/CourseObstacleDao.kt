package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.CourseObstacle

@Dao
interface CourseObstacleDao {
    @Insert
    suspend fun insertCourseObstacle(courseObstacle: CourseObstacle)

    @Query("SELECT * FROM CourseObstacle WHERE position = :position AND obstacleName = :obstacleName")
    suspend fun getCourseObstacleByPositionAndName(position: Int, obstacleName: String): List<CourseObstacle>

    @Query("SELECT * FROM CourseObstacle WHERE id = :courseObstacleId")
    suspend fun getCourseObstacleById(courseObstacleId: Int): CourseObstacle?

    @Query("SELECT * FROM CourseObstacle WHERE position = :position")
    suspend fun getCourseObstaclesByPosition(position: Int): List<CourseObstacle>

    @Query("DELETE FROM CourseObstacle WHERE id = :courseObstacleId")
    suspend fun deleteCourseObstacleById(courseObstacleId: Int)
}

