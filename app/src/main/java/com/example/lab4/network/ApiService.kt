package com.example.lab4.network
import com.example.lab4.model.AuthRequest
import com.example.lab4.model.AuthResponse
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

}