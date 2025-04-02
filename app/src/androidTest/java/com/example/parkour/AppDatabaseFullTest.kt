package com.example.parkour

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.parkour.data.database.AppDatabase
import com.example.parkour.data.database.dao.*
import com.example.parkour.data.model.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*


class AppDatabaseFullTest {

    private lateinit var database: AppDatabase
    private lateinit var competitionDao: CompetitionDao
    private lateinit var courseDao: CourseDao
    private lateinit var obstacleDao: ObstacleDao
    private lateinit var competitorDao: CompetitorDao
    private lateinit var performanceDao: PerformanceDao
    private lateinit var courseObstacleDao: CourseObstacleDao

    @BeforeEach
    fun setup() {
        // Configure la base de données en mémoire
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        competitionDao = database.competitionDao()
        courseDao = database.courseDao()
        obstacleDao = database.obstacleDao()
        competitorDao = database.competitorDao()
        performanceDao = database.performanceDao()
        courseObstacleDao = database.courseObstacleDao()
    }

    @AfterEach
    fun teardown() {
        // Fermer la base après chaque test
        database.close()
    }

    @Test
    fun insertAndRetrieveCompetition() = runBlocking {
        // Création d'une instance de Competition
        val competition = Competition(
            id = 1,
            createdAt = "2025-03-30T12:00:00",
            updatedAt = "2025-03-30T12:00:00",
            name = "Parkour Championship",
            ageMin = 18,
            ageMax = 35,
            gender = "mixed",
            hasTry = 1,
            status = "ongoing"
        )

        // Insertion de la compétition dans la base
        competitionDao.insertCompetition(competition)

        // Récupération de la compétition à partir de son ID
        val retrievedCompetition = competitionDao.getCompetitionById(1)

        // Vérification des valeurs récupérées
        assertEquals("Parkour Championship", retrievedCompetition?.name)
        assertEquals(18, retrievedCompetition?.ageMin)
        assertEquals(35, retrievedCompetition?.ageMax)
        assertEquals("mixed", retrievedCompetition?.gender)
        assertEquals(1, retrievedCompetition?.hasTry)
        assertEquals("ongoing", retrievedCompetition?.status)
        assertEquals("2025-03-30T12:00:00", retrievedCompetition?.createdAt)
        assertEquals("2025-03-30T12:00:00", retrievedCompetition?.updatedAt)
    }

    @Test
    fun insertAndRetrieveCourse() = runBlocking {
        // Création d'une instance de Course
        val course = Course(
            id = 1,
            createdAt = "2025-03-30T12:00:00",
            updatedAt = "2025-03-30T12:00:00",
            name = "Obstacle Run",
            maxDuration = 120,
            position = 1,
            isOver = 0,
            competitionId = 1
        )

        // Insertion du parcours dans la base de données
        courseDao.insertCourse(course)

        // Récupération du parcours à partir de l'ID
        val retrievedCourse = courseDao.getCourseById(1)

        // Vérifications des valeurs récupérées
        assertEquals("Obstacle Run", retrievedCourse?.name)
        assertEquals(120, retrievedCourse?.maxDuration)
        assertEquals(0, retrievedCourse?.isOver)
        assertEquals(1, retrievedCourse?.competitionId)
    }

    @Test
    fun insertAndRetrieveCompetitor() = runBlocking {
        val competitor = Competitor(
            id = 1,
            createdAt = "2025-03-30T12:00:00",
            updatedAt = "2025-03-30T12:00:00",
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            gender = "male",
            phone = "1234567890",
            bornAt = "2000-01-01"
        )

        competitorDao.insertCompetitor(competitor)

        val retrievedCompetitor = competitorDao.getCompetitorById(1)
        assertEquals("John", retrievedCompetitor?.firstName)
        assertEquals("Doe", retrievedCompetitor?.lastName)
    }

    @Test
    fun insertAndRetrieveObstacle() = runBlocking {
        // Création d'une instance d'obstacle
        val obstacle = Obstacle(
            id = 1,
            createdAt = "2025-03-30T12:00:00",
            updatedAt = "2025-03-30T12:00:00",
            name = "Wall Jump"
        )

        // Insertion de l'obstacle dans la base de données
        obstacleDao.insertObstacle(obstacle)

        // Récupération de l'obstacle à partir de son ID
        val retrievedObstacle = obstacleDao.getObstacleById(1)

        // Vérification des valeurs récupérées
        assertEquals("Wall Jump", retrievedObstacle?.name)
        assertEquals("2025-03-30T12:00:00", retrievedObstacle?.createdAt)
        assertEquals("2025-03-30T12:00:00", retrievedObstacle?.updatedAt)
    }

    @Test
    fun deleteCompetitor() = runBlocking {
        val competitor = Competitor(
            id = 1,
            createdAt = "2025-03-30T12:00:00",
            updatedAt = "2025-03-30T12:00:00",
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            gender = "male",
            phone = "1234567890",
            bornAt = "2000-01-01"
        )

        competitorDao.insertCompetitor(competitor)
        competitorDao.deleteCompetitor(1)

        val retrievedCompetitor = competitorDao.getCompetitorById(1)
        assertNull(retrievedCompetitor)
    }
}
