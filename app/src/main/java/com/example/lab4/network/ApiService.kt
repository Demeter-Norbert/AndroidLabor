package com.example.lab4.network
import com.example.lab4.model.AuthRequest
import com.example.lab4.model.AuthResponse
import com.example.lab4.model.CreateHabitRequest
import com.example.lab4.model.CreateScheduleRequest
import com.example.lab4.model.HabitCategory
import com.example.lab4.model.HabitResponse
import com.example.lab4.model.ScheduleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @GET("/schedule/day")
    suspend fun getScheduleByDay(@Query("date") day: String):
            List<ScheduleResponse>

    @GET("/habit/categories")
    suspend fun getHabitCategories(): List<HabitCategory>

    @POST("/habit")
    suspend fun createHabit(@Body request: CreateHabitRequest): Response<HabitResponse>

    @GET("/habit")
    suspend fun getAllHabits(): List<HabitResponse>

    @POST("/schedule/recurring")
    suspend fun createSchedule(@Body request: CreateScheduleRequest): Response<List<ScheduleResponse>>
}