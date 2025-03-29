package com.example.parkour.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.CompetitionCreate
import com.example.parkour.data.model.Competitions
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.repository.CompetitionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompetitionViewModel(private val repository: CompetitionRepository) : ViewModel() {

    private val _competitions = MutableStateFlow<List<Competitions>>(emptyList())
    val competitions: StateFlow<List<Competitions>> = _competitions

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private val _obstacles = MutableStateFlow<List<Obstacle>>(emptyList())
    val obstacles: StateFlow<List<Obstacle>> = _obstacles

    private val _selectedCourseId = MutableStateFlow<Int?>(null)
    val selectedCourseId: StateFlow<Int?> = _selectedCourseId

    init {
        loadCompetitions()
        //loadCourses()
    }

    fun loadCompetitions() {
        viewModelScope.launch {
            try {
                _competitions.value = repository.getCompetitions() // Assurez-vous que cette méthode renvoie une List<Competitions>
            } catch (e: Exception) {
                // Gérer les erreurs
            }
        }
    }

    fun addCompetition(competitionCreate: CompetitionCreate) {
        viewModelScope.launch {
            try {
                val addedCompetition = repository.addCompetition(competitionCreate)
            } catch (e: Exception) {
                println("pas creer")
            }
        }
    }

  /*  fun loadCourses() {
        viewModelScope.launch {
            try {
                _courses.value = repository.getCourses()
            } catch (e: Exception) {
                // Gérer les erreurs
            }
        }
    }

    fun loadObstacles(courseId: Int) {
        _selectedCourseId.value = courseId
        viewModelScope.launch {
            try {
                _obstacles.value = repository.getObstaclesForCourse(courseId)
            } catch (e: Exception) {
                // Gérer les erreurs
            }
        }
    }*/
}
