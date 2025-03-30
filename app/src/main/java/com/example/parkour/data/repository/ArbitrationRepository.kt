package com.example.parkour.data.repository

import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Competitor
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.CourseObstacle
import com.example.parkour.data.model.PerformanceObstacle
import com.example.parkour.data.model.create.PerformanceObstacleCreate
import com.example.parkour.network.ApiService
import retrofit2.Response

class ArbitrationRepository(private val apiService: ApiService) {
    suspend fun getCompetitionCourses(competitionId: Int): Response<List<Course>> {
        return apiService.getCompetitionCourses(competitionId)
    }

    suspend fun getCourseObstacles(courseId: Int): Response<List<CourseObstacle>> {
        return apiService.getCourseObstacles(courseId)
    }

    suspend fun addPerformanceObstacle(performanceObstacle: PerformanceObstacleCreate): Response<PerformanceObstacle> {
        return apiService.addPerformanceObstacle(performanceObstacle)
    }

    suspend fun getCompetitors(): Response<List<Competitor>> {
        return apiService.getCompetitors()
    }

    suspend fun getCompetitions(): Response<List<Competition>> {
        return apiService.getCompetitions()
    }

    suspend fun getCompetitorsForCompetition(competitionId: Int): Response<List<Competitor>> {
        return apiService.getCompetitionCompetitors(competitionId)
    }
}