package com.example.sarwsapp2.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.sarwsapp.service.ApiClient
import com.example.sarwsapp2.R
import com.example.sarwsapp2.request.LoginRequest
import com.example.sarwsapp2.response.ApiResponse
import com.example.sarwsapp2.response.LoginResponse
import com.example.sarwsapp2.service.SessionManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule


class GenelMapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    //private var customPreferences = CustomSharedPreferences()
    private var refreshTime = 1000L
    private val permissionCode = 1
    private var thiscontext = context


    private val callback = OnMapReadyCallback { googleMap ->

        // map adjusting

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

        //

        loginToSystem()

        Timer("SettingUp", false).schedule(120000) {
            refresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.refreshmenu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.action_refresh){
            refresh()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refresh(){
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        thiscontext = container!!.context
        return inflater.inflate(R.layout.fragment_genel_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setSubtitle("Updated in 2 minutes")

        mapFragment?.getMapAsync(callback)
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




    fun fetchPosts() {
        // Pass the token as parameter
        apiClient.getApiService(thiscontext!!).fetchPosts()
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
                                                "Battery Level: ${apiResponse.body()!!.BatteryLevel[0].value} ").icon(BitmapDescriptorFactory.fromResource(R.mipmap.device1)))


                    } else {
                        Log.e("device", "error")
                    }
                }
            })
    }



    fun loginToSystem(): Boolean {
        apiClient = ApiClient()
        sessionManager = SessionManager(thiscontext!!)

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
                        Log.e("login success", loginResponse!!.token)
                    } else {
                        Log.e("login", " error")
                    }
                }
            })

        return true
    }

}