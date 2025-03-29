package com.example.parkour.data.model;

import kotlinx.serialization.SerialName;
import kotlinx.serialization.Serializable;

@Serializable
data class CompetitionUpdate (
    val id: Int,
    val name: String,
    @SerialName("age_min") val ageMin: Int,
    @SerialName("age_max") val ageMax: Int,
    val gender: String,
    @SerialName("has_retry") val hasRetry: Int
)

