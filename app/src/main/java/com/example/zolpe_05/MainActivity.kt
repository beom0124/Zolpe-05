package com.example.zolpe_05

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var actionBar : ActionBar?

        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        //supportFragmentManager.beginTransaction().add(R.id.linearLayout, HomeFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.home -> {
                Toast.makeText(this,"홈이예찬",Toast.LENGTH_SHORT).show()
                //supportFragmentManager.beginTransaction().replace(R.id.linearLayout , HomeFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.camera -> {
                Toast.makeText(this,"카메라",Toast.LENGTH_SHORT).show()
                //supportFragmentManager.beginTransaction().replace(R.id.linearLayout, TVFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.chat -> {
                Toast.makeText(this,"챗봇",Toast.LENGTH_SHORT).show()
                //supportFragmentManager.beginTransaction().replace(R.id.linearLayout, CalendarFragment()).commitAllowingStateLoss()
                return true
            }
        }

        return false
    }
}