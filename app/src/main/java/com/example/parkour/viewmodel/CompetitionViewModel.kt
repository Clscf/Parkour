package com.example.parkour.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.create.CompetitionCreate
import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.Obstacle
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

    init {
        loadCompetitions()
        //loadCourses()
    }

    fun loadCompetitions() {
        viewModelScope.launch {
            try {
                val response = repository.getCompetitions()
                if (response.isSuccessful) {
                    _competitions.value = response.body() ?: emptyList()
                } else {
                    Log.e("CompetitionViewModel", "Erreur API : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception : ${e.message}")
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

                        // Ajouter la nouvelle compétition à la liste actuelle sans recharger toutes les compétitions
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

}
