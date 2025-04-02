package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.Obstacle

@Dao
interface ObstacleDao {
    @Insert
    fun insertObstacle(obstacle: Obstacle)

    @Query("SELECT id, createdAt, updatedAt, name FROM Obstacle")
    fun getAllObstacles(): List<Obstacle>

    @Query("SELECT id, createdAt, updatedAt, name FROM Obstacle WHERE id = :obstacleId")
    fun getObstacleById(obstacleId: Int): Obstacle?
}