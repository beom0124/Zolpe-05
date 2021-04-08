package com.example.zolpe_05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*var bottomnavigationview = BottomNavigationView
        bottomnavigationview.setOnNavigationItemSelectedListener(this)*/

        val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                    R.id.home -> {
                        supportFragmentManager.beginTransaction().replace(R.id.linearLayout , HomeFragment()).commitAllowingStateLoss()
                        return true
                    }
                    R.id.camera -> {
                        supportFragmentManager.beginTransaction().replace(R.id.linearLayout, TVFragment()).commitAllowingStateLoss()
                        return true
                    }
                    R.id.chat -> {
                        supportFragmentManager.beginTransaction().replace(R.id.linearLayout, CalendarFragment()).commitAllowingStateLoss()
                        return true
                    }
                }

            }
            false
        }

        supportFragmentManager.beginTransaction().add(R.id.linearLayout, HomeFragment()).commit()
    }
 /*override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.home -> {
                supportFragmentManager.beginTransaction().replace(R.id.linearLayout , HomeFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.camera -> {
                supportFragmentManager.beginTransaction().replace(R.id.linearLayout, TVFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.chat -> {
                supportFragmentManager.beginTransaction().replace(R.id.linearLayout, CalendarFragment()).commitAllowingStateLoss()
                return true
            }
        }*/

        return false
    }