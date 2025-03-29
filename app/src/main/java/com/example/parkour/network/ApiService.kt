package com.example.parkour.network

import com.example.parkour.data.model.CompetitionCreate
import com.example.parkour.data.model.CompetitionUpdate
import com.example.parkour.data.model.Competitions
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.Obstacle
import retrofit2.http.*
import retrofit2.Response

interface ApiService {
    @GET("courses")
    suspend fun getCourses(): List<Course>

    @GET("courses/{id}/obstacles")
    suspend fun getObstaclesForCourse(@Path("id") courseId: Int): List<Obstacle>

    @GET("competitions")
    suspend fun getCompetitions(): List<Competitions>

    @GET("competitions/{id}")
    suspend fun getCompetitionById(@Path("id") competitionId: Int): Competitions

    @POST("/api/competitions")
    suspend fun addCompetition(@Body competitionCreate: CompetitionCreate): Response<CompetitionCreate>

    @DELETE("competitions/{id}")
    suspend fun deleteCompetition(@Path("id") competitionId: Int)

    @PUT("competitions/{id}")
    suspend fun updateCompetition(@Path("id") competitionId: Int, @Body updatedCompetition: CompetitionUpdate)

}