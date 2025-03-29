package com.example.parkour.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseObstacle(
    val id: Int,
    @SerialName("obstacle_name") val obstacleName: String,
    val position: Int
)