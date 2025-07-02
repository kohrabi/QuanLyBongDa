package com.example.quanlybongda.Services

import android.util.Log
import android.widget.Toast
import com.example.quanlybongda.BuildConfig
import com.example.quanlybongda.MainActivity
import com.example.quanlybongda.Services.Converters.LocalDateConverter
import com.example.quanlybongda.Services.Converters.LocalDateTimeConverter
import com.example.quanlybongda.Services.Data.Competition
import com.example.quanlybongda.Services.Data.CompetitionsResponse
import com.example.quanlybongda.Services.Data.MatchResponse
import com.example.quanlybongda.Services.Data.StandingsResponse
import com.example.quanlybongda.Services.Data.TeamResponse
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
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

val logInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val requestRetryInterceptor = Interceptor { chain ->
    val request = chain.proceed(chain.request());
    val headers = request.headers;
    val requestAvailable = headers.find { it.first == "x-requests-available-minute" };
    if (request.code == 429) {
        val resetTime = requestAvailable?.second?.toLongOrNull() ?: 60L;
        MainActivity.mainActivity.runOnUiThread{
            Toast.makeText(MainActivity.mainActivity,
                "Request limit reached. Waiting for $resetTime seconds to reset.", Toast.LENGTH_SHORT).show()
        }

        Log.wtf("FootballAPI", "Request limit reached. Waiting for $resetTime seconds to reset.");
        Log.d("FootballAPI", request.toString());
        request.close();
        Thread.sleep(resetTime * 1000);
        Log.d("FootballAPI", "Retrying request after waiting for $resetTime seconds.");
        return@Interceptor chain.proceed(chain.request().newBuilder().build());
    }
    return@Interceptor request;
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .addInterceptor(logInterceptor)
    .addInterceptor(requestRetryInterceptor)
    .retryOnConnectionFailure(true)
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
    suspend fun getCompetitions() : Response<CompetitionsResponse>;

    @GET("competitions/{id}/teams")
    suspend fun getCompetitionTeams(@Path("id") id: String, @Query("season") season: Int = 2024) : Response<TeamResponse>;

    @GET("competitions/{id}/matches")
    suspend fun getCompetitionMatches(@Path("id") id: String, @Query("season") season: Int = 2024) : Response<MatchResponse>;

    @GET("competitions/{id}/standings")
    suspend fun getCompetitionStandings(@Path("id") id: String, @Query("season") season: Int = 2024) : Response<StandingsResponse>;

    @GET("competitions/{code}")
    suspend fun getCompetition(@Path("code") code: String) : Response<Competition>;
}

object FootballAPI {
    val retrofitService : FootballAPIService by lazy {
        retrofit.create(FootballAPIService::class.java)
    }
}