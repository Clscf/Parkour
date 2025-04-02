package com.example.parkour.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.create.CompetitionCreate
import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Competitor
import com.example.parkour.data.model.CompetitorIdRequest
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.model.create.CompetitorCreate
import com.example.parkour.data.model.create.CourseCreate
import com.example.parkour.data.model.uptdate.CompetitionUpdate
import com.example.parkour.repository.CompetitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CompetitionViewModel(private val repository: CompetitionRepository) : ViewModel() {

    private val _competitions = MutableStateFlow<List<Competition>>(emptyList())
    val competitions: StateFlow<List<Competition>> = _competitions

    private val _competitors = MutableStateFlow<List<Competitor>>(emptyList())
    val competitors: StateFlow<List<Competitor>> = _competitors

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private val _obstacles = MutableStateFlow<List<Obstacle>>(emptyList())
    val obstacles: StateFlow<List<Obstacle>> = _obstacles

    private val _selectedCourseId = MutableStateFlow<Int?>(null)
    val selectedCourseId: StateFlow<Int?> = _selectedCourseId

    private val _competitionDeleted = MutableStateFlow(false)
    val competitionDeleted: StateFlow<Boolean> = _competitionDeleted

    private val _selectedCompetition = MutableStateFlow<Competition?>(null)
    val selectedCompetition: StateFlow<Competition?> = _selectedCompetition

    init {
        loadCompetitions()
        //loadCourses()
    }

    fun loadCompetitions() {
        viewModelScope.launch {
            try {
                val response = repository.getCompetitions()
                if (response.isSuccessful) {
                    Log.d("API_RESPONSE", "Réponse brute : ${response.body()?.toString()}")
                    _competitions.value = response.body() ?: emptyList()
                } else {
                    Log.e("CompetitionViewModel", "Erreur : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Erreur API :", e)
            }
        }
    }

    fun loadCourses() {
        viewModelScope.launch {
            try {
                val response = repository.getCourse()
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

    fun addCompetition(competitionCreate: CompetitionCreate) {
        viewModelScope.launch {
            try {
                val response = repository.addCompetition(competitionCreate)

                if (response.isSuccessful) {
                    response.body()?.let { newCompetition ->
                        Log.d("CompetitionViewModel", "Compétition ajoutée avec succès : $newCompetition")
                        _competitions.value = _competitions.value + newCompetition
                    }
                } else {
                    Log.e("CompetitionViewModel", "Échec de l'ajout de la compétition : ${response.code()} - ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Erreur lors de l'ajout de la compétition : ${e.message}")
            }
        }
    }


    fun deleteCompetition(competitionId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.deleteCompetition(competitionId)
                if (response.isSuccessful) {
                    _competitions.value = _competitions.value.filterNot { it.id == competitionId }
                    _competitionDeleted.value = true
                    Log.d("CompetitionViewModel", "Compétition supprimée avec succès")
                } else {
                    Log.e("CompetitionViewModel", "Erreur lors de la suppression de la compétition : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Erreur lors de la suppression de la compétition : ${e.message}")
            }
        }
    }

    fun resetDeletionState() {
        _competitionDeleted.value = false
    }

    fun updateCompetition(competitionId: Int, updatedCompetition: CompetitionUpdate) {
        viewModelScope.launch {
            Log.d("CompetitionViewModel", "Envoi de la mise à jour : $updatedCompetition")


            try {

                val response = repository.updateCompetition(competitionId, updatedCompetition)
                if (response.isSuccessful) {
                    Log.d("CompetitionViewModel", "Compétition mise à jour avec succès")
                    loadCompetitions()
                } else {
                    Log.e("CompetitionViewModel", "Erreur lors de la mise à jour : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Erreur lors de la mise à jour : ${e.message}")            }
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

    fun addCourse(courseCreate: CourseCreate) {
        viewModelScope.launch {
            try {
                val response = repository.addCourse(courseCreate)
                if (response.isSuccessful) {
                    Log.d("CompetitionViewModel", "Parcours ajouté avec succès : ${response.body()}")
                    _courses.value += (response.body() ?: return@launch)
                } else {
                    Log.e("CompetitionViewModel", "Erreur lors de l'ajout du parcours : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception lors de l'ajout du parcours : ${e.message}")
            }
        }
    }




    fun loadCompetitionCompetitors(competitionId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCompetitionCompetitors(competitionId)
                if (response.isSuccessful) {
                    _competitors.value = response.body() ?: emptyList()
                    Log.d("CompetitionViewModel", "Compétiteurs chargés avec succès")
                } else {
                    Log.e("CompetitionViewModel", "Erreur : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Erreur API :", e)
            }
        }
    }

    fun addCompetitorToCompetition(competitionId: Int, competitor: Competitor) {
        viewModelScope.launch {
            try {
                Log.d("CompetitionViewModel", "Tentative d'ajout : competitionId=$competitionId, competitor=$competitor")
                val response = repository.addCompetitorToCompetition(competitionId, CompetitorIdRequest(competitor.id))
                if (response.isSuccessful) {
                    Log.d("CompetitionViewModel", "Compétiteur ajouté avec succès")
                    loadCompetitionCompetitors(competitionId)
                } else {
                    Log.e("CompetitionViewModel", "Erreur lors de l'ajout du compétiteureeee : ${response.code()} - ${response.message()}")                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Erreur lors de l'ajout du compétiteur : ${e.message}")
            }
        }
    }

    fun removeCompetitorFromCompetition(competitionId: Int, competitorId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.removeCompetitorFromCompetition(competitionId, competitorId)
                if (response.isSuccessful) {
                    loadCompetitionCompetitors(competitionId)
                    Log.d("CompetitionViewModel", "Compétiteur supprimé avec succès")
                } else {
                    Log.e("CompetitionViewModel", "Erreur lors de la suppression du compétiteur : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Erreur lors de la suppression du compétiteur : ${e.message}")
            }
        }
    }



    fun getCompetitionById(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCompetition(id)
                if (response.isSuccessful) {
                    _selectedCompetition.value = response.body()
                } else {
                    // Gérer les erreurs ici
                    println("Erreur: ${response.message()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }
    }


        private val _isEditing = MutableStateFlow(false)
        val isEditing: StateFlow<Boolean> get() = _isEditing

        fun setEditing(editing: Boolean) {
            _isEditing.value = editing
        }













}
