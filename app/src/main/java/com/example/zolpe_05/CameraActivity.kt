package com.example.zolpe_05

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.zolpe_05.databinding.ActivityCameraBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var actionBar: ActionBar?

        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        //settingPermission()

        binding.button.setOnClickListener {
            startCapture()
        }

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
            R.id.camera -> {
                Toast.makeText(this, "이미 카메라 메뉴에 있습니다.", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.chat -> {
                val chatIntent = Intent(this, MainActivity::class.java)
                chatIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(chatIntent)
                finish()
                return true
            }
        }
        return false
    }

//    fun settingPermission() {
//        var permis = object : PermissionListener {
//            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
//            override fun onPermissionGranted() {
//                Toast.makeText(this@CameraActivity, "권한 허가", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//                Toast.makeText(this@CameraActivity, "권한 거부", Toast.LENGTH_SHORT)
//                    .show()
//                ActivityCompat.finishAffinity(this@CameraActivity) // 권한 거부시 앱 종료
//            }
//        }
//
//        TedPermission.with(this)
//            .setPermissionListener(permis)
//            .setRationaleMessage("카메라 사진 권한 필요")
//            .setDeniedMessage("카메라 권한 요청 거부")
//            .setPermissions(
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                android.Manifest.permission.CAMERA
//            )
//            .check()
//    }

    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.zolpe_05",
                        it
                    )
                    //takePictureIntent.setFlags()
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(contentResolver, Uri.fromFile(file))
                binding.imageView.setImageBitmap(bitmap)
            } else {
                val decode = ImageDecoder.createSource(
                    this.contentResolver,
                    Uri.fromFile(file)
                )
                val bitmap = ImageDecoder.decodeBitmap(decode)
                binding.imageView.setImageBitmap(bitmap)
            }
        }
    }
}