package com.example.lab4.repository

import android.content.Context
import com.example.lab4.model.ScheduleResponse
import kotlin.getValue
import com.example.lab4.network.RetrofitClient

public class ScheduleRepository(context: Context) {
    private val api by lazy {
        RetrofitClient.getInstance(context)
    }
    suspend fun getScheduleByDay(day: String): List<ScheduleResponse> {
        return api.getScheduleByDay(day)
    }
}
