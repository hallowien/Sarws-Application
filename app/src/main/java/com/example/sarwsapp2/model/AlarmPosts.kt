package com.example.sarwsapp2.model

import com.example.sarwsapp2.model.AlarmDetail
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AlarmPosts(
        @SerializedName("id")
        @Expose
        var id: AlarmDetail,
        @SerializedName("createdTime")
        @Expose
        var createdTime: Float?,
        @SerializedName("tenantId")
        @Expose
        var tenantId: AlarmDetail,
        @SerializedName("type")
        @Expose
        var type: String?,
        @SerializedName("originator")
        @Expose
        var originator: AlarmDetail,
        @SerializedName("severity")
        @Expose
        var severity: String?,
        @SerializedName("status")
        @Expose
        var status: String?,
        @SerializedName("startTs")
        @Expose
        var startTs: Float?,
        @SerializedName("endTs")
        @Expose
        var endTs: Float?,
        @SerializedName("ackTs")
        @Expose
        var ackTs: Float?,
        @SerializedName("clearTs")
        @Expose
        var clearTs: Float?,
        @SerializedName("details")
        @Expose
        var details:  Any,
        @SerializedName("propagate")
        @Expose
        var propagate: Boolean?,
        @SerializedName("originatorName")
        @Expose
        var originatorName: String?,
        @SerializedName("name")
        @Expose
        var name: String?){

}