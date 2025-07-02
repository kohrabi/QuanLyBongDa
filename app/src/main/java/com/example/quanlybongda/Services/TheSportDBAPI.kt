package com.example.quanlybongda.Services

import android.util.Log
import android.widget.Toast
import com.example.quanlybongda.MainActivity
import com.example.quanlybongda.Services.Data.SearchPlayerResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/123/"

private val requestRetryInterceptor = Interceptor { chain ->
    val request = chain.proceed(chain.request());
    if (request.code == 429) {
        val resetTime = request.header("retry-after")?.toLongOrNull() ?: 60L

        Log.wtf("TheSportsDBAPI", "Request limit reached. Waiting for $resetTime seconds to reset.");
        Log.d("TheSportsDBAPI", request.toString());
        request.close();
        Thread.sleep(resetTime * 1000);
        return@Interceptor chain.proceed(chain.request().newBuilder().build());
    }
    return@Interceptor request;
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logInterceptor)
    .addInterceptor(requestRetryInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
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
