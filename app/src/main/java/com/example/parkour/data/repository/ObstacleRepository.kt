package com.example.parkour.repository

import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.model.create.ObstacleCreate
import com.example.parkour.data.model.uptdate.ObstacleUpdate
import com.example.parkour.network.ApiService
import retrofit2.Response

class ObstacleRepository(private val apiService: ApiService) {

    suspend fun getObstacles(): Response<List<Obstacle>> {
        return apiService.getObstacles()
    }

    suspend fun addObstacle(obstacleCreate: ObstacleCreate): Response<Obstacle> {
        return apiService.addObstacle(obstacleCreate)
    }

    suspend fun getObstacle(id: Int): Response<Obstacle> {
        return apiService.getObstacle(id)
    }

    suspend fun updateObstacle(id: Int, obstacleUpdate: ObstacleUpdate): Response<Obstacle> {
        return apiService.updateObstacle(id, obstacleUpdate)
    }

    suspend fun deleteObstacle(id: Int): Response<Unit> {
        return apiService.deleteObstacle(id)
    }
}
