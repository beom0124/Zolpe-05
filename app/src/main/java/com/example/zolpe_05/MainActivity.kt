package com.example.zolpe_05

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.zolpe_05.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

//import com.example.firebasedatabase.databinding.ActivityMainBinding
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        val rightarrow = binding.rightArrow
        rightarrow.setOnClickListener {
            Toast.makeText(this, "right arrow", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.home -> {
                Toast.makeText(this,"이미 메인 화면 입니다",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.camera -> {
                val cameraIntent = Intent(this, CameraActivity::class.java)
                cameraIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(cameraIntent)
                return true
            }
            R.id.chat -> {
                val chatIntent = Intent(this, ChatActivity::class.java)
                chatIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(chatIntent)
                return true
            }
        }
        return false
    }
}