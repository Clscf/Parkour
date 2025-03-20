import com.example.parkour.Course

import retrofit2.http.GET

interface CourseApiService {
    @GET("courses")
    suspend fun getCourses(): List<Course>
}
