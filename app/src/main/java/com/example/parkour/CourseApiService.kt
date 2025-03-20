import com.example.parkour.Competitions
import com.example.parkour.Course
import com.example.parkour.Obstacle

import retrofit2.http.GET
import retrofit2.http.Path

interface CourseApiService {
    @GET("courses")
    suspend fun getCourses(): List<Course>

    @GET("courses/{id}/obstacles")
    suspend fun getObstaclesForCourse(@retrofit2.http.Path("id") courseId: Int): List<Obstacle>

    @GET("competitions")
    suspend fun getCompetitions(): List<Competitions>

    @GET("competitions/{id}")
    suspend fun getCompetitionById(@Path("id") competitionId: Int): Competitions
}
