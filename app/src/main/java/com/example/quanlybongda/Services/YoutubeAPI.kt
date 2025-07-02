package com.example.quanlybongda.Services

import android.os.Build
import com.example.quanlybongda.BuildConfig
import com.example.quanlybongda.Services.Data.SearchPlayerResponse
import com.example.quanlybongda.Services.Data.YouTubeSearchResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
private const val API_KEY = BuildConfig.YOUTUBE_DATA_API_KEY;

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .client(OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .build())
    .build()

interface YoutubeAPIService {
    @GET("search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("part") part: String = "snippet",
        @Query("key") apiKey: String = API_KEY,
        @Query("maxResults") maxResults: Int = 10
    ): YouTubeSearchResponse
}


object YoutubeAPI {
    val retrofitService : YoutubeAPIService by lazy {
        retrofit.create(YoutubeAPIService::class.java)
    }
}
