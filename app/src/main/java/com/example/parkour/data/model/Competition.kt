package com.example.parkour.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "Competition")
data class Competition(
    @PrimaryKey val id: Int,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
    val ageMin: Int,
    val ageMax: Int,
    val gender: String,
    val hasRetry: Int,
    val status: String
)
