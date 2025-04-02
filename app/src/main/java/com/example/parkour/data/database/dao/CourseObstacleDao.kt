package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.CourseObstacle

@Dao
interface CourseObstacleDao {
    @Insert
    fun insertCourseObstacle(courseObstacle: CourseObstacle)

    @Query("SELECT * FROM CourseObstacle WHERE position = :position AND obstacleName = :obstacleName")
    fun getCourseObstacleByPositionAndName(position: Int, obstacleName: String): List<CourseObstacle>

    @Query("SELECT * FROM CourseObstacle WHERE id = :courseObstacleId")
    fun getCourseObstacleById(courseObstacleId: Int): CourseObstacle?

    @Query("SELECT * FROM CourseObstacle WHERE position = :position")
    fun getCourseObstaclesByPosition(position: Int): List<CourseObstacle>

    @Delete
    fun deleteCourseObstacleById(courseObstacle: CourseObstacle)
}

