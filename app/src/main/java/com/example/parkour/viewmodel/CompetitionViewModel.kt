package com.example.parkour.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.create.CompetitionCreate
import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.model.uptdate.CompetitionUpdate
import com.example.parkour.repository.CompetitionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompetitionViewModel(private val repository: CompetitionRepository) : ViewModel() {

    private val _competitions = MutableStateFlow<List<Competition>>(emptyList())
    val competitions: StateFlow<List<Competition>> = _competitions

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

    fun addCompetition(competitionCreate: CompetitionCreate) {
        viewModelScope.launch {
            try {
                Log.d("CompetitionViewModel", "Envoi de la compétition : $competitionCreate")

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
                Log.d("Tryt", "Compétition qui rentre dans le try mise à jour avec succès")

                val response = repository.updateCompetition(competitionId, updatedCompetition)
                Log.d("Tryt", "Compétition mise à jour avec succès")



                if (response.isSuccessful) {
                    Log.d("CompetitionViewModel", "Compétition mise à jour avec succès")
                    loadCompetitions() // <-- Recharge la liste depuis l'API
                } else {
                    Log.d("CompetitionViewModel", "Réponse API : ${response.body()}")
                    Log.e("CompetitionViewModel", "Erreur lors de la mise à jour : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {

                _competitions.value.forEach { competition ->
                    Log.d("CompetitionViewModel", "Compétition ID: ${competition.id}, " +
                            "Name: ${competition.name}, " +
                            "date:  ${competition.createdAt} "+
                            "date update:  ${competition.updatedAt} "+
                            "Age Min: ${competition.ageMin}, " +
                            "Age Max: ${competition.ageMax}, " +
                            "Gender: ${competition.gender}, " +
                            "Has Retry: ${competition.hasRetry}, " +
                            "Status: ${competition.status}")
                }

                Log.e("CompetitionViewModel", "Erreur lors de la mise à jour : ${e.message}")            }
        }
    }


}
