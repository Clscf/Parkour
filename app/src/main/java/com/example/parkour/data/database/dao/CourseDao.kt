package com.example.parkour.data.database.dao

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import com.example.parkour.data.model.Course
interface CourseDao {
    @Insert
    suspend fun insertCourse(course: Course)

    @Query("SELECT * FROM Course WHERE competitionId = :competitionId")
    suspend fun getCoursesByCompetition(competitionId: Int): List<Course>

    @Update
    suspend fun updateCourse(course: Course)

    @Query("DELETE FROM Course WHERE id = :courseId")
    suspend fun deleteCourse(courseId: Int)
}