package com.example.parkour.data.model

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable @Entity
data class Competition(
    val id: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    val name: String,
    @SerialName("age_min") val ageMin: Int,
    @SerialName("age_max") val ageMax: Int,
    val gender: String,
    @SerialName("has_retry") val hasRetry: Int,
    val status: String

)





