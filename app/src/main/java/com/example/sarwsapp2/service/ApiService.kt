package com.example.sarwsapp2.service

import com.example.sarwsapp2.request.LoginRequest
import com.example.sarwsapp2.response.AlarmResponse
import com.example.sarwsapp2.response.ApiResponse
import com.example.sarwsapp2.response.LoginResponse

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    @GET(Constants.POSTS_URL)
    fun fetchPosts(): Call<ApiResponse>
    @GET(Constants.ALARM_URL)
    fun getAlarm(): Call<AlarmResponse>

}
