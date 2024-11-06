package com.eseul.yobunjeong

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eseul.yobunjeong.fragment.CameraFragment
import com.eseul.yobunjeong.fragment.GuideFragment
import com.eseul.yobunjeong.fragment.HomeFragment
import com.eseul.yobunjeong.fragment.MapFragment
import com.eseul.yobunjeong.fragment.ShopFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // 초기화면으로 HomeFragment 설정
        if (savedInstanceState == null) {  // 중복 추가 방지
            loadFragment(HomeFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.home -> HomeFragment()
                R.id.guide -> GuideFragment()
                R.id.camera -> CameraFragment()
                R.id.map -> MapFragment()
                R.id.shop -> ShopFragment()
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
