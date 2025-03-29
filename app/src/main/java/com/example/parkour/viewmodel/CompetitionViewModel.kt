package com.example.parkour.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.CompetitionCreate
import com.example.parkour.data.model.CompetitionUpdate
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

    private val _competitionDeleted = MutableStateFlow(false)
    val competitionDeleted: StateFlow<Boolean> = _competitionDeleted

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
                Log.d("TAG", "Envoi de la compétition: $competitionCreate")
                repository.addCompetition(competitionCreate)
                _competitions.value = repository.getCompetitions() // Recharger la liste après suppression
                _competitionDeleted.value = true // Déclencher la réinitialisation
            } catch (e: Exception) {
                Log.d("TAG", "Erreur lors de l'ajout: ${e.message}")
            }
        }
    }


    fun deleteCompetition(competitionId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteCompetition(competitionId)
                _competitions.value = repository.getCompetitions() // Recharger la liste après suppression
                _competitionDeleted.value = true // Déclencher la réinitialisation
            } catch (e: Exception) {
                Log.e("TAG", "Erreur lors de la suppression : $e")
            }
        }
    }

    fun resetDeletionState() {
        _competitionDeleted.value = false
    }

    fun updateCompetition(updatedCompetition: CompetitionUpdate) {
        viewModelScope.launch {
            try {
                repository.updateCompetition(updatedCompetition)
                _competitions.value = repository.getCompetitions() // Recharger la liste après mise à jour
            } catch (e: Exception) {
                Log.e("TAG", "Erreur lors de la mise à jour : $e")
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
