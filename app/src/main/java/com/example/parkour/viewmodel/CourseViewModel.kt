package com.example.parkour.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.create.CompetitionCreate
import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.model.create.CourseCreate
import com.example.parkour.data.model.uptdate.CompetitionUpdate
import com.example.parkour.data.model.uptdate.CourseUpdate
import com.example.parkour.repository.CompetitionRepository
import com.example.parkour.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            try {
                val response = repository.getCourses()
                if (response.isSuccessful) {
                    _courses.value = response.body() ?: emptyList()
                    Log.d("CourseViewModel", "Courses chargées : ${_courses.value}")
                } else {
                    Log.e("CourseViewModel", "Erreur lors du chargement : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Exception lors du chargement des courses : ${e.message}")
            }
        }
    }


    fun deleteCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.deleteCourse(courseId)
                if (response.isSuccessful) {
                    _courses.value = _courses.value.filterNot { it.id == courseId }
                    Log.d("CompetitionViewModel", "Parcours supprimé avec succès")
                } else {
                    Log.e("CompetitionViewModel", "Erreur lors de la suppression : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception lors de la suppression : ${e.message}")
            }
        }
    }

    fun updateCourse(courseId: Int, courseUpdate: CourseUpdate) {
        viewModelScope.launch {
            try {
                val response = repository.updateCourse(courseId, courseUpdate)
                if (response.isSuccessful) {
                    _courses.value = _courses.value.map { course ->
                        if (course.id == courseUpdate.competitionId) {
                            course.copy(
                                name = courseUpdate.name,
                                maxDuration = courseUpdate.maxDuration,
                                position = courseUpdate.position,
                                isOver = courseUpdate.isOver
                            )
                        } else course
                    }
                    Log.d("CourseViewModel", "Parcours mis à jour avec succès")
                } else {
                    Log.e("CourseViewModel", "Erreur de mise à jour : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Exception lors de la mise à jour : ${e.message}")
            }
        }
    }


}
