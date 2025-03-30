package com.example.parkour.data.database.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.Competitor

interface CompetitorDao {
    @Insert
    suspend fun insertCompetitor(competitor: Competitor)

    @Query("SELECT * FROM Competitor WHERE id = :competitorId")
    suspend fun getCompetitorById(competitorId: Int): Competitor?

    @Query("SELECT * FROM Competitor")
    suspend fun getAllCompetitors(): List<Competitor>
}