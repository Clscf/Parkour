package com.example.parkour.data.model.create

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceObstacleCreate(
    @SerialName("obstacle_id") val obstacleId: Int,
    @SerialName("performance_id") val performanceId: Int,
    @SerialName("has_fell") val hasFell: Int,
    @SerialName("to_verify") val toVerify: Int,
    val time: Int
)