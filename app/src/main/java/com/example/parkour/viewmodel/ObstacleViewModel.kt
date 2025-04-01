package com.example.parkour.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.parkour.data.model.CourseObstacle
import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.model.create.ObstacleCreate
import com.example.parkour.repository.ObstacleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ObstacleViewModel(private val repository: ObstacleRepository) : ViewModel() {
    val courseObstacles = MutableStateFlow<List<CourseObstacle>>(emptyList())
    val obstacles = MutableStateFlow<List<Obstacle>>(emptyList())

    // Méthode pour charger tous les obstacles
    fun loadAllObstacles() {
        viewModelScope.launch {
            try {
                val response = repository.getObstacles()
                if (response.isSuccessful) {
                    obstacles.value = response.body() ?: emptyList()
                } else {
                    Log.e("ObstacleViewModel", "Erreur API: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ObstacleViewModel", "Erreur lors de la récupération des obstacles: ${e.message}")
            }
        }
    }

    // Méthode pour charger les obstacles existants
    fun loadObstaclesForCourse(courseId: Int) {
        viewModelScope.launch {
            val obstacles = repository.getObstaclesForCourse(courseId)
            courseObstacles.value = obstacles
        }
    }

    // Créer un nouvel obstacle
    fun createObstacle(obstacleCreate: ObstacleCreate, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Appel API pour créer l'obstacle
                val response = repository.createObstacle(obstacleCreate)
                if (response.isSuccessful) {
                    // Callback pour signaler la réussite
                    onSuccess()
                } else {
                    // Gestion des erreurs API
                    onFailure("Erreur API: ${response.message()}")
                }
            } catch (e: Exception) {
                // Erreur lors de la requête réseau
                Log.e("ObstacleViewModel", "Erreur lors de la création de l'obstacle: ${e.message}", e)
                onFailure("Erreur réseau: ${e.message}")
            }
        }
    }

}