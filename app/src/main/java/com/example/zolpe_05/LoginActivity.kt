package com.example.zolpe_05

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.zolpe_05.databinding.ActivityLoginBinding
import com.example.zolpe_05.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)}

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var actionBar : ActionBar?

        actionBar = supportActionBar
        actionBar?.hide()

        binding.buttonLogin.setOnClickListener { //버튼 왜 안됨?
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
                if(binding.heightInsert.getText().toString() == null || binding.weightInsert.getText().toString() == null || binding.topsizeInsert.getText().toString() == null || binding.undersizeInsert.getText().toString() == null
                        || binding.googleIdInsert.getText().toString() == null || binding.googlePwInsert.getText().toString() == null){
                Toast.makeText(this, "빈칸 없이 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}