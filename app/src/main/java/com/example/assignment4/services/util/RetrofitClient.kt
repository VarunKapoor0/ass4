package com.example.assignment4.services.util

import com.example.assignment4.services.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://assignment-3-419100.wl.r.appspot.com/"

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(0, TimeUnit.SECONDS)  // Set the connection timeout
        .readTimeout(0, TimeUnit.SECONDS)     // Set the read timeout
        .writeTimeout(0, TimeUnit.SECONDS)    // Set the write timeout
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}