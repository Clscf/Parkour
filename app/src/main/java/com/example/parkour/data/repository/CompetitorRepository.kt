package com.example.parkour.repository

import com.example.parkour.data.model.Competitor
import com.example.parkour.data.model.create.CompetitorCreate
import com.example.parkour.data.model.uptdate.CompetitorUpdate
import com.example.parkour.network.ApiService
import retrofit2.Response

class CompetitorRepository(private val apiService: ApiService) {

    suspend fun getCompetitors(): Response<List<Competitor>> {
        return apiService.getCompetitors()
    }

    suspend fun addCompetitor(competitorCreate: CompetitorCreate): Response<Competitor> {
        return apiService.addCompetitor(competitorCreate)
    }

    suspend fun getCompetitor(id: Int): Response<Competitor> {
        return apiService.getCompetitor(id)
    }

    suspend fun updateCompetitor(id: Int, competitorUpdate: CompetitorUpdate): Response<Unit> {
        return apiService.updateCompetitor(id, competitorUpdate)
    }

    suspend fun deleteCompetitor(id: Int): Response<Unit> {
        return apiService.deleteCompetitor(id)
    }
}
