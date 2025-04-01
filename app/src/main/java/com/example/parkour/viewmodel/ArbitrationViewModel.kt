package com.example.parkour.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.*
import com.example.parkour.data.model.create.PerformanceObstacleCreate
import com.example.parkour.data.repository.ArbitrationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

class ArbitrationViewModel(private val repository: ArbitrationRepository) : ViewModel() {

    private var timerJob: Job? = null
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private val _competitions = MutableStateFlow<List<Competition>>(emptyList())
    val competitions: StateFlow<List<Competition>> = _competitions

    private val _competitors = MutableStateFlow<List<Competitor>>(emptyList())
    val competitors: StateFlow<List<Competitor>> = _competitors

    private val _obstacles = MutableStateFlow<List<CourseObstacle>>(emptyList())
    val obstacles: StateFlow<List<CourseObstacle>> = _obstacles

    private val _selectedCourseId = MutableStateFlow<Int?>(null)
    val selectedCourseId: StateFlow<Int?> = _selectedCourseId

    private val _selectedCompetitorId = MutableStateFlow<Int?>(null)
    val selectedCompetitorId: StateFlow<Int?> = _selectedCompetitorId

    private val _currentObstacle = MutableStateFlow<CourseObstacle?>(null)
    val currentObstacle: StateFlow<CourseObstacle?> = _currentObstacle

    // Charger les compétitions
    fun loadCompetitions() {
        viewModelScope.launch {
            try {
                val response = repository.getCompetitions()
                if (response.isSuccessful) {
                    Log.d("CompetitionViewModel", "Réponse brute : ${response.body()?.toString()}")
                    _competitions.value = response.body() ?: emptyList()
                } else {
                    Log.e("CompetitionViewModel", "Erreur : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Erreur API :", e)
                _competitions.value = emptyList()  // Si une erreur se produit, vider la liste pour éviter un comportement inattendu.
            }
        }
    }


    // Charger les compétiteurs pour une compétition spécifique
    fun loadCompetitorsForCompetition(competitionId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCompetitorsForCompetition(competitionId)
                if (response.isSuccessful) {
                    _competitors.value = response.body() ?: emptyList()
                } else {
                    Log.e("ArbitrationViewModel", "Erreur lors de la récupération des compétiteurs : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur API : ${e.message}")
            }
        }
    }



    // Charger les obstacles d'un parcours
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

    // Démarrer le chronomètre
    fun startTimer(onTick: (Long) -> Unit) {
        timerJob?.cancel()
        _elapsedTime.value = 0L

        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            while (true) {
                val currentTime = System.currentTimeMillis()
                _elapsedTime.value = currentTime - startTime
                onTick(_elapsedTime.value)
                kotlinx.coroutines.delay(10)
            }
        }
    }

    // Arrêter le chronomètre
    fun stopTimer() {
        timerJob?.cancel()
    }

    // Sélectionner un parcours
    fun selectCourse(courseId: Int) {
        _selectedCourseId.value = courseId
        loadObstacles(courseId)
    }

    // Sélectionner un compétiteur
    fun selectCompetitor(competitorId: Int) {
        _selectedCompetitorId.value = competitorId
    }

    // Enregistrer la performance d'un compétiteur sur un obstacle
    fun registerPerformance(time: Double, hasFell: Boolean) {
        viewModelScope.launch {
            try {
                val currentObstacle = _currentObstacle.value ?: return@launch
                val performance = PerformanceObstacleCreate(
                    obstacleId = currentObstacle.id,
                    performanceId = 1,  // À ajuster avec l'ID réel de la performance
                    hasFell = if (hasFell) 1 else 0,
                    toVerify = 1,
                    time = time
                )
                val response = repository.addPerformanceObstacle(performance)
                if (response.isSuccessful) {
                    Log.d("ArbitrationViewModel", "Performance ajoutée avec succès")
                } else {
                    Log.e("ArbitrationViewModel", "Erreur lors de l'ajout de la performance")
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur API : ${e.message}")
            }
        }
    }

    // Avancer à l'obstacle suivant
    fun moveToNextObstacle() {
        val currentIndex = _obstacles.value.indexOf(_currentObstacle.value)
        if (currentIndex in _obstacles.value.indices && currentIndex < _obstacles.value.size - 1) {
            _currentObstacle.value = _obstacles.value[currentIndex + 1]
        }
    }
}
