package com.example.parkour.repository

import com.example.parkour.data.model.Performance
import com.example.parkour.data.model.create.PerformanceCreate
import com.example.parkour.data.model.uptdate.PerformanceUpdate
import com.example.parkour.network.ApiService
import retrofit2.Response

class PerformanceRepository(private val apiService: ApiService) {

    suspend fun getPerformances(): Response<List<Performance>> {
        return apiService.getPerformances()
    }

    suspend fun addPerformance(performanceCreate: PerformanceCreate): Response<Performance> {
        return apiService.addPerformance(performanceCreate)
    }

    suspend fun getPerformance(id: Int): Response<Performance> {
        return apiService.getPerformance(id)
    }

    suspend fun updatePerformance(id: Int, performanceUpdate: PerformanceUpdate): Response<Performance> {
        return apiService.updatePerformance(id, performanceUpdate)
    }

    suspend fun deletePerformance(id: Int): Response<Unit> {
        return apiService.deletePerformance(id)
    }
}
