package com.example.parkour.data.model.uptdate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompetitorUpdate(
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    val email: String,
    val phone: String,
    @SerialName("born_at") val bornAt: String
)