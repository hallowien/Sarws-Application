package com.example.sarwsapp2.response

import com.example.sarwsapp2.model.AlarmPosts
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AlarmResponse (
    @SerializedName("data")
    @Expose
    var data: List<AlarmPosts>,
    @SerializedName("nextPageLink")
    @Expose
    var nextPageLink: String?,
    @SerializedName("hasNext")
    @Expose
    var hasNext:Boolean
) {
}