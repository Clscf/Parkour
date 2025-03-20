import com.example.parkour.Course
import com.example.parkour.Obstacle

import retrofit2.http.GET

interface CourseApiService {
    @GET("courses")
    suspend fun getCourses(): List<Course>

    @GET("courses/{id}/obstacles")
    suspend fun getObstaclesForCourse(@retrofit2.http.Path("id") courseId: Int): List<Obstacle>
}
