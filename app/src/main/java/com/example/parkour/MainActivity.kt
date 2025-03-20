package com.example.parkour

import CourseApiService
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkour.ui.theme.ParkourTheme
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import kotlinx.serialization.ExperimentalSerializationApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

fun createCourseApiService(): CourseApiService {
    val baseUrl = "http://92.222.217.100/api/"
    val contentType = "application/json".toMediaType()

    val authToken = "gfY4b0jr67qNqH0ecVXO1ciz7x2JhcQrwNk1QtWHYmftD2cTzA0IG92NMvOYlCuN"
    val authInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $authToken")
            .build()
        chain.proceed(newRequest)
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    return retrofit.create(CourseApiService::class.java)
}



@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val courseApiService = remember { createCourseApiService() }
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("createCompetition") {
            CreateCompetitionScreen(
                courseApiService = courseApiService
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
            Button(onClick = {
                navController.navigate("createCompetition")
            }) {
                Text("Créer une compétition")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun CreateCompetitionScreen(courseApiService: CourseApiService) {
    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var isLoadingCourses by remember { mutableStateOf(true) }
    var errorMessageCourses by remember { mutableStateOf<String?>(null) }
    var selectedCourseId by remember { mutableStateOf<Int?>(null) }
    var obstacles by remember { mutableStateOf<List<Obstacle>>(emptyList()) }
    var isLoadingObstacles by remember { mutableStateOf(false) }
    var errorMessageObstacles by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        isLoadingCourses = true
        try {
            courses = courseApiService.getCourses()
            errorMessageCourses = null
        } catch (e: Exception) {
            errorMessageCourses = "Erreur lors de la récupération des courses : ${e.message}"
            Log.e("CreateCompetitionScreen", "Erreur lors de la récupération des courses", e)
        } finally {
            isLoadingCourses = false
        }
    }
    LaunchedEffect(key1 = selectedCourseId) {
        if (selectedCourseId != null) {
            isLoadingObstacles = true
            try {
                obstacles = courseApiService.getObstaclesForCourse(selectedCourseId!!)
                errorMessageObstacles = null
            } catch (e: Exception) {
                errorMessageObstacles = "Erreur lors de la récupération des obstacles : ${e.message}"
                Log.e("CreateCompetitionScreen", "Erreur lors de la récupération des obstacles", e)
            } finally {
                isLoadingObstacles = false
            }
        } else {
            obstacles = emptyList()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sélectionnez les courses pour la compétition",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            if (isLoadingCourses) {
                Text("Chargement des courses...")
            } else if (errorMessageCourses != null) {
                Text("Erreur : $errorMessageCourses")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(courses) { course ->
                        CourseItem(
                            course = course,
                            onCourseSelected = { isChecked ->
                                course.isSelected = isChecked
                                if (isChecked) {
                                    selectedCourseId = course.id
                                } else {
                                    selectedCourseId = null
                                }
                            }
                        )
                    }
                }
            }

        }
        if (selectedCourseId != null) {
            Text(
                text = "Sélectionnez les obstacles pour la course",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                if (isLoadingObstacles) {
                    Text("Chargement des obstacles...")
                } else if (errorMessageObstacles != null) {
                    Text("Erreur : $errorMessageObstacles")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(obstacles) { obstacle ->
                            ObstacleItem(
                                obstacle = obstacle,
                                onObstacleSelected = { isChecked ->
                                    obstacle.isSelected = isChecked
                                }
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            val selectedCourses = courses.filter { it.isSelected }
            val selectedObstacles = obstacles.filter { it.isSelected }
            // Ajouter ici le code pour traiter les courses sélectionnées
        }) {
            Text("Valider")
        }
    }
}


@Composable
fun CourseItem(course: Course, onCourseSelected: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = course.isSelected,
            onCheckedChange = { isChecked ->
                onCourseSelected(isChecked)
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = course.name)
    }
}

@Composable
fun ObstacleItem(obstacle: Obstacle, onObstacleSelected: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = obstacle.isSelected,
            onCheckedChange = { isChecked ->
                onObstacleSelected(isChecked)
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = obstacle.obstacleName)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        MainScreen()
    }
}