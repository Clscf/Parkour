package com.example.parkour.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceObstacle(
    val id: Int,
    @SerialName("obstacle_id") val obstacleId: Int,
    @SerialName("performance_id") val performanceId: Int,
    @SerialName("has_fell") val hasFell: Boolean,
    @SerialName("to_verify") val toVerify: Boolean,
    val time: Double,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)
