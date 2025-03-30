package com.example.parkour.network

import com.example.parkour.data.model.*
import com.example.parkour.data.model.create.*
import com.example.parkour.data.model.uptdate.*
import com.example.parkour.data.model.uptdate.PerformanceUpdate
import retrofit2.http.*
import retrofit2.Response

interface ApiService {


    @GET("/api/competitions")
    suspend fun getCompetitions(): Response<List<Competition>>

    @POST("/api/competitions")
    suspend fun addCompetition(@Body competition: CompetitionCreate): Response<Competition>

    @GET("/api/competitions/{id}")
    suspend fun getCompetition(@Path("id") id: Int): Response<Competition>

    @PUT("/api/competitions/{id}")
    suspend fun updateCompetition(@Path("id") id: Int, @Body competition: CompetitionUpdate): Response<Unit>

    @DELETE("/api/competitions/{id}")
    suspend fun deleteCompetition(@Path("id") id: Int): Response<Unit>

    @GET("/api/competitions/{id}/inscriptions")
    suspend fun getCompetitionCompetitors(@Path("id") id: Int): Response<List<Competitor>>

    @POST("/api/competitions/{id}/add_competitor")
    suspend fun addCompetitorToCompetition(@Path("id") id: Int, @Body competitor: Competitor): Response<Unit>

    @DELETE("/api/competitions/{id}/remove_competitor/{id_competitor}")
    suspend fun removeCompetitorFromCompetition(@Path("id") id: Int, @Path("id_competitor") competitorId: Int): Response<Unit>

    @GET("/api/competitions/{id}/courses")
    suspend fun getCompetitionCourses(@Path("id") id: Int): Response<List<Course>>

    // Competitors
    @GET("/api/competitors")
    suspend fun getCompetitors(): Response<List<Competitor>>

    @POST("/api/competitors")
    suspend fun addCompetitor(@Body competitor: CompetitorCreate): Response<Competitor>

    @GET("/api/competitors/{id}")
    suspend fun getCompetitor(@Path("id") id: Int): Response<Competitor>

    @PUT("/api/competitors/{id}")
    suspend fun updateCompetitor(@Path("id") id: Int, @Body competitor: CompetitorUpdate): Response<Unit>

    @DELETE("/api/competitors/{id}")
    suspend fun deleteCompetitor(@Path("id") id: Int): Response<Unit>

    @GET("/api/competitors/{id}/performances")
    suspend fun getCompetitorPerformances(@Path("id") id: Int): Response<List<Performance>>

    @GET("/api/competitors/{id}/courses")
    suspend fun getCompetitorCourses(@Path("id") id: Int): Response<List<Course>>

    @GET("/api/competitors/{id}/{id_course}/details_performances")
    suspend fun getCompetitorPerformanceDetails(@Path("id") id: Int, @Path("id_course") courseId: Int): Response<List<PerformanceObstacle>>

    // Courses
    @GET("/api/courses")
    suspend fun getCourses(): Response<List<Course>>

    @POST("/api/courses")
    suspend fun addCourse(@Body course: CourseCreate): Response<Course>

    @GET("/api/courses/{id}")
    suspend fun getCourse(@Path("id") id: Int): Response<Course>

    @PUT("/api/courses/{id}")
    suspend fun updateCourse(@Path("id") id: Int, @Body course: CourseUpdate): Response<Unit>

    @DELETE("/api/courses/{id}")
    suspend fun deleteCourse(@Path("id") id: Int): Response<Unit>

    @GET("/api/courses/{id}/obstacles")
    suspend fun getCourseObstacles(@Path("id") id: Int): Response<List<CourseObstacle>>

    @GET("/api/courses/{id}/performances")
    suspend fun getCoursePerformances(@Path("id") id: Int): Response<List<Performance>>

    @POST("/api/courses/{id}/add_obstacle")
    suspend fun addObstacleToCourse(@Path("id") id: Int, @Body obstacle: ObstacleCreate): Response<Unit>

    @DELETE("/api/courses/{id}/remove_obstacle/{id_obstacle}")
    suspend fun removeObstacleFromCourse(@Path("id") id: Int, @Path("id_obstacle") obstacleId: Int): Response<Unit>

    @POST("/api/courses/{id}/update_obstacle_position")
    suspend fun updateObstaclePosition(@Path("id") id: Int, @Body courseObstacle: CourseObstacleUpdate): Response<Unit>

    // Obstacles
    @GET("/api/obstacles")
    suspend fun getObstacles(): Response<List<Obstacle>>

    @POST("/api/obstacles")
    suspend fun addObstacle(@Body obstacle: ObstacleCreate): Response<Obstacle>

    @GET("/api/obstacles/{id}")
    suspend fun getObstacle(@Path("id") id: Int): Response<Obstacle>

    @PUT("/api/obstacles/{id}")
    suspend fun updateObstacle(@Path("id") id: Int, @Body obstacle: ObstacleUpdate): Response<Unit>

    @DELETE("/api/obstacles/{id}")
    suspend fun deleteObstacle(@Path("id") id: Int): Response<Unit>

    // Performance Obstacles
    @GET("/api/performance_obstacles")
    suspend fun getPerformanceObstacles(): Response<List<PerformanceObstacle>>

    @POST("/api/performance_obstacles")
    suspend fun addPerformanceObstacle(@Body performanceObstacle: PerformanceObstacleCreate): Response<PerformanceObstacle>

    @GET("/api/performance_obstacles/{id}")
    suspend fun getPerformanceObstacle(@Path("id") id: Int): Response<PerformanceObstacle>

    @PUT("/api/performance_obstacles/{id}")
    suspend fun updatePerformanceObstacle(@Path("id") id: Int, @Body performanceObstacle: PerformanceObstacleUpdate): Response<Unit>

    // Performances
    @GET("/api/performances")
    suspend fun getPerformances(): Response<List<Performance>>

    @POST("/api/performances")
    suspend fun addPerformance(@Body performance: PerformanceCreate): Response<Performance>

    @GET("/api/performances/{id}")
    suspend fun getPerformance(@Path("id") id: Int): Response<Performance>

    @PUT("/api/performances/{id}")
    suspend fun updatePerformance(@Path("id") id: Int, @Body performance: PerformanceUpdate): Response<Unit>

    @DELETE("/api/performances/{id}")
    suspend fun deletePerformance(@Path("id") id: Int): Response<Unit>

    @GET("/api/performances/{id}/details")
    suspend fun getPerformanceDetails(@Path("id") id: Int): Response<List<PerformanceObstacle>>

}