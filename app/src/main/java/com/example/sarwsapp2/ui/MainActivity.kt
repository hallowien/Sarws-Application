package com.example.sarwsapp2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sarwsapp2.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var tempFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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