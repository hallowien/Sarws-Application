package com.example.sarwsapp2.ui

import android.content.Context
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sarws.SessionManager
import com.example.sarwsapp.CustomInfoWindowForGoogleMap
import com.example.sarwsapp.service.ApiClient
import com.example.sarwsapp2.R
import com.example.sarwsapp2.request.LoginRequest
import com.example.sarwsapp2.response.AlarmResponse
import com.example.sarwsapp2.response.ApiResponse
import com.example.sarwsapp2.response.LoginResponse

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenelMapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient


    private val callback = OnMapReadyCallback { googleMap ->

        val loc = LatLng(38.4607268, 27.0949522)
        mMap = googleMap
        mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(requireContext()))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))

        loginToSystem()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_genel_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    fun getAlarms(){
        apiClient.getApiService(requireContext()).getAlarm()
            .enqueue(object : Callback<AlarmResponse> {
                override fun onFailure(call: Call<AlarmResponse>, t: Throwable) {
                    Log.e("alarm", "error")
                }

                override fun onResponse(call: Call<AlarmResponse>, response: Response<AlarmResponse>) {
                    val apiResponse = response
                    if (apiResponse != null) {



                    } else {
                        Log.e("device alarm", "error")
                    }
                }
            })
    }



    fun fetchPosts() {
        // Pass the token as parameter
        apiClient.getApiService(requireContext()).fetchPosts()
            .enqueue(object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("fetching", "error")
                }

                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    val apiResponse = response
                    if (apiResponse.body() != null) {
                        Log.e("body", apiResponse.body()!!.Long[3].value.toString())

                        val currentposition = LatLng(38.4607268, 27.0949522)
                        //val currentposition =  LatLng(apiResponse.body()!!.Lat[0].value.toDouble(), apiResponse.body()!!.Long[0].value.toDouble())

                        mMap.addMarker(
                            MarkerOptions()
                                .position(currentposition)
                                .title("RSMSB1")
                                .snippet("Air humidity: ${apiResponse.body()!!.AirHumidity[0].value} \n" +
                                    "Air Temperature: ${apiResponse.body()!!.AirTemperature[0].value} \n" +
                                    "Surface Temperature: ${apiResponse.body()!!.SurfaceTemperature[0].value} \n" +
                                    "Surface Conductivity: ${apiResponse.body()!!.SurfaceConductivity[0].value} \n" +
                                    "Battery Level: ${apiResponse.body()!!.BatteryLevel[0].value} ")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name))
                        )



                    } else {
                        Log.e("device", "error")
                    }
                }
            })
    }


    fun loginToSystem(){
        apiClient = ApiClient()
        sessionManager = SessionManager(requireContext())

        apiClient.getApiService(requireContext())
            .login(LoginRequest(username = "sarws@turkcell.com.tr", password = "sarws1234"))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Error logging in
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        sessionManager.saveAuthToken(loginResponse.token.toString())
                        fetchPosts()
                        getAlarms()
                        Log.e("login success", loginResponse!!.token)
                    } else {
                        Log.e("login"," error")
                    }
                }
            })
    }

}