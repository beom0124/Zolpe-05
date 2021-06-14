package com.example.zolpe_05

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.zolpe_05.databinding.ActivityMagazineBinding
import com.example.zolpe_05.ui.ChatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MagazineActivity : AppCompatActivity() , BottomNavigationView.OnNavigationItemSelectedListener {
    val binding by lazy { ActivityMagazineBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var actionBar: ActionBar?

        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val homeIntent = Intent(this, MainActivity::class.java)
                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(homeIntent)
                finish()
                //인텐트 flag랑 finish 해도 안 넘어계속 중간에 메인엑티비티 나옴
                return true
            }
            R.id.magazine -> {
                Toast.makeText(this, "이미 매거진 메뉴에 있습니다.", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.chat -> {
                val chatIntent = Intent(this, ChatActivity::class.java)
                chatIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(chatIntent)
                finish()
                return true
            }
        }
        return false

    }
}