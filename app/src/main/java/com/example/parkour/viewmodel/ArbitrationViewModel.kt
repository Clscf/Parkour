package com.example.parkour.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.*
import com.example.parkour.data.model.create.PerformanceCreate
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

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private val _obstacles = MutableStateFlow<List<CourseObstacle>>(emptyList())
    val obstacles: StateFlow<List<CourseObstacle>> = _obstacles

    private val _selectedCourseId = MutableStateFlow<Int?>(null)
    val selectedCourseId: StateFlow<Int?> = _selectedCourseId

    private val _selectedCompetitorId = MutableStateFlow<Int?>(null)
    val selectedCompetitorId: StateFlow<Int?> = _selectedCompetitorId

    private val _currentObstacle = MutableStateFlow<CourseObstacle?>(null)
    val currentObstacle: StateFlow<CourseObstacle?> = _currentObstacle

    private val _performanceObstacles = MutableStateFlow<List<PerformanceObstacle>>(emptyList())
    val performanceObstacles: StateFlow<List<PerformanceObstacle>> = _performanceObstacles

    private var _performanceId: Int? = null

    // Charger les compétitions
    fun loadCompetitions() {
        viewModelScope.launch {
            try {
                val response = repository.getCompetitions()
                if (response.isSuccessful) {
                    val competitionsList = response.body() ?: emptyList()
                    Log.d("ArbitrationViewModel", "Compétitions récupérées: $competitionsList")
                    _competitions.value = competitionsList
                } else {
                    Log.e("ArbitrationViewModel", "Erreur API : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur API : ${e.message}")
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
                    _currentObstacle.value = _obstacles.value[0]
                } else {
                    Log.e("ArbitrationViewModel", "Erreur API : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur API : ${e.message}")
            }
        }
    }

    fun loadCoursesForCompetition(competitionId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCompetitionCourses(competitionId)
                if (response.isSuccessful) {
                    _courses.value = response.body() ?: emptyList()
                } else {
                    Log.e("CompetitionViewModel", "Erreur lors de la récupération des courses : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Erreur lors de la récupération des courses : ${e.message}")
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
        _selectedCourseId.value?.let { courseId ->
            loadObstacles(courseId) }
    }

    // Enregistrer la performance d'un compétiteur sur un obstacle

    // Avancer à l'obstacle suivant
    fun moveToNextObstacle(elapsedTime: Long) {
        val currentObstacle = _currentObstacle.value ?: return
        val currentIndex = _obstacles.value.indexOf(currentObstacle)

        // Enregistrer la performance pour l'obstacle actuel
        registerPerformance(elapsedTime.toInt() / 1000, hasFell = false) // Convertir ms en secondes

        if (currentIndex < _obstacles.value.size - 1) {
            // Passer à l'obstacle suivant
            _currentObstacle.value = _obstacles.value[currentIndex + 1]
        } else {
            // Dernier obstacle - créer la performance finale
            createFinalPerformance()
        }
    }

    fun createFinalPerformance(status: String = "to_finish") {
        viewModelScope.launch {
            try {
                val competitorId = _selectedCompetitorId.value ?: return@launch
                val courseId = _selectedCourseId.value ?: return@launch
                val totalTime = _performanceObstacles.value.sumOf { it.time }

                val performance = PerformanceCreate(
                    competitorId = competitorId,
                    courseId = courseId,
                    status = status,
                    totalTime = totalTime
                )

                val response = repository.createPerformance(performance)
                if (response.isSuccessful) {
                    _performanceId = response.body()?.id
                    Log.d("ArbitrationViewModel", "Performance enregistrée avec statut : $status")
                } else {
                    Log.e("ArbitrationViewModel", "Erreur API : ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur API : ${e.message}")
            }
        }
    }


    fun registerPerformance(time: Int, hasFell: Boolean) {
        viewModelScope.launch {
            try {
                val currentObstacle = _currentObstacle.value ?: return@launch
                val performanceObstacle = PerformanceObstacleCreate(
                    obstacleId = currentObstacle.id,
                    performanceId = _performanceId ?: 0, // 0 si pas encore créé
                    hasFell = if (hasFell) 1 else 0,
                    toVerify = 1,
                    time = time
                )

                // Stocker localement
                _performanceObstacles.value += PerformanceObstacle(
                    id = 0, // temporaire
                    obstacleId = currentObstacle.id,
                    performanceId = _performanceId ?: 0,
                    hasFell = if (hasFell) 1 else 0,
                    toVerify = 1,
                    time = time,
                    createdAt = "",
                    updatedAt = ""
                )

                // Envoyer au serveur si performanceId existe
                if (_performanceId != null) {
                    val response = repository.addPerformanceObstacle(performanceObstacle)
                    if (response.isSuccessful) {
                        Log.d("ArbitrationViewModel", "PerformanceObstacle ajoutée avec succès")
                    }
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur : ${e.message}")
            }
        }
    }

    fun loadCourseForCompetition(competitionId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCourseForCompetition(competitionId)
                if (response.isSuccessful) {
                    val courses = response.body() ?: emptyList()
                    if (courses.isNotEmpty()) {
                        val selectedCourse = courses.first()
                        _selectedCourseId.value = selectedCourse.id
                        //loadObstacles(selectedCourse.id)
                    } else {
                        Log.e("ArbitrationViewModel", "Aucun parcours trouvé pour cette compétition")
                    }
                } else {
                    Log.e("ArbitrationViewModel", "Erreur lors de la récupération des parcours")
                }
            } catch (e: Exception) {
                Log.e("ArbitrationViewModel", "Erreur API : ${e.message}")
            }
        }
    }


}
