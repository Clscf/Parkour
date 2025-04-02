package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.parkour.data.model.Course

@Dao
interface CourseDao {
    @Insert
    fun insertCourse(course: Course)

    @Update
    fun updateCourse(course: Course)

    @Query("DELETE FROM Course WHERE id = :courseId")
    fun deleteCourse(courseId: Int)

    @Query("SELECT * FROM Course WHERE id = :courseId")
    fun getCourseById(courseId: Int): Course?
}