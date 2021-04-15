package com.example.sarwsapp2

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AlarmPosts(
    @SerializedName("id")
    @Expose
    var alarmid: List<AlarmDetail>,
    @SerializedName("createdTime")
    @Expose
    var createdTime: Float,
    @SerializedName("tenanId")
    @Expose
    var tenanId: List<AlarmDetail>,
    @SerializedName("type")
    @Expose
    var type: String,
    @SerializedName("originator")
    @Expose
    var id: List<AlarmDetail>,
    @SerializedName("severity")
    @Expose
    var severity: String,
    @SerializedName("status")
    @Expose
    var status: String,
    @SerializedName("startTs")
    @Expose
    var startTs: Int,
    @SerializedName("endTs")
    @Expose
    var endTs: Int,
    @SerializedName("ackTs")
    @Expose
    var ackTs: Int,
    @SerializedName("clearTs")
    @Expose
    var clearTs: Int,
    @SerializedName("details")
    @Expose
    var details: Int,
    @SerializedName("propagate")
    @Expose
    var propagate: Boolean,
    @SerializedName("originatorName")
    @Expose
    var originatorName: Int,
    @SerializedName("name")
    @Expose
    var name: String){

}