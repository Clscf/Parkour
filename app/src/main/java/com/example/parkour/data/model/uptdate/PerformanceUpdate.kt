package com.example.parkour.data.model.uptdate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceUpdate(
    val status: String,
    @SerialName("total_time") val totalTime: Int
)