package com.example.sarwsapp2.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse
    (@SerializedName("status_code")
     var statusCode: Int,
    @SerializedName("token")
     var token: String,
     @SerializedName("refreshToken")
     var refreshToken: String){

}