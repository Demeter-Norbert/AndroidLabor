package com.example.lab4.repository

import android.content.Context
import com.example.lab4.model.AuthRequest
import com.example.lab4.network.RetrofitClient

class AuthRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)
    suspend fun login(email: String, password: String) =
        api.login(AuthRequest(email = email, password = password))
}
