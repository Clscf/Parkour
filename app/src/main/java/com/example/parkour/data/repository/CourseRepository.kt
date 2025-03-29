package com.example.parkour.repository

import com.example.parkour.data.model.Course
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

    suspend fun updateCourse(id: Int, courseUpdate: CourseUpdate): Response<Course> {
        return apiService.updateCourse(id, courseUpdate)
    }

    suspend fun deleteCourse(id: Int): Response<Unit> {
        return apiService.deleteCourse(id)
    }
}
