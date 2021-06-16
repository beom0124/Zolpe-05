package com.example.zolpe_05

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zolpe_05.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater)}

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private var RC_SIGN_IN = 9001

    // ...
// Initialize Firebase Auth
    //private var firebaseAuth : FirebaseAuth? = null
    //private var RC_SIGN_IN = 9001
    //private var googleSignInClient: GoogleSignInClient? = null
    //구글 계정 로그인

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setContentView(binding.root)

        //auth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        google_login.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        ///////////
        join.setOnClickListener {         //회원가입
            val memberShip = Intent(this, SignUpActivitiy::class.java)
            startActivity(memberShip)
        }

        login_btn.setOnClickListener {        //이메일로그인
            // 사용자의 아이디와 이메일  ( 현서 11/3일 )
            val email = (findViewById<View>(R.id.login_id) as EditText).text.toString()
            val password = (findViewById<View>(R.id.login_pw) as EditText).text.toString()
            if (email.length > 0 && password.length > 0) //null 값 체크
            {
                // 파이어베이스 Auth 로그인 메소드 ( 현서 11/3일 )
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this)
                    { task ->
                        // 로그인 성공시 ( 현서 11/3일 )
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            //val currentUserEmail =
                            user!!.getIdToken(true)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            //로그인 실패시  ( 현서 11/3일 )
                            Toast.makeText(
                                this@LoginActivity, "아이디와 비밀번호가 정확하지 않습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this@LoginActivity, "이메일또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    //
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show()       }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "FireBase 아이디 생성이 완료 되었습니다", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "signInWithCredential:failure", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    private fun updateUI(user: FirebaseUser?) {

    }
//    private fun signIn(){
//        google_login.setOnClickListener{
//            val signInIntent = googleSignInClient.signInIntent
//            startActivityForResult(signInIntent, RC_SIGN_IN)
//        }
//    }
    private fun signOut(){
        Firebase.auth.signOut()
    }

    //======================
    private fun signIn() {

    }
}