package com.example.zolpe_05

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        var actionBar : ActionBar?

        lateinit var bitmap: Bitmap
        lateinit var all : Bitmap
        lateinit var context : Context

        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.home -> {
                val homeIntent = Intent(this, MainActivity::class.java)
                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(homeIntent)
                finish()
                return true
            }
            R.id.camera -> {
                val cameraIntent = Intent(this, MainActivity::class.java)
                cameraIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(cameraIntent)
                finish()
                return true
            }
            R.id.chat -> {
                Toast.makeText(this,"이미 챗봇 메뉴에 있습니다.", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }
}
