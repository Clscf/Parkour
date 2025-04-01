package com.example.parkour.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class CourseObstacle(
    @PrimaryKey val id: Int,
    @SerialName("obstacle_name") val obstacleName: String,
    val position: Int
)