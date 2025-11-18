package com.example.lab4.network
import android.content.Context
import com.example.lab4.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager =
        SessionManager(context.applicationContext)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }


}
