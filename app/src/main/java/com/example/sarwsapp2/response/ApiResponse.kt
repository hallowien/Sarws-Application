package com.example.sarwsapp2.response


import com.example.sarwsapp2.Posts
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiResponse (
        @SerializedName("AirHumidity")
        @Expose
        var AirHumidity: List<Posts>,
        @SerializedName("MovingStatus")
        @Expose
        var MovingStatus: List<Posts>,
        @SerializedName("AirTemperature")
        @Expose
        var AirTemperature: List<Posts>,
        @SerializedName("Long")
        @Expose
        var Long: List<Posts>,
        @SerializedName("SurfaceTemperature")
        @Expose
        var SurfaceTemperature: List<Posts>,
        @SerializedName("SurfaceConductivity")
        @Expose
        var SurfaceConductivity: List<Posts>,
        @SerializedName("BatteryLevel")
        @Expose
        var BatteryLevel: List<Posts>,
        @SerializedName("DeviceID")
        @Expose
        var DeviceID: List<Posts>,
        @SerializedName("Lat")
        @Expose
        var Lat: List<Posts>,
        @SerializedName("Illumination")
        @Expose
        var Illumination: List<Posts>,
        @SerializedName("BatteryCharging")
        @Expose
        var BatteryCharging: List<Posts>){
}