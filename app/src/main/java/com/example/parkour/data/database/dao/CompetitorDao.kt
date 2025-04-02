package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.Competitor
import com.example.parkour.data.model.SyncEntity

@Dao
interface CompetitorDao {
    @Insert
    fun insertCompetitor(competitor: Competitor)

    @Query("SELECT * FROM Competitor WHERE id = :competitorId")
    fun getCompetitorById(competitorId: Int): Competitor?

    @Query("SELECT * FROM Competitor")
    fun getAllCompetitors(): List<Competitor>

    @Delete
    fun deleteCompetitor(competitor: Competitor)
}