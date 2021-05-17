package com.example.zolpe_05

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.zolpe_05.databinding.ActivityCameraBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

var file: File? = null

class CameraActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var actionBar : ActionBar?

        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        val sdcard: File = Environment.getExternalStorageDirectory() //여기가 문제
        //val sdcard: File = Environment.getExternalFilesDir()
        file = File(sdcard, "capture.jpg")
        binding.button.setOnClickListener {
            capture()
        }

    }

    fun capture() { //카메라 촬영  인텐트 시작하는 함수
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val options = BitmapFactory.Options()
            options.inSampleSize = 8
            val bitmap = BitmapFactory.decodeFile(file!!.absolutePath, options)
            binding.imageView.setImageBitmap(bitmap)
            //imageView!!.setImageBitmap(bitmap)
        }
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

