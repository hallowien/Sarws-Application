package com.example.sarwsapp

import android.content.Context
import android.util.Log
import com.example.sarws.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetchAuthToken()?.let {

            requestBuilder.addHeader("User-Agent", "PostmanRuntime/7.26.8")
            requestBuilder.addHeader("Connect-Type", "application/json")
            requestBuilder.addHeader("X-Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }


}