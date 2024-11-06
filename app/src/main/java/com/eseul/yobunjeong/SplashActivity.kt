package com.eseul.yobunjeong

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.eseul.yobunjeong.auth.LoginActivity

class SplashActivity : AppCompatActivity() {

    private val splashScreenDuration = 3000L  // 3초

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 상태바 숨기기 설정
        hideStatusBar()

        // 딜레이 후 로그인 화면으로 안전하게 전환
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing) {  // Activity가 아직 종료되지 않았다면 전환
                navigateToLogin()
            }
        }, splashScreenDuration)
    }

    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
