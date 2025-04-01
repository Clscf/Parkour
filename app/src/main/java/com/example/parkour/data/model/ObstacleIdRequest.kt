package com.example.parkour.data.model

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ObstacleIdRequest(
    @SerialName("obstacle_id") val obstacle_id: Int,
)