package com.example.parkour.data.model.create

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseCreate(
    val name: String,
    @SerialName("max_duration") val maxDuration: Int,
    @SerialName("competition_id") val competitionId: Int
)