package com.example.zolpe_05

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CameraActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        var actionBar : ActionBar?

        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.home -> {
                val homeIntent = Intent(this, MainActivity::class.java)
                homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(homeIntent)
                finish()
                //인텐트 flag랑 finish 해도 안 넘어계속 중간에 메인엑티비티 나옴
                return true
            }
            R.id.camera -> {
                Toast.makeText(this,"이미 카메라 메뉴에 있습니다.", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.chat -> {
                val chatIntent = Intent(this, MainActivity::class.java)
                chatIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(chatIntent)
                finish()
                return true
            }
        }
        return false
    }
}
