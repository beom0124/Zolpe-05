package com.example.zolpe_05

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            if(height_insert.isNullOrEmpty() && weight_insert.isNullOrEmpty() && topsize_insert.isNullEmpty() && undersize_insert.isNullOrEmpty() && google_id_insert.isNullEmpty() && google_pw_insert.isNullEmpty()){
                Toast.makeText(this, "정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}