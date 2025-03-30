package com.example.parkour.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.data.model.Competitor
import com.example.parkour.data.model.create.CompetitorCreate
import com.example.parkour.repository.CompetitorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompetitorViewModel(private val repository: CompetitorRepository) : ViewModel() {

    private val _competitors = MutableStateFlow<List<Competitor>>(emptyList())
    val competitors: StateFlow<List<Competitor>> = _competitors

    init {
        loadCompetitors()
    }

    fun loadCompetitors() {
        viewModelScope.launch {
            try {
                val response = repository.getCompetitors()
                if (response.isSuccessful) {
                    _competitors.value = response.body() ?: emptyList()
                } else {
                    Log.e("CompetitorViewModel", "Erreur API : ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitorViewModel", "Erreur API : ${e.message}")
            }
        }
    }

    fun addCompetitor(competitorCreate: CompetitorCreate) {
        viewModelScope.launch {
            try {
                val response = repository.addCompetitor(competitorCreate)
                if (response.isSuccessful) {
                    response.body()?.let { newCompetitor ->
                        _competitors.value = _competitors.value + newCompetitor
                    }
                } else {
                    Log.e("CompetitorViewModel", "Erreur lors de l'ajout : ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CompetitorViewModel", "Erreur lors de l'ajout : ${e.message}")
            }
        }
    }
}
