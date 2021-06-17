package com.example.zolpe_05

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpActivitiy : AppCompatActivity() {
     private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        findViewById<View>(R.id.member_button).setOnClickListener(onClickListener) // 회원가입 버튼 클릭시 메소드 실행
        auth = Firebase.auth
    }

    //클릭 이벤트 Switch 문으로 넘겨진 id 값에 따라 메소드 처리
    var onClickListener =
        View.OnClickListener { v ->
            when (v.id) {
                R.id.member_button -> singUp()
            }
        }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun singUp() {

        val email = (findViewById<View>(R.id.signup_email) as EditText).text.toString()
        val password = (findViewById<View>(R.id.signup_password) as EditText).text.toString()
        val passwordCheck =
            (findViewById<View>(R.id.signup_passconfirm) as EditText).text.toString()
        if (email.length > 0 && password.length > 0 && passwordCheck.length > 0) //null 값 체크
        {
            // 패스워드 같을 때만 회원가입 완료
            if (password == passwordCheck) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            // 회원가입 성공시
                            val user = auth.currentUser
                            Toast.makeText(this@SignUpActivitiy, "회원가입이 성공했습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivitiy, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            //회원가입 실패시
                            Toast.makeText(
                                this@SignUpActivitiy, "회원가입이 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this@SignUpActivitiy, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@SignUpActivitiy, "이메일또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUI(user: FirebaseUser?) {

    }

    companion object {
        private const val TAG = "SignUpActivity"
    }
}