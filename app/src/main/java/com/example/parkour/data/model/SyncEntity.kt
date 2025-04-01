package com.example.parkour.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable @Entity
data class SyncEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entityType: String,
    val entityId: Int,
    val action: String
)