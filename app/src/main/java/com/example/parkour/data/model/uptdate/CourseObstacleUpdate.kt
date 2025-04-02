package com.example.parkour.data.model.uptdate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseObstacleUpdate(
    @SerialName("obstacle_id") val obstacleId: Int,
    val position: Int
)