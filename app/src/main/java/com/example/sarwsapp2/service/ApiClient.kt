package com.example.sarwsapp.service

import android.content.Context
import com.example.sarwsapp.AuthInterceptor
import com.example.sarwsapp2.service.ApiService
import com.example.sarwsapp2.service.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private lateinit var apiService: ApiService


    fun getApiService(context: Context): ApiService {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okhttpClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }


    private fun okhttpClient(context: Context): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY };


        return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .addNetworkInterceptor(interceptor)
                .build()
    }



}
