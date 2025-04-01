package com.example.parkour.data.model

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable @Entity
data class CompetitorIdRequest(
    @SerialName("competitor_id") val competitor_id: Int,
)





