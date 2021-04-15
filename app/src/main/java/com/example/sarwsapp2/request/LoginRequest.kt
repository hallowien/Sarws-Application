package com.example.sarwsapp2.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("username")
    @Expose
    var username: String,
    @SerializedName("password")
    @Expose
    var password: String){

}