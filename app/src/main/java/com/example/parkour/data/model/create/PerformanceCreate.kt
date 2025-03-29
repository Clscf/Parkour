package com.example.parkour.data.model.create

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceCreate(
    @SerialName("competitor_id") val competitorId: Int,
    @SerialName("course_id") val courseId: Int,
    val status: String,
    @SerialName("total_time") val totalTime: Double
)