package com.example.parkour.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkour.data.database.dao.CompetitionDao
import com.example.parkour.data.database.dao.PerformanceDao
import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Performance
import com.example.parkour.data.model.Course

@Database(entities = [Competition::class, Performance::class, Course::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competitionDao(): CompetitionDao
    abstract fun performanceDao(): PerformanceDao
}