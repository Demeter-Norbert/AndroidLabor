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
import okhttp3.MultipartBody
import com.example.lab4.model.ProfileResponse
import com.example.lab4.model.UpdateProfileRequest
import retrofit2.http.PATCH
import com.example.lab4.model.UpdateScheduleRequest

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

    @GET("/schedule/{id}")
    suspend fun getScheduleById(@retrofit2.http.Path("id") id: Long): retrofit2.Response<ScheduleResponse>

    @retrofit2.http.DELETE("/schedule/{id}")
    suspend fun deleteSchedule(@retrofit2.http.Path("id") id: Long): retrofit2.Response<Unit>

    @GET("/profile")
    suspend fun getMyProfile(): retrofit2.Response<ProfileResponse>

    @retrofit2.http.Multipart
    @POST("/profile/upload-profile-image")
    suspend fun uploadProfileImage(@retrofit2.http.Part image: MultipartBody.Part): retrofit2.Response<ProfileResponse>

    @PATCH("/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): retrofit2.Response<ProfileResponse>

    @PATCH("/schedule/{id}")
    suspend fun updateSchedule(
        @retrofit2.http.Path("id") id: Long,
        @Body request: UpdateScheduleRequest
    ): retrofit2.Response<ScheduleResponse>
}