package com.example.quanlybongda.Services

import com.example.quanlybongda.BuildConfig
import com.example.quanlybongda.Services.Converters.LocalDateConverter
import com.example.quanlybongda.Services.Converters.LocalDateTimeConverter
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.time.LocalDate
import java.time.LocalDateTime


private const val BASE_URL = "https://api.football-data.org/v4/"
private const val API_KEY = BuildConfig.FOOTBALL_DATA_API_KEY;

private val authInterceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("X-Auth-Token", API_KEY)
        .build()
    chain.proceed(request)
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .build()

val gson = GsonBuilder()
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeConverter())
    .registerTypeAdapter(LocalDate::class.java, LocalDateConverter())
    .create()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface FootballAPIService {
    @GET("competitions/")
    suspend fun getCompetitions() : JsonObject
}

object FootballAPI {
    val retrofitService : FootballAPIService by lazy {
        retrofit.create(FootballAPIService::class.java)
    }
}
