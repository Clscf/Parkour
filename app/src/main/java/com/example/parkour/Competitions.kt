package com.example.parkour

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Competitions(
    val id: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val name: String,
    @SerialName("age_min") val ageMin: Int,
    @SerialName("age_max") val ageMax: Int,
    val gender: Char,
    @SerialName("has_retry") val hasTry: Int,
    val status: CompetitionStatus
)

enum class CompetitionStatus {
    not_ready, not_started, started, finished
}



