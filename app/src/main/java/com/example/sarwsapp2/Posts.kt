package com.example.sarwsapp2

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Posts(@SerializedName("ts")
            @Expose
            var ts: Float,
            @SerializedName("value")
            @Expose
            var value: String) {
}