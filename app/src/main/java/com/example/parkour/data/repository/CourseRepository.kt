package com.example.parkour.repository

import com.example.parkour.data.model.Course
import com.example.parkour.data.model.CourseObstacle
import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.model.create.CourseCreate
import com.example.parkour.data.model.uptdate.CourseUpdate
import com.example.parkour.network.ApiService
import retrofit2.Response

class CourseRepository(private val apiService: ApiService) {

    suspend fun getCourses(): Response<List<Course>> {
        return apiService.getCourses()
    }

    suspend fun addCourse(courseCreate: CourseCreate): Response<Course> {
        return apiService.addCourse(courseCreate)
    }

    suspend fun getCourse(id: Int): Response<Course> {
        return apiService.getCourse(id)
    }

    suspend fun updateCourse(id: Int, courseUpdate: CourseUpdate): Response<Unit> {
        return apiService.updateCourse(id, courseUpdate)
    }

    suspend fun deleteCourse(id: Int): Response<Unit> {
        return apiService.deleteCourse(id)
    }

    // Ajoute un obstacle à une course
    suspend fun addObstacleToCourse(courseId: Int, obstacleId: Int): Response<Unit> {
        return apiService.addObstacleToCourse(courseId, obstacleId)
    }

    // Supprime un obstacle d'une course
    suspend fun removeObstacleFromCourse(courseId: Int, obstacle: Int): Response<Unit> {
        return apiService.removeObstacleFromCourse(courseId, obstacle)
    }

    // Récupère les obstacles associés à une course
    suspend fun getCourseObstacles(courseId: Int): Response<List<CourseObstacle>> {
        return apiService.getCourseObstacles(courseId)
    }

    suspend fun getObstacles(): Response<List<Obstacle>> {
        return apiService.getObstacles()
    }
}
