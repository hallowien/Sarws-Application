package com.example.sarwsapp2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AlarmDetail(
    @SerializedName("entityType")
    @Expose
    var entityType: String,
    @SerializedName("id")
    @Expose
    var id: String){
}