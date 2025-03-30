package com.example.parkour.data.database.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.Obstacle

interface ObstacleDao {
    @Insert
    suspend fun insertObstacle(obstacle: Obstacle)

    @Query("SELECT id, createdAt, updatedAt, name FROM Obstacle")
    suspend fun getAllObstacles(): List<Obstacle>

    @Query("SELECT id, createdAt, updatedAt, name FROM Obstacle WHERE id = :obstacleId")
    suspend fun getObstacleById(obstacleId: Int): Obstacle?
}