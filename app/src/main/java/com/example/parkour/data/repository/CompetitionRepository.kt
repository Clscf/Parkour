package com.example.parkour.data.repository

import androidx.compose.animation.scaleOut
import com.example.parkour.data.model.CompetitionCreate
import com.example.parkour.data.model.Competitions
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.Obstacle
import com.example.parkour.network.ApiService
import retrofit2.Response
import android.util.Log


class CompetitionRepository(private val apiService: ApiService) {

    suspend fun getCompetitions(): List<Competitions> {
        return apiService.getCompetitions()
    }

    suspend fun getCourses(): List<Course> {
        return apiService.getCourses()
    }

    suspend fun getObstaclesForCourse(courseId: Int): List<Obstacle> {
        return apiService.getObstaclesForCourse(courseId)
    }

    suspend fun addCompetition(competition: CompetitionCreate): CompetitionCreate {
        val response: Response<CompetitionCreate> = apiService.addCompetition(competition)

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("La compétition n'a pas pu être ajoutée.")
            Log.d("TAG", "Compétition créée avec succès : $body")
            return body
        } else {
            Log.e("TAG", "Erreur HTTP : ${response.code()} - ${response.errorBody()?.string()}")
            throw Exception("Erreur lors de l'ajout de la compétition : ${response.message()}")
        }
    }
}
