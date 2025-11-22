package com.example.lab4.repository

import android.content.Context
import com.example.lab4.model.CreateScheduleRequest
import com.example.lab4.model.HabitResponse
import com.example.lab4.model.ScheduleResponse
import com.example.lab4.network.RetrofitClient
import retrofit2.Response

class ScheduleRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun getScheduleByDay(day: String): List<ScheduleResponse> {
        return api.getScheduleByDay(day)
    }

    suspend fun getHabits(): List<HabitResponse> {
        return api.getAllHabits()
    }

    suspend fun createSchedule(request: CreateScheduleRequest): Response<List<ScheduleResponse>> {
        return api.createSchedule(request)
    }
}