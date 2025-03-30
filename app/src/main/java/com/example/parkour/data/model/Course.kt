package com.example.parkour.data.model

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable @Entity
data class Course(
    val id: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val name: String,
    @SerialName("max_duration") val maxDuration: Int,
    val position: Int,
    @SerialName("is_over") val isOver: Int,
    @SerialName("competition_id") val competitionId: Int
)
