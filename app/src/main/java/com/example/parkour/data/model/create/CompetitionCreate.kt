package com.example.parkour.data.model.create

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompetitionCreate(
    val name: String,
    @SerialName("age_min") val ageMin: Int,
    @SerialName("age_max") val ageMax: Int,
    val gender: String,
    @SerialName("has_retry") val hasRetry: Int
)