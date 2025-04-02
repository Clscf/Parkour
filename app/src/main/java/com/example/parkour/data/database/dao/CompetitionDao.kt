package com.example.parkour.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parkour.data.model.Competition

@Dao
interface CompetitionDao {
    @Insert
    fun insertCompetition(competition: Competition)

    @Query("SELECT * FROM Competition WHERE id = :competitionId")
    fun getCompetitionById(competitionId: Int): Competition?

    @Query("SELECT * FROM Competition")
    fun getAllCompetitions(): List<Competition>

}