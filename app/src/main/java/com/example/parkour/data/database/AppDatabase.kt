package com.example.parkour.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkour.data.database.dao.CompetitionDao
import com.example.parkour.data.database.dao.CompetitorDao
import com.example.parkour.data.database.dao.CourseDao
import com.example.parkour.data.database.dao.ObstacleDao
import com.example.parkour.data.database.dao.PerformanceDao
import com.example.parkour.data.database.dao.PerformanceObstacleDao
import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Competitor
import com.example.parkour.data.model.Performance
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.model.PerformanceObstacle

@Database(
    entities = [Competition::class, Performance::class, Course::class, Obstacle::class, Competitor::class, PerformanceObstacle::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competitionDao(): CompetitionDao
    abstract fun performanceDao(): PerformanceDao
    abstract fun courseDao(): CourseDao
    abstract fun obstacleDao(): ObstacleDao
    abstract fun competitorDao(): CompetitorDao
    abstract fun performanceObstacleDao(): PerformanceObstacleDao
}
