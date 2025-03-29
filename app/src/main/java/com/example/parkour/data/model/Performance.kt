package com.example.parkour.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Performance(
    val id: Int,
    @SerialName("competitor_id") val competitorId: Int,
    @SerialName("course_id") val courseId: Int,
    val status: String,
    @SerialName("total_time") val totalTime: Double,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)