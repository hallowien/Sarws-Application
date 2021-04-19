package com.example.sarwsapp2.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sarwsapp2.service.SessionManager
import com.example.sarwsapp.service.ApiClient
import com.example.sarwsapp2.R
import com.example.sarwsapp2.request.LoginRequest
import com.example.sarwsapp2.response.AlarmResponse
import com.example.sarwsapp2.response.ApiResponse
import com.example.sarwsapp2.response.LoginResponse
import com.example.sarwsapp2.service.CustomSharedPreferences
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.AccessController.checkPermission

class GenelMapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var customPreferences = CustomSharedPreferences()
    private var refreshTime = 10*1000L
    private val permissionCode = 1


    private val callback = OnMapReadyCallback { googleMap ->


        val loc = LatLng(38.4607268, 27.0949522)
        mMap = googleMap

        getAccces()



        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isRotateGesturesEnabled = true
        uiSettings.isScrollGesturesEnabled = true
        uiSettings.isTiltGesturesEnabled = true
        uiSettings.isZoomGesturesEnabled = true
        uiSettings.setAllGesturesEnabled(true)

        mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(requireContext()))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14.0F))

        val updateTime = customPreferences.getTime()


        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime > refreshTime){
            Log.e("süre", "doldu")
            loginToSystem()
        }
        loginToSystem()
    }
    private fun getAccces(){
        if(ContextCompat.checkSelfPermission(requireActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled = true
        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if(requestCode == permissionCode){
            if(grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                mMap.isMyLocationEnabled = true
            }
            else{
                Toast.makeText(requireActivity(), "User denied permission", Toast.LENGTH_LONG).show()
            }
        }
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
                                    "Battery Level: ${apiResponse.body()!!.BatteryLevel[0].value} "))




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

        customPreferences.saveTime(System.nanoTime())
    }

}