package com.example.sarwsapp2.ui

import android.app.Application
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

class AlarmMapsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_alarm_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        loginToSystem()
        mapFragment?.getMapAsync(callback)
    }


    fun getAlarms(){
        apiClient.getApiService(requireContext()).getAlarm()
            .enqueue(object : Callback<AlarmResponse> {
                override fun onFailure(call: Call<AlarmResponse>, t: Throwable) {
                    Log.e("alarm", "error")
                }

                override fun onResponse(call: Call<AlarmResponse>, response: Response<AlarmResponse>) {
                    val alarmResponse = response
                    val currentposition = LatLng(38.4607268, 27.0949522)
                    if (alarmResponse != null) {

                        Log.e("id0", alarmResponse.body()!!.data[0].tenanId[0].detailid )

                        if(alarmResponse.body()!!.data[0].tenanId[0].detailid == "e2e561d0-e605-11ea-879f-81ec0f3682aa"){
                            mMap.addMarker(MarkerOptions()
                                .position(currentposition)
                                .title("RSMSB1")
                                .snippet( "${alarmResponse.body()!!.data[0].name}"))
                        }


                    } else {
                        Log.e("device alarm", "error")
                    }
                }
            })
    }


    fun loginToSystem(){
        apiClient = ApiClient()
        sessionManager = SessionManager(this.requireContext())

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
                        getAlarms()
                        Log.e("login success", loginResponse!!.token)
                    } else {
                        Log.e("login"," error")
                    }
                }
            })
    }
}