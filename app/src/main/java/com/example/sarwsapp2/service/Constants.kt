package com.example.sarwsapp2.service

object Constants {

    const val BASE_URL = "http://52.143.187.39/api/"
    const val LOGIN_URL = "auth/login"
    const val ALARM_URL = "alarm/DEVICE/e2e561d0-e605-11ea-879f-81ec0f3682aa?searchStatus,status,limit,startTime,endTime,ascOrder,offset,fetchOriginator&limit=5"
    //const val RSMSB_URL = "plugins/telemetry/DEVICE/e2e561d0-e605-11ea-879f-81ec0f3682aa/values/timeseries?limit=100&agg=NONE&keys=AirHumidity,MovingStatus,AirTemp"
    const val RSMSB_URL = "plugins/telemetry/DEVICE/e2e561d0-e605-11ea-879f-81ec0f3682aa/values/timeseries?limit=100&agg=NONE&keys=AirHumidity,MovingStatus,AirTemperature,Long,SurfaceTemperature,SurfaceConductivity,BatteryLevel,DeviceID,Lat,Illumination,BatteryCharging&startTs=1298423165617&endTs=1705185560680"
    const val POSTS_URL = "plugins/telemetry/DEVICE/e2e561d0-e605-11ea-879f-81ec0f3682aa/values/timeseries?limit=100&agg=NONE&keys=AirHumidity,MovingStatus,AirTemperature,Long,SurfaceTemperature,SurfaceConductivity,BatteryLevel,DeviceID,Lat,Illumination,BatteryCharging&startTs=1298423165617&endTs=1705185560680"

}