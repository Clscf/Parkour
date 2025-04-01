package com.example.parkour.repository

import android.util.Log
import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Competitor
import com.example.parkour.data.model.CompetitorIdRequest
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.create.CompetitionCreate
import com.example.parkour.data.model.create.CompetitorCreate
import com.example.parkour.data.model.create.CourseCreate
import com.example.parkour.data.model.uptdate.CompetitionUpdate
import com.example.parkour.network.ApiService
import retrofit2.Response


class CompetitionRepository(private val apiService: ApiService) {

    suspend fun getCompetitions(): Response<List<Competition>> {
        return apiService.getCompetitions()
    }

    suspend fun addCompetition(competitionCreate: CompetitionCreate): Response<Competition> {
        return apiService.addCompetition(competitionCreate)
    }

    suspend fun getCompetition(id: Int): Response<Competition> {
        return apiService.getCompetition(id)
    }

    suspend fun updateCompetition(id: Int, competitionUpdate: CompetitionUpdate): Response<Unit> {
        return apiService.updateCompetition(id, competitionUpdate)
    }



    suspend fun deleteCompetition(id: Int): Response<Unit> {
        return apiService.deleteCompetition(id)
    }

    suspend fun getCompetitionCourses(id: Int): Response<List<Course>> {
        return apiService.getCompetitionCourses(id)
    }

    suspend fun addCourse(courseCreate: CourseCreate): Response<Course> {
        return apiService.addCourse(courseCreate)
    }


    suspend fun getCompetitionCompetitors(id: Int): Response<List<Competitor>>{
        return apiService.getCompetitionCompetitors(id)
    }

    suspend fun addCompetitorToCompetition(competitionId: Int, competitorId: CompetitorIdRequest): Response<Unit> {
        return apiService.addCompetitorToCompetition(competitionId, competitorId)
    }

    suspend fun removeCompetitorFromCompetition(competitionId: Int, competitorId: Int ): Response<Unit>{
        return apiService.removeCompetitorFromCompetition(competitionId, competitorId)
    }

    suspend fun getCourse(): Response<List<Course>> {
        return apiService.getCourses()

    }


}
