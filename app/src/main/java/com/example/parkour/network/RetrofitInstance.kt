package com.example.parkour.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json

object RetrofitInstance {
    private const val BASE_URL = "http://92.222.217.100/api/"
    private const val AUTH_TOKEN = "gfY4b0jr67qNqH0ecVXO1ciz7x2JhcQrwNk1QtWHYmftD2cTzA0IG92NMvOYlCuN"

    private val authInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $AUTH_TOKEN")
            .build()
        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    // Créez une instance de Json avec ignoreUnknownKeys activé
    private val json = Json {
        ignoreUnknownKeys = true  // Ignore les clés inconnues dans la réponse
        isLenient = true
        coerceInputValues = true
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))  // Utilisez la config Json avec ignoreUnknownKeys
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
