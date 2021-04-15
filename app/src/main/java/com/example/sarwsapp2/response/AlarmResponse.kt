package com.example.sarwsapp2.response

import com.example.sarwsapp2.AlarmPosts
import com.example.sarwsapp2.Posts
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AlarmResponse (
    @SerializedName("data")
    @Expose
    var data: List<AlarmPosts>) {
}