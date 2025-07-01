package com.example.quanlybongda.Services

import com.example.quanlybongda.Services.Data.SearchPlayerResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/123/"

//private val authInterceptor = Interceptor { chain ->
//    val request = chain.request().newBuilder()
//        .addHeader("X-API-KEY", "123")
//        .build()
//    chain.proceed(request)
//}
//
//private val okHttpClient = OkHttpClient.Builder()
//    .addInterceptor(authInterceptor)
//    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
//    .client(okHttpClient)
    .build()

interface TheSportsDBAPIService {
    @GET("searchplayers.php")
    suspend fun searchPlayer(@Query("p") p: String) : Response<SearchPlayerResponse>;
}


object TheSportsDBAPI {
    val retrofitService : TheSportsDBAPIService by lazy {
        retrofit.create(TheSportsDBAPIService::class.java)
    }
}
