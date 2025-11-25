package com.example.lab4.repository

import android.content.Context
import com.example.lab4.model.ProfileResponse
import com.example.lab4.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import com.example.lab4.model.UpdateProfileRequest

class ProfileRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun getProfile(): Response<ProfileResponse> {
        return api.getMyProfile()
    }

    suspend fun uploadImage(file: File): Response<ProfileResponse> {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
        return api.uploadProfileImage(body)
    }

    suspend fun updateProfile(username: String, description: String): Response<ProfileResponse> {
        val request = UpdateProfileRequest(username, description)
        return api.updateProfile(request)
    }
}