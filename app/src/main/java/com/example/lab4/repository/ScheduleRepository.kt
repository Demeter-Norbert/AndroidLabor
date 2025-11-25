package com.example.lab4.repository

import android.content.Context
import com.example.lab4.model.CreateScheduleRequest
import com.example.lab4.model.HabitResponse
import com.example.lab4.model.ScheduleResponse
import com.example.lab4.network.RetrofitClient
import retrofit2.Response
import com.example.lab4.model.UpdateScheduleRequest

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

    suspend fun getScheduleById(id: Long): retrofit2.Response<ScheduleResponse> {
        return api.getScheduleById(id)
    }

    suspend fun deleteSchedule(id: Long): retrofit2.Response<Unit> {
        return api.deleteSchedule(id)
    }

    suspend fun updateSchedule(id: Long, notes: String, duration: Int): retrofit2.Response<ScheduleResponse> {
        val request = UpdateScheduleRequest(notes = notes, duration_minutes = duration)
        return api.updateSchedule(id, request)
    }
}