package com.example.lab4.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import java.time.LocalDateTime
import com.example.lab4.utils.LocalDateTimeAdapter
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"
    @RequiresApi(Build.VERSION_CODES.O)
    fun getInstance(context: Context): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java,
                LocalDateTimeAdapter())
            .create()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }


    
}
