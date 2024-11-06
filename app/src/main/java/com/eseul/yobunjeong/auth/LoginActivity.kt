package com.eseul.yobunjeong.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider // ViewModelProvider 임포트
import com.eseul.yobunjeong.MainActivity
import com.eseul.yobunjeong.R
import com.eseul.yobunjeong.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel // lateinit으로 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // ViewModel 초기화
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // UI 요소 초기화
        val emailEditText: EditText = findViewById(R.id.login_Id)
        val passwordEditText: EditText = findViewById(R.id.login_Password)
        val loginButton: Button = findViewById(R.id.loginBtn)
        val joinTextView: TextView = findViewById(R.id.join)

        // ViewModel에서 로그인 결과를 관찰
        loginViewModel.loginResult.observe(this, Observer { response ->
            response?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } ?: run {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        })


        // 로그인 버튼 클릭 이벤트
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.loginUser(email, password)
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 텍스트 클릭 이벤트
        joinTextView.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}
