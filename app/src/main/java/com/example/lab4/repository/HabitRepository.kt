package com.example.lab4.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lab4.model.CreateHabitRequest
import com.example.lab4.model.HabitCategory
import com.example.lab4.model.HabitResponse
import com.example.lab4.network.RetrofitClient
import retrofit2.Response

class HabitRepository(context: Context) {
    @RequiresApi(Build.VERSION_CODES.O)
    private val api = RetrofitClient.getInstance(context)

    suspend fun getCategories(): List<HabitCategory> {
        return api.getHabitCategories()
    }

    suspend fun createHabit(name: String, description: String, categoryId: Long, goal: String): Response<HabitResponse> {
        val request = CreateHabitRequest(name, description, categoryId, goal)
        return api.createHabit(request)
    }
}