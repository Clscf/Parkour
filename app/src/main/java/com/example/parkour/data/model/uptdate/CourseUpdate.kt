package com.example.parkour.data.model.uptdate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseUpdate(
    val name: String,
    @SerialName("max_duration") val maxDuration: Int,
    val position: Int,
    @SerialName("is_over") val isOver: Int,
    @SerialName("competition_id") val competitionId: Int
)