package com.example.parkour

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Obstacle(
    val id: Int,
    @SerialName("obstacle_id")
    val obstacleId: Int,
    @SerialName("obstacle_name")
    val obstacleName: String,
    val position: Int,
    var isSelected: Boolean = false
)