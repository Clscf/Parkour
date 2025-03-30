package com.example.parkour.repository

import com.example.parkour.data.model.CourseObstacle
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

    suspend fun updateObstacle(id: Int, obstacleUpdate: ObstacleUpdate): Response<Unit> {
        return apiService.updateObstacle(id, obstacleUpdate)
    }

    suspend fun deleteObstacle(id: Int): Response<Unit> {
        return apiService.deleteObstacle(id)
    }

    // Fonction pour créer un obstacle via l'API
    suspend fun createObstacle(obstacleCreate: ObstacleCreate): Response<Obstacle> {
        return apiService.addObstacle(obstacleCreate)
    }

    // Fonction pour récupérer les obstacles d'une course
    suspend fun getObstaclesForCourse(courseId: Int): List<CourseObstacle> {
        val response = apiService.getCourseObstacles(courseId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Erreur lors de la récupération des obstacles: ${response.message()}")
        }
    }
}
