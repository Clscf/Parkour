package com.example.parkour.data.model

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable @Entity
data class CourseObstacle(
    val id: Int,
    @SerialName("obstacle_name") val obstacleName: String,
    val position: Int
)