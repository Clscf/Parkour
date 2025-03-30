package com.example.parkour.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.model.uptdate.CourseUpdate
import com.example.parkour.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses
    // Liste des obstacles associés à une course
    private val _courseObstacles = MutableStateFlow<List<Obstacle>>(emptyList())
    val courseObstacles: StateFlow<List<Obstacle>> = _courseObstacles


    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            try {
                val response = repository.getCourses()
                if (response.isSuccessful) {
                    _courses.value = response.body() ?: emptyList()
                } else {
                    Log.e("CourseViewModel", "Erreur lors du chargement des courses")
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

    fun addObstacleToCourse(courseId: Int, obstacleId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.addObstacleToCourse(courseId, obstacleId)
                if (response.isSuccessful) {
                    // Recharger les obstacles pour la course après l'ajout
                    loadObstaclesForCourse(courseId)
                } else {
                    Log.e("CourseViewModel", "Erreur lors de l'ajout de l'obstacle: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Exception lors de l'ajout de l'obstacle : ${e.message}")
            }
        }
    }



    fun removeObstacleFromCourse(courseId: Int, obstacleId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.removeObstacleFromCourse(courseId, obstacleId)
                if (response.isSuccessful) {
                    // Recharger les obstacles après suppression
                    loadObstaclesForCourse(courseId)
                } else {
                    Log.e("CourseViewModel", "Erreur lors de la suppression de l'obstacle")
                }
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Exception lors de la suppression de l'obstacle : ${e.message}")
            }
        }
    }

    fun loadObstaclesForCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                // Étape 1: Charger les obstacles associés à la course
                val courseObstaclesResponse = repository.getCourseObstacles(courseId)
                if (courseObstaclesResponse.isSuccessful) {
                    val courseObstacles = courseObstaclesResponse.body() ?: emptyList()

                    // Étape 2: Extraire les IDs des obstacles associés à la course
                    val obstacleIds = courseObstacles.map { it.id }

                    // Étape 3: Charger tous les obstacles existants
                    val obstaclesResponse = repository.getObstacles()
                    if (obstaclesResponse.isSuccessful) {
                        val allObstacles = obstaclesResponse.body() ?: emptyList()

                        // Étape 4: Filtrer les obstacles pour ne garder que ceux qui sont associés à la course
                        val obstaclesForCourse = allObstacles.filter { obstacle ->
                            obstacle.id in obstacleIds
                        }

                        // Étape 5: Mettre à jour l'état avec les obstacles associés à la course
                        _courseObstacles.value = obstaclesForCourse
                    } else {
                        Log.e("CourseViewModel", "Erreur lors du chargement des obstacles existants")
                    }
                } else {
                    Log.e("CourseViewModel", "Erreur lors du chargement des obstacles associés à la course")
                }
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Exception lors du chargement des obstacles : ${e.message}")
            }
        }
    }

}
