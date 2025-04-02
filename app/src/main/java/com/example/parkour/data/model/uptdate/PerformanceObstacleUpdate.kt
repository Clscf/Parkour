package com.example.parkour.data.model.uptdate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceObstacleUpdate(
    @SerialName("has_fell") val hasFell: Int,
    @SerialName("to_verify") val toVerify: Int,
    val time: Int
)