package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.Competition

@Dao
interface CompetitionDao {
    @Insert
    suspend fun insertCompetition(competition: Competition)

    @Query("SELECT * FROM Competition WHERE id = :competitionId")
    suspend fun getCompetitionById(competitionId: Int): Competition?

    @Query("SELECT id, createdAt, updatedAt, name, ageMin, ageMax, gender, hasTry, status FROM Competition")
    suspend fun getAllCompetitions(): List<Competition>
}