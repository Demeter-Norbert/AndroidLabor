package com.example.lab4.model

data class ProfileResponse(
    val id: Long,
    val email: String,
    val username: String,
    val description: String?,
    val profileImageUrl: String?,
    val profileImageBase64: String?
)

data class UpdateProfileRequest(
    val username: String,
    val description: String
)