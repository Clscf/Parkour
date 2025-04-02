package com.example.parkour.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkour.data.database.dao.*
import com.example.parkour.data.model.*

@Database(
    entities = [Competition::class, Performance::class, Course::class, Obstacle::class, Competitor::class, PerformanceObstacle::class, CourseObstacle::class, SyncEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competitionDao(): CompetitionDao
    abstract fun performanceDao(): PerformanceDao
    abstract fun courseDao(): CourseDao
    abstract fun obstacleDao(): ObstacleDao
    abstract fun competitorDao(): CompetitorDao
    abstract fun performanceObstacleDao(): PerformanceObstacleDao
    abstract fun courseObstacleDao(): CourseObstacleDao
    abstract fun syncDao(): SyncDao
}