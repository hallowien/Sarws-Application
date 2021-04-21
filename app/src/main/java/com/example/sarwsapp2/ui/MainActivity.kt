package com.example.sarwsapp2.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.sarwsapp2.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var tempFragment: Fragment

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setTitle("SARWS")


        supportFragmentManager.beginTransaction().add(R.id.fragmentTutucu, GenelMapsFragment()).commit()

        val nav: BottomNavigationView = findViewById(R.id.bottomnav)
        nav.setOnNavigationItemReselectedListener { menuItem ->

            if(menuItem.itemId == R.id.action1){
                tempFragment = GenelMapsFragment()
            }
            if(menuItem.itemId == R.id.action2){
                tempFragment = AlarmMapsFragment()
            }

            supportFragmentManager.beginTransaction().replace(R.id.fragmentTutucu, tempFragment).commit()


            true
        }
    }


}