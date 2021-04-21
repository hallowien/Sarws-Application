package com.example.sarwsapp2.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.sarwsapp.service.ApiClient
import com.example.sarwsapp2.R
import com.example.sarwsapp2.request.LoginRequest
import com.example.sarwsapp2.response.AlarmResponse
import com.example.sarwsapp2.response.LoginResponse
import com.example.sarwsapp2.service.SessionManager
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
import java.io.IOException
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.schedule




class AlarmMapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private val permissionCode = 1
    private var thiscontext = context

    private val callback = OnMapReadyCallback { googleMap ->

        val loc = LatLng(38.4607268, 27.0949522)
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))

        getAccces()

        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true

        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14.0F))

        Timer("SettingUp", false).schedule(120000) {
            Log.e("refresh", "hh")
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
        thiscontext = container!!.context
        return inflater.inflate(R.layout.fragment_alarm_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        loginToSystem()
        setHasOptionsMenu(true)

        mapFragment?.getMapAsync(callback)
    }


    fun getAlarms(){


        apiClient.getApiService(thiscontext!!).getAlarm()
            .enqueue(object : Callback<AlarmResponse> {
                override fun onFailure(call: Call<AlarmResponse>, t: Throwable) {

                }
                override fun onResponse(call: Call<AlarmResponse>, response: Response<AlarmResponse>) {
                    val alarmResponse = response
                    val currentposition = LatLng(38.4607268, 27.0949522)
                    if (alarmResponse != null) {

                        Log.e("id0", alarmResponse.body()!!.data[0].tenantId.id)
                        if (alarmResponse.body()!!.data != null) {
                            val data = alarmResponse.body()!!.data
                            for (i in data) {
                                if (i.originator.id == "e2e561d0-e605-11ea-879f-81ec0f3682aa") {
                                    mMap.addMarker(MarkerOptions()
                                            .position(currentposition)
                                            .title("${alarmResponse.body()!!.data[0].name}!").icon(BitmapDescriptorFactory.fromResource(R.mipmap.alarm)))
                                }
                            }
                        }
                    } else {
                        Log.e("device alarm", "error")
                    }
                }
            })
    }


    fun loginToSystem(){
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
                        getAlarms()
                        Log.e("login success", loginResponse!!.token)
                    } else {
                        Log.e("login", " error")
                    }
                }
            })
    }
}