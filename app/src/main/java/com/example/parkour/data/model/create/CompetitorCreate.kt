package com.example.parkour.data.model.create

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompetitorCreate(
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    val email: String,
    val phone: String,
    val gender: String,
    @SerialName("born_at") val bornAt: String
)