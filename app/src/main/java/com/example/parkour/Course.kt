package com.example.parkour

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val name: String,
    @SerialName("max_duration") val maxDuration: Int,
    val position: Int,
    @SerialName("is_over") val isOver: Int,
    @SerialName("competition_id") val competitionId: Int,
    var isSelected: Boolean = false
)
