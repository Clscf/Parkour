package com.example.parkour.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.CourseObstacle
import com.example.parkour.data.model.PerformanceObstacle
import com.example.parkour.data.model.create.PerformanceObstacleCreate
import com.example.parkour.data.repository.ArbitrationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ArbitrationViewModel(private val repository: ArbitrationRepository) : ViewModel() {

    private var timerJob: Job? = null
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private val _obstacles = MutableStateFlow<List<CourseObstacle>>(emptyList())
    val obstacles: StateFlow<List<CourseObstacle>> = _obstacles

    private val _performances = MutableStateFlow<List<PerformanceObstacle>>(emptyList())
    val performances: StateFlow<List<PerformanceObstacle>> = _performances

    private val _selectedCourseId = MutableStateFlow<Int?>(null)
    val selectedCourseId: StateFlow<Int?> = _selectedCourseId

    private val _currentObstacle = MutableStateFlow<CourseObstacle?>(null)
    val currentObstacle: StateFlow<CourseObstacle?> = _currentObstacle


    init {
        // Charger les données si nécessaire lors de l'initialisation
    }

    // Charger les parcours d'une compétition
    fun loadCourses(competitionId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCompetitionCourses(competitionId)
                if (response.isSuccessful) {
                    _courses.value = response.body() ?: emptyList()
                } else {
                    Log.e("ArbitrationViewModel", "Erreur API : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur API : ${e.message}")
            }
        }
    }

    // Charger les obstacles pour un parcours donné
    fun loadObstacles(courseId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCourseObstacles(courseId)
                if (response.isSuccessful) {
                    _obstacles.value = response.body() ?: emptyList()
                } else {
                    Log.e("ArbitrationViewModel", "Erreur API : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur API : ${e.message}")
            }
        }
    }

    // Ajouter une performance pour un obstacle
    fun addPerformanceObstacle(performanceObstacleCreate: PerformanceObstacleCreate) {
        viewModelScope.launch {
            try {
                val response = repository.addPerformanceObstacle(performanceObstacleCreate)
                if (response.isSuccessful) {
                    response.body()?.let { newPerformance ->
                        _performances.value = _performances.value + newPerformance
                        Log.d("ArbitrationViewModel", "Performance ajoutée avec succès")
                    }
                } else {
                    Log.e("ArbitrationViewModel", "Échec de l'ajout de la performance : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur lors de l'ajout de la performance : ${e.message}")
            }
        }
    }

    // Sélectionner un parcours actif
    fun selectCourse(courseId: Int) {
        _selectedCourseId.value = courseId
        loadObstacles(courseId)
    }

    fun startTimer(onTick: (Long) -> Unit) {
        timerJob?.cancel()
        _elapsedTime.value = 0L
        
        timerJob = viewModelScope.launch { 
            val startTime = System.currentTimeMillis()
            while(isActive) {
                val currentTime = System.currentTimeMillis()
                _elapsedTime.value = currentTime - startTime
                onTick(_elapsedTime.value)
                delay(10)
            }
        }
    }
    
    fun stopTimer() {
        timerJob?.cancel()
    }

    fun registerPerformance(time: Double, hasFell: Boolean) {
        viewModelScope.launch {
            try {
                // Obstacle actuel (obtenu depuis le StateFlow)
                val currentObstacle = _currentObstacle.value ?: return@launch

                // Créer l'objet PerformanceObstacleCreate
                val performance = PerformanceObstacleCreate(
                    obstacleId = currentObstacle.id,
                    performanceId = 1, // Remplace par l'ID de la performance en cours
                    hasFell = if (hasFell) 1 else 0,
                    toVerify = 1,
                    time = time
                )


                // Appeler le repository pour enregistrer la performance
                val response = repository.addPerformanceObstacle(performance)
                if (response.isSuccessful) {
                    response.body()?.let { newPerformance ->
                        _performances.value = _performances.value + newPerformance
                        Log.d("ArbitrationViewModel", "Performance ajoutée : $newPerformance")
                    }
                } else {
                    Log.e(
                        "ArbitrationViewModel",
                        "Erreur lors de l'ajout de la performance : ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur API : ${e.message}")
            }
        }
    }

    fun moveToNextObstacle() {
        val currentIndex = _obstacles.value.indexOf(_currentObstacle.value)

        if(currentIndex in _obstacles.value.indices) {
            _currentObstacle.value = _obstacles.value.getOrNull(currentIndex + 1)
        }
    }
}